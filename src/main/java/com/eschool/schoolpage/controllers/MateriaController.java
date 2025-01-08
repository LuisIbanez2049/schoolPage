package com.eschool.schoolpage.controllers;

import com.eschool.schoolpage.dtos.MateriaDTO;
import com.eschool.schoolpage.dtos.RecordModificarMateria;
import com.eschool.schoolpage.dtos.RecordNewMateria;
import com.eschool.schoolpage.dtos.UsuarioDTO;
import com.eschool.schoolpage.models.Materia;
import com.eschool.schoolpage.models.Rol;
import com.eschool.schoolpage.models.Usuario;
import com.eschool.schoolpage.models.UsuarioMateria;
import com.eschool.schoolpage.repositories.MateriaRepository;
import com.eschool.schoolpage.repositories.UsuarioMateriaRepository;
import com.eschool.schoolpage.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/materias")
public class MateriaController {

    @Autowired
    private MateriaRepository materiaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioMateriaRepository usuarioMateriaRepository;

    @GetMapping("/")
    public ResponseEntity<?> getAllMaterias(Authentication authentication){
        List<MateriaDTO> materiaDTOS = materiaRepository.findAll().stream().filter(materia -> materia.isAsset())
                .map(materia -> new MateriaDTO(materia)).collect(Collectors.toList()).stream().sorted(Comparator.comparing(MateriaDTO::getId).reversed()).collect(Collectors.toList());
        return new ResponseEntity<>(materiaDTOS, HttpStatus.OK);
    }

    @GetMapping("/availablesubjects")
    public ResponseEntity<?> getAllMateriasavailableforAuthenticatedUser(Authentication authentication){
        Usuario usuario = usuarioRepository.findByMail(authentication.getName());
        if (usuario == null) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }
        List<MateriaDTO> materiasFromUser = usuario.getUsuarioMaterias().stream().filter(usuarioMateria -> usuarioMateria.isAsset()).map(usuarioMateria -> new MateriaDTO(usuarioMateria.getMateria())).collect(Collectors.toList());
        if (materiasFromUser == null) {
            return new ResponseEntity<>("THERE ISN´T SUBJECTS YET", HttpStatus.NOT_FOUND);
        }
        Set<Long> materiasFromUserIds = materiasFromUser.stream().map(materiaDTO -> materiaDTO.getId()).collect(Collectors.toSet());
        List<MateriaDTO> materiaDTOSavailable = materiaRepository.findAll().stream().filter(materia -> !materiasFromUserIds.contains(materia.getId()))
                .map(materia -> new MateriaDTO(materia)).collect(Collectors.toList());
        return new ResponseEntity<>(materiaDTOSavailable, HttpStatus.OK);
    }

    @GetMapping("/mysubjects")
    public ResponseEntity<?> getAllMateriasFromAuthenticatedUser(Authentication authentication){
        Usuario usuario = usuarioRepository.findByMail(authentication.getName());
        if (usuario == null) {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }
        UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);
        List<MateriaDTO> materiaDTOS = usuario.getUsuarioMaterias().stream().filter(usuarioMateria -> usuarioMateria.isAsset()).map(usuarioMateria -> new MateriaDTO(usuarioMateria.getMateria())).collect(Collectors.toList())
                .stream().sorted(Comparator.comparing(MateriaDTO::getId).reversed()).collect(Collectors.toList());
        if (materiaDTOS.isEmpty() || materiaDTOS == null) {
            return new ResponseEntity<>("No hay meterias", HttpStatus.NOT_FOUND);
        }
        //List<MateriaDTO> materiaDTOS = usuarioDTO.getUsuarioMaterias().
        return new ResponseEntity<>(materiaDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findMateriaById(Authentication authentication, @PathVariable Long id){
        try {
            Materia materia = materiaRepository.findById(id).orElse(null);
            if (materia == null) {
                return new ResponseEntity<>("Materia not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new MateriaDTO(materia), HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
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
            Materia lastMateria = materiaRepository.findTopByOrderByIdDesc();
            if (lastMateria == null) {
                return new ResponseEntity<>("Last subject not found", HttpStatus.NOT_FOUND);
            }
            String color;
            if (lastMateria.getColor().equals("#a1d9d9")) {
                color = "#a2b38b";
            } else if (lastMateria.getColor().equals("#a2b38b")) {
                color = "#c8677f";
            } else { color = "#a1d9d9"; }
            if (recordNewMateria.accessCode().isBlank()) {
                return new ResponseEntity<>("El codigo de acceso debe ser especificado.", HttpStatus.FORBIDDEN);
            }
            Materia newMateria = new Materia(recordNewMateria.nombre(), recordNewMateria.descripcion(), recordNewMateria.portada(), color, recordNewMateria.accessCode());
            materiaRepository.save(newMateria);
            return new ResponseEntity<>("La materia " + recordNewMateria.nombre() + " fue creada exitosamente.", HttpStatus.OK);

        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @PatchMapping("/modificarMateria")
    public ResponseEntity<?> modificarMateria(Authentication authentication,@RequestBody RecordModificarMateria recordModificarMateria){
        try {
            Usuario usuario = usuarioRepository.findByMail(authentication.getName());
            Materia materia = materiaRepository.findById(recordModificarMateria.idMateria()).orElse(null);
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
