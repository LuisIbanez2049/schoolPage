package com.eschool.schoolpage.controllers;

import com.eschool.schoolpage.dtos.MateriaDTO;
import com.eschool.schoolpage.dtos.RecordModificarMateria;
import com.eschool.schoolpage.dtos.RecordNewMateria;
import com.eschool.schoolpage.models.Materia;
import com.eschool.schoolpage.models.Rol;
import com.eschool.schoolpage.models.Usuario;
import com.eschool.schoolpage.repositories.MateriaRepository;
import com.eschool.schoolpage.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/materias")
public class MateriaController {

    @Autowired
    private MateriaRepository materiaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/")
    public ResponseEntity<?> getAllMaterias(){
        List<MateriaDTO> materiaDTOS = materiaRepository.findAll().stream().filter(materia -> materia.isAsset())
                .map(materia -> new MateriaDTO(materia)).collect(Collectors.toList());
        return new ResponseEntity<>(materiaDTOS, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> crearMateria(Authentication authentication, @RequestBody RecordNewMateria recordNewMateria){
        try {
            Usuario usuario = usuarioRepository.findByMail(authentication.getName());
            if (usuario == null) {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
            if (usuario.getRol() != Rol.ADMIN) {
                return new ResponseEntity<>("No tienes permisos para realizar esta accion", HttpStatus.FORBIDDEN);
            }
            if (recordNewMateria.nombre().isBlank()) {
                return new ResponseEntity<>("El nombre debe ser especificado.", HttpStatus.FORBIDDEN);
            }
            if (recordNewMateria.descripcion().isBlank()) {
                return new ResponseEntity<>("La descripcion debe ser especificada.", HttpStatus.FORBIDDEN);
            }
            if (recordNewMateria.portada().isBlank()) {
                return new ResponseEntity<>("La portada debe ser especificada.", HttpStatus.FORBIDDEN);
            }
            Materia newMateria = new Materia(recordNewMateria.nombre(), recordNewMateria.descripcion(), recordNewMateria.portada());
            materiaRepository.save(newMateria);
            return new ResponseEntity<>("La materia " + recordNewMateria.nombre() + " fue creada exitosamente.", HttpStatus.OK);

        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @PatchMapping("/modificarMateria")
    public ResponseEntity<?> modificarMateria(Authentication authentication,@RequestBody RecordModificarMateria recordModificarMateria){
        try {
            Usuario usuario = usuarioRepository.findByMail(authentication.getName());
            Materia materia = materiaRepository.findById(recordModificarMateria.idMateria()).orElse(null);
            if (usuario.getRol() != Rol.ADMIN) {
                return new ResponseEntity<>("No tienes permisos para realizar esta accion", HttpStatus.FORBIDDEN);
            }
            if (usuario == null) {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
            if (materia == null) {
                return new ResponseEntity<>("Materia no encontrada", HttpStatus.NOT_FOUND);
            }
            if (!recordModificarMateria.nombre().isEmpty()) {
                materia.setNombre(recordModificarMateria.nombre());
                materiaRepository.save(materia);
                return new ResponseEntity<>("Titulo modificado con exito", HttpStatus.OK);
            }
            if (!recordModificarMateria.descripcion().isEmpty()) {
                materia.setDescripcion(recordModificarMateria.descripcion());
                materiaRepository.save(materia);
                return new ResponseEntity<>("Descripción modificado con exito", HttpStatus.OK);

            }
            if (!recordModificarMateria.portada().isEmpty()) {
                materia.setPortada(recordModificarMateria.portada());
                materiaRepository.save(materia);
                return new ResponseEntity<>("Portada modificado con exito", HttpStatus.OK);

            }
            return new ResponseEntity<>("", HttpStatus.OK);


        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @DeleteMapping("/desactivar/{id}")
    public ResponseEntity<?> borrarMateria(Authentication authentication,@PathVariable Long id ){
        try {
            Materia materia = materiaRepository.findById(id).orElse(null);
            if (materia == null) {
                return new ResponseEntity<>("Materia no encontrada", HttpStatus.NOT_FOUND);
            }
            materia.setAsset(false);
            materiaRepository.save(materia);
            return new ResponseEntity<>("Materia desactivada exitosamente.", HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @PatchMapping("/activar/{id}")
    public ResponseEntity<?> activarMateria(Authentication authentication,@PathVariable Long id ){
        try {
            Usuario usuario = usuarioRepository.findByMail(authentication.getName());

            Materia materia = materiaRepository.findById(id).orElse(null);
            if (materia == null) {
                return new ResponseEntity<>("Materia no encontrada", HttpStatus.NOT_FOUND);
            }
            if (usuario == null) {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
            if (usuario.getRol().equals(Rol.ESTUDIANTE)) {
                return new ResponseEntity<>("NO TIENES PERMISO PARA REALIZAR ESTA ACCION", HttpStatus.FORBIDDEN);
            }
            if (materia.isAsset()) {
                return new ResponseEntity<>("La materia " + materia.getNombre() + " esta activa actualmente", HttpStatus.NOT_FOUND);
            }
            materia.setAsset(true);
            materiaRepository.save(materia);
            return new ResponseEntity<>("Materia activada exitosamente.", HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }


}
