package com.eschool.schoolpage.controllers;

import com.eschool.schoolpage.dtos.ArchivoDTO;
import com.eschool.schoolpage.dtos.RecordFile;
import com.eschool.schoolpage.models.Archivo;
import com.eschool.schoolpage.models.Respuesta;
import com.eschool.schoolpage.models.Usuario;
import com.eschool.schoolpage.repositories.ArchivoRepository;
import com.eschool.schoolpage.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/archivo")

public class ArchivoController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ArchivoRepository archivoRepository;

    @GetMapping("/")
    public ResponseEntity<?> getAllFiles(Authentication authentication){
        try {
            Usuario usuario = usuarioRepository.findByMail(authentication.getName());
            if (usuario == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
            Set<ArchivoDTO> archivoDTOS = archivoRepository.findAll().stream().map(archivo -> new ArchivoDTO(archivo)).collect(Collectors.toSet());
            return new ResponseEntity<>(archivoDTOS, HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFilById(Authentication authentication, @PathVariable Long id){
        try {
            Usuario usuario = usuarioRepository.findByMail(authentication.getName());
            if (usuario == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            Archivo archivo = archivoRepository.findById(id).orElse(null);
            if (archivo == null) {
                return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(new ArchivoDTO(archivo), HttpStatus.OK);

        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @PatchMapping("/editFile")
    public ResponseEntity<?> editFile(Authentication authentication, @RequestBody RecordFile recordFile)
    {
        try {
            Usuario usuario = usuarioRepository.findByMail(authentication.getName());
            if (usuario == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            Archivo archivo = archivoRepository.findById(recordFile.id()).orElse(null);
            if (archivo == null) {
                return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
            }

            if (recordFile.name().isEmpty() || recordFile.name().isEmpty()) {
                return new ResponseEntity<>("Name can not be empty.", HttpStatus.BAD_REQUEST);
            }
            if (recordFile.tipoArchivo().isEmpty() || recordFile.tipoArchivo().isEmpty()) {
                return new ResponseEntity<>("Type file can not be empty.", HttpStatus.BAD_REQUEST);
            }
            if (recordFile.link().isEmpty() || recordFile.link().isEmpty()) {
                return new ResponseEntity<>("Link can not be empty.", HttpStatus.BAD_REQUEST);
            }

            archivo.setName(recordFile.name());
            archivo.setTipoArchivo(recordFile.tipoArchivo());
            archivo.setLink(recordFile.link());
            archivoRepository.save(archivo);
            return new ResponseEntity<>("File modify successfully.", HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }
}
