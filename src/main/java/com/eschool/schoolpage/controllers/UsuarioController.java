package com.eschool.schoolpage.controllers;

import com.eschool.schoolpage.dtos.MateriaDTO;
import com.eschool.schoolpage.dtos.RecordLoginMateria;
import com.eschool.schoolpage.dtos.RecordSalirDeMateria;
import com.eschool.schoolpage.dtos.UsuarioDTO;
import com.eschool.schoolpage.models.JornadaTurno;
import com.eschool.schoolpage.models.Materia;
import com.eschool.schoolpage.models.Usuario;
import com.eschool.schoolpage.models.UsuarioMateria;
import com.eschool.schoolpage.repositories.MateriaRepository;
import com.eschool.schoolpage.repositories.UsuarioMateriaRepository;
import com.eschool.schoolpage.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.rsocket.context.RSocketPortInfoApplicationContextInitializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private MateriaRepository materiaRepository;
    @Autowired
    private UsuarioMateriaRepository usuarioMateriaRepository;

    @GetMapping("/")
    public ResponseEntity<?> getallUsuariosActivos(Authentication authentication){
        List<UsuarioDTO> usuarioDTOS = usuarioRepository.findAll().stream().filter(usuario -> usuario.isAsset())
                .map(cadaUsuario -> new UsuarioDTO(cadaUsuario)).collect(Collectors.toList());
        return new ResponseEntity<>(usuarioDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findUsuario(Authentication authentication, @PathVariable Long id){
        try {
            Usuario usuario = usuarioRepository.findById(id).orElse(null);
            if (usuario == null) {
                return new ResponseEntity<>("Usuario no encontrado.", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new UsuarioDTO(usuario), HttpStatus.OK);
        }  catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }


    @DeleteMapping("/leaveSubject")
    public ResponseEntity<?> salirDeMateria(Authentication authentication, @RequestBody RecordSalirDeMateria recordSalirDeMateria){
        Usuario usuario = usuarioRepository.findById(recordSalirDeMateria.idUsuario()).orElse(null);
        Materia materia = materiaRepository.findById(recordSalirDeMateria.idMateria()).orElse(null);
        if (materia == null) {
            return new ResponseEntity<>("materia no encontrada", HttpStatus.NOT_FOUND);
        }
        if (usuario == null) {
            return new ResponseEntity<>("usuario no encontrado", HttpStatus.NOT_FOUND);
        }

        UsuarioMateria usuarioMateriaFiltrado = usuario.getUsuarioMaterias().stream().filter(usuarioMateria -> usuarioMateria.getMateria().getNombre().equals(materia.getNombre()))
                .findFirst().orElse(null);
        if (usuarioMateriaFiltrado == null) {
            return new ResponseEntity<>("usuario materia no encontrado", HttpStatus.NOT_FOUND);
        }
        if (!usuarioMateriaFiltrado.isAsset()) {
            return new ResponseEntity<>("No perteneces a esta materia", HttpStatus.NOT_FOUND);
        }

        else { usuarioMateriaFiltrado.setAsset(false); }
        usuarioMateriaRepository.save(usuarioMateriaFiltrado);


        usuarioRepository.save(usuario);
        return new ResponseEntity<>("Usuario: " + usuario.getName() + " " + usuario.getLastName() + " salio de la materia " + materia.getNombre(), HttpStatus.OK);
    }

    @PostMapping("/loginMateria")
    public ResponseEntity<?> entrarEnMateria(Authentication authentication, @RequestBody RecordLoginMateria recordLoginMateria){
        try {
            Usuario usuario = usuarioRepository.findByMail(authentication.getName());
            Materia materia = materiaRepository.findById(recordLoginMateria.idMateria()).orElse(null);
            if (materia == null) {
                return new ResponseEntity<>("Materia no encontrda", HttpStatus.NOT_FOUND);
            }
            if (usuario == null) {
                return new ResponseEntity<>("Usuario no encontrda", HttpStatus.NOT_FOUND);
            }
            UsuarioMateria usuarioMateriaa = materia.getUsuarioMaterias().stream().filter(usuarioMateria -> usuarioMateria.getUsuario().getId().equals(usuario.getId())).findFirst().orElse(null);
            JornadaTurno jornadaTurno = JornadaTurno.MORNIG;
            if (recordLoginMateria.turno().equalsIgnoreCase("EVENING")) {
                jornadaTurno = JornadaTurno.EVENING;
            }
            if (recordLoginMateria.turno().equalsIgnoreCase("NIGHT")) {
                jornadaTurno = JornadaTurno.NIGHT;
            }
            if (usuarioMateriaa != null && usuarioMateriaa.isAsset()) {
                return new ResponseEntity<>("YA ESTAS DENTRO DE LA MATERIA " + materia.getNombre(), HttpStatus.OK);
            }
            if (usuarioMateriaa != null && !usuarioMateriaa.isAsset()) {
                usuarioMateriaa.setAsset(true);
                usuarioMateriaa.setJornadaTurno(jornadaTurno);
                usuarioMateriaRepository.save(usuarioMateriaa);
                return new ResponseEntity<>("TE UNISTE A LA MATERIA " + materia.getNombre() + " EXITOSAMENTE", HttpStatus.OK);
            }

            UsuarioMateria newUsuarioMateria = new UsuarioMateria(jornadaTurno);
            usuario.addUsuarioMateria(newUsuarioMateria);
            materia.addUsuarioMateria(newUsuarioMateria);
            usuarioMateriaRepository.save(newUsuarioMateria);

            return new ResponseEntity<>("TE UNISTE A LA MATERIA " + materia.getNombre() + " EXITOSAMENTE", HttpStatus.OK);


        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

}
