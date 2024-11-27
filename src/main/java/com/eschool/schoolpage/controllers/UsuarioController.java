package com.eschool.schoolpage.controllers;

import com.eschool.schoolpage.dtos.MateriaDTO;
import com.eschool.schoolpage.dtos.RecordSalirDeMateria;
import com.eschool.schoolpage.dtos.UsuarioDTO;
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
    public ResponseEntity<?> getallUsuariosActivos(){
        List<UsuarioDTO> usuarioDTOS = usuarioRepository.findAll().stream().filter(usuario -> usuario.isAsset())
                .map(cadaUsuario -> new UsuarioDTO(cadaUsuario)).collect(Collectors.toList());
        return new ResponseEntity<>(usuarioDTOS, HttpStatus.OK);
    }


    @DeleteMapping("/leaveSubject")
    public ResponseEntity<?> salirDeMateria(@RequestBody RecordSalirDeMateria recordSalirDeMateria){
        Usuario usuario = usuarioRepository.findById(recordSalirDeMateria.idUsuario()).orElse(null);
        Materia materia = materiaRepository.findById(recordSalirDeMateria.idMateria()).orElse(null);
        if (materia == null) {
            return new ResponseEntity<>("materia no encontrada", HttpStatus.NOT_FOUND);
        }
        if (usuario == null) {
            return new ResponseEntity<>("usuario no encontrado", HttpStatus.NOT_FOUND);
        }
        UsuarioMateria usuarioMateriaFiltrado = usuario.getUsuarioMaterias().stream().filter(usuarioMateria -> usuarioMateria.getMateria().getNombre().equals(materia.getNombre())).findFirst().orElse(null);
//        UsuarioMateria usuarioMateria = usuarioMateriaRepository.findById(usuarioMateriaFiltrado.getId()).orElse(null);
        if (usuarioMateriaFiltrado == null) {
            return new ResponseEntity<>("usuario materia no encontrado", HttpStatus.NOT_FOUND);
        }
        else { usuarioMateriaFiltrado.setAsset(false); }
        usuarioMateriaRepository.save(usuarioMateriaFiltrado);


        usuarioRepository.save(usuario);
        return new ResponseEntity<>("Usuario: " + usuario.getName() + " " + usuario.getLastName() + " salio de la materia " + materia.getNombre(), HttpStatus.OK);
    }
}
