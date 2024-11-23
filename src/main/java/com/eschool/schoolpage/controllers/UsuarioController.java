package com.eschool.schoolpage.controllers;

import com.eschool.schoolpage.dtos.UsuarioDTO;
import com.eschool.schoolpage.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/")
    public ResponseEntity<?> getallUsuariosActivos(){
        List<UsuarioDTO> usuarioDTOS = usuarioRepository.findAll().stream().filter(usuario -> usuario.isAsset())
                .map(cadaUsuario -> new UsuarioDTO(cadaUsuario)).collect(Collectors.toList());
        return new ResponseEntity<>(usuarioDTOS, HttpStatus.OK);
    }
}
