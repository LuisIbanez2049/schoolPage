package com.eschool.schoolpage.controllers;

import com.eschool.schoolpage.dtos.*;
import com.eschool.schoolpage.models.Comentario;
import com.eschool.schoolpage.models.Contenido;
import com.eschool.schoolpage.models.Usuario;
import com.eschool.schoolpage.repositories.ComentarioRepository;
import com.eschool.schoolpage.repositories.ContenidoRepository;
import com.eschool.schoolpage.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.encrypt.RsaKeyHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comentario")
public class ComentarioController {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContenidoRepository contenidoRepository;

    @GetMapping("/")
    public ResponseEntity<?> getAllComentarios(Authentication authentication){
        try {
            List<ComentarioDTO> comentarioDTOS = comentarioRepository.findAll().stream().filter(comentario -> comentario.isAsset())
                    .map(comentario -> new ComentarioDTO(comentario)).collect(Collectors.toList());
            return new ResponseEntity<>(comentarioDTOS, HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getComentarioById(Authentication authentication, @PathVariable Long id){
        try {
            Comentario comentario = comentarioRepository.findById(id).orElse(null);
            if (comentario == null) {
                return new ResponseEntity<>("Comentario con id: " + id + " no encontrado", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new ComentarioDTO(comentario), HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<?> getComentarioByIdAdmin(Authentication authentication, @PathVariable Long id){
        try {
            Comentario comentario = comentarioRepository.findById(id).orElse(null);
            if (comentario == null) {
                return new ResponseEntity<>("Comentario con id: " + id + " no encontrado", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new ComentarioAdminDTO(comentario), HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @PostMapping("/create")
    public ResponseEntity<?> crearComentario(Authentication authentication, @RequestBody RecordComentario recordComentario){
        try {
            Usuario usuario = usuarioRepository.findByMail(authentication.getName());
            Contenido contenido = contenidoRepository.findById(recordComentario.idContenido()).orElse(null);
            if (usuario == null) {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
            if (contenido == null) {
                return new ResponseEntity<>("Contenido no encontrado", HttpStatus.NOT_FOUND);
            }
            if (recordComentario.texto().isBlank()) {
                return new ResponseEntity<>("Comment can not be empty.", HttpStatus.NOT_FOUND);
            }

            Comentario newComentario = new Comentario(recordComentario.texto(), LocalDateTime.now());
            contenido.addComentario(newComentario);
            newComentario.setContenido(contenido);
            usuario.addComentario(newComentario);
            newComentario.setUsuario(usuario);
            comentarioRepository.save(newComentario);
            return new ResponseEntity<>("Comentario agregado", HttpStatus.OK);
        }  catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @PatchMapping("/modificar")
    public ResponseEntity<?> modificarComentario(Authentication authentication, @RequestBody RecordModificarComentario recordModificarComentario){
        try {
            Usuario usuario = usuarioRepository.findByMail(authentication.getName());
            Comentario comentario = comentarioRepository.findById(recordModificarComentario.idComentario()).orElse(null);
            if (usuario == null) {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
            if (comentario == null) {
                return new ResponseEntity<>("Comentario no encontrado", HttpStatus.NOT_FOUND);
            }
            Comentario comentarioUsuario = usuario.getComentarios().stream().filter(comentario1 -> comentario1.getId().equals(comentario.getId()))
                    .findFirst().orElse(null);
            if (comentarioUsuario == null) {
                return new ResponseEntity<>("Este comentario no te pertenece", HttpStatus.FORBIDDEN);
            }
            if (recordModificarComentario.texto().isBlank()) {
                return new ResponseEntity<>("COMMENT CAN NOT BE EMPTY", HttpStatus.NOT_FOUND);
            }

            comentario.setTexto(recordModificarComentario.texto());
            comentarioRepository.save(comentario);
            return new ResponseEntity<>("Comentario modificado con exito.", HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @DeleteMapping("/authenticatedUserDesactivar/{id}")
    public ResponseEntity<?> desactivarComentarioAuthenticatedUser(Authentication authentication, @PathVariable Long id){
        try {
            Usuario usuario = usuarioRepository.findByMail(authentication.getName());
            Comentario comentario = comentarioRepository.findById(id).orElse(null);
            if (usuario == null) {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
            if (comentario == null) {
                return new ResponseEntity<>("Comentario no encontrado", HttpStatus.NOT_FOUND);
            }
            Comentario comentarioUsuario = usuario.getComentarios().stream().filter(comentario1 -> comentario1.getId().equals(comentario.getId()))
                    .findFirst().orElse(null);
            if (comentarioUsuario == null) {
                return new ResponseEntity<>("Este comentario no te pertenece.", HttpStatus.FORBIDDEN);
            }
            comentario.setAsset(false);
            comentarioRepository.save(comentario);
            return new ResponseEntity<>("Comentario eliminado", HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @DeleteMapping("/adminDesactivar/{id}")
    public ResponseEntity<?> desactivarComentarioAdmin(Authentication authentication, @PathVariable Long id){
        try {
            Usuario usuario = usuarioRepository.findByMail(authentication.getName());
            Comentario comentario = comentarioRepository.findById(id).orElse(null);
            if (usuario == null) {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
            if (comentario == null) {
                return new ResponseEntity<>("Comentario no encontrado", HttpStatus.NOT_FOUND);
            }
            comentario.setAsset(false);
            comentarioRepository.save(comentario);
            return new ResponseEntity<>("Comentario eliminado", HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @PatchMapping("/adminActivar/{id}")
    public ResponseEntity<?> activarComentarioAdmin(Authentication authentication, @PathVariable Long id){
        try {
            Usuario usuario = usuarioRepository.findByMail(authentication.getName());
            Comentario comentario = comentarioRepository.findById(id).orElse(null);
            if (usuario == null) {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
            if (comentario == null) {
                return new ResponseEntity<>("Comentario no encontrado", HttpStatus.NOT_FOUND);
            }
            comentario.setAsset(true);
            comentarioRepository.save(comentario);
            return new ResponseEntity<>("Comentario reactivado", HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }
}
