package com.eschool.schoolpage.controllers;

import com.eschool.schoolpage.dtos.*;
import com.eschool.schoolpage.models.Comentario;
import com.eschool.schoolpage.models.Contenido;
import com.eschool.schoolpage.models.Respuesta;
import com.eschool.schoolpage.models.Usuario;
import com.eschool.schoolpage.repositories.ComentarioRepository;
import com.eschool.schoolpage.repositories.RespuestaRepository;
import com.eschool.schoolpage.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/respuesta")
public class RespuestaController {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RespuestaRepository respuestaRepository;

    @GetMapping("/")
    public ResponseEntity<?> getAllRespuestas(Authentication authentication){
        try {
            List<RespuestaDTO> respuestaDTOS = respuestaRepository.findAll().stream().filter(respuesta -> respuesta.isAsset())
                    .map(respuesta -> new RespuestaDTO(respuesta)).collect(Collectors.toList());
            return new ResponseEntity<>(respuestaDTOS, HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @PostMapping("/create")
    public ResponseEntity<?> crearRespuesta(Authentication authentication, @RequestBody RecordRespuesta recordRespuesta){
        try {
            Usuario usuario = usuarioRepository.findByMail(authentication.getName());
            Comentario comentario = comentarioRepository.findById(recordRespuesta.idComentario()).orElse(null);
            if (usuario == null) {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
            if (comentario == null) {
                return new ResponseEntity<>("Comentario no encontrado", HttpStatus.NOT_FOUND);
            }
            if (recordRespuesta.texto().isBlank()) {
                return new ResponseEntity<>("Campo vacio.", HttpStatus.NOT_FOUND);
            }

            Respuesta newRespuesta = new Respuesta(recordRespuesta.texto(), LocalDateTime.now());
            comentario.addRespuesta(newRespuesta);
            newRespuesta.setComentario(comentario);
            usuario.addRespuesta(newRespuesta);
            newRespuesta.setUsuario(usuario);
            respuestaRepository.save(newRespuesta);
            return new ResponseEntity<>("Respuesta agregada", HttpStatus.OK);
        }  catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @PatchMapping("/modificar")
    public ResponseEntity<?> modificarRespuesta(Authentication authentication, @RequestBody RecordModificarRespuesta recordModificarRespuesta){
        try {
            Usuario usuario = usuarioRepository.findByMail(authentication.getName());
            Respuesta respuesta = respuestaRepository.findById(recordModificarRespuesta.idRespuesta()).orElse(null);
            if (usuario == null) {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
            if (respuesta == null) {
                return new ResponseEntity<>("Respuesta no encontrada", HttpStatus.NOT_FOUND);
            }
            Respuesta respuestaUsuario = usuario.getRespuestas().stream().filter(respuesta1 -> respuesta1.getId().equals(respuesta.getId()))
                    .findFirst().orElse(null);
            if (respuestaUsuario == null) {
                return new ResponseEntity<>("Esta respuesta no te pretenece", HttpStatus.FORBIDDEN);
            }
            if (recordModificarRespuesta.texto().isBlank()) {
                return new ResponseEntity<>("Campo vacio.", HttpStatus.NOT_FOUND);
            }

            respuesta.setTexto(recordModificarRespuesta.texto());
            respuestaRepository.save(respuesta);
            return new ResponseEntity<>("Respuesta modificado con exito.", HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @DeleteMapping("/authenticatedUserDesactivar/{id}")
    public ResponseEntity<?> desactivarRespuestaAuthenticatedUser(Authentication authentication, @PathVariable Long id){
        try {
            Usuario usuario = usuarioRepository.findByMail(authentication.getName());
            Respuesta respuesta = respuestaRepository.findById(id).orElse(null);
            if (usuario == null) {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
            if (respuesta == null) {
                return new ResponseEntity<>("Respuesta no encontrada", HttpStatus.NOT_FOUND);
            }
            Respuesta respuestaUsuario = usuario.getRespuestas().stream().filter(respuesta1 -> respuesta1.getId().equals(respuesta.getId()))
                    .findFirst().orElse(null);
            if (respuestaUsuario == null) {
                return new ResponseEntity<>("Esta respuesta no te pretenece.", HttpStatus.FORBIDDEN);
            }
            respuesta.setAsset(false);
            respuestaRepository.save(respuesta);
            return new ResponseEntity<>("Respuesta eliminada", HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @DeleteMapping("/adminDesactivar/{id}")
    public ResponseEntity<?> desactivarRespuestaAdmin(Authentication authentication, @PathVariable Long id){
        try {
            Usuario usuario = usuarioRepository.findByMail(authentication.getName());
            Respuesta respuesta = respuestaRepository.findById(id).orElse(null);
            if (usuario == null) {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
            if (respuesta == null) {
                return new ResponseEntity<>("Respuesta no encontrada", HttpStatus.NOT_FOUND);
            }
            respuesta.setAsset(false);
            respuestaRepository.save(respuesta);
            return new ResponseEntity<>("Respuesta eliminada", HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }
}
