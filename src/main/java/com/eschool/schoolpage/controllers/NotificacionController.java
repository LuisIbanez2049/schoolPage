package com.eschool.schoolpage.controllers;

import com.eschool.schoolpage.dtos.MateriaDTO;
import com.eschool.schoolpage.dtos.NotificacionDTO;
import com.eschool.schoolpage.models.Notificacion;
import com.eschool.schoolpage.models.Usuario;
import com.eschool.schoolpage.repositories.NotificacionRepository;
import com.eschool.schoolpage.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/notificacion")
public class NotificacionController {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/")
    public ResponseEntity<?> getAllNotifications(Authentication authentication){
        try {
            Usuario usuario = usuarioRepository.findByMail(authentication.getName());
            if (usuario == null) {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
            List<NotificacionDTO> notificacionDTOS = notificacionRepository.findAll().stream().filter(notificacion -> notificacion.isAsset()).map(notificacion -> new NotificacionDTO(notificacion)).collect(Collectors.toList());
            if (notificacionDTOS == null) {
                return new ResponseEntity<>("No hay notificaciones", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(notificacionDTOS, HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @PatchMapping("/markAsViewed/{id}")
    public ResponseEntity<?> markNotificationAsViwed(Authentication authentication, @PathVariable Long id){
        try {
            Usuario usuario = usuarioRepository.findByMail(authentication.getName());
            if (usuario == null) {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
            Notificacion notificacion = notificacionRepository.findById(id).orElse(null);
            if (notificacion == null) {
                return new ResponseEntity<>("Notificacion no encontrada", HttpStatus.NOT_FOUND);
            }
            notificacion.setVisto(true);
            notificacionRepository.save(notificacion);
            return new ResponseEntity<>("Notification marked as viewed", HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @PatchMapping("/clean")
    public ResponseEntity<?> cleanNotificationsFrmUser(Authentication authentication){
        try {
            Usuario usuario = usuarioRepository.findByMail(authentication.getName());
            if (usuario == null) {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
            List<Notificacion> notificacionsFromUser = usuario.getNotificaciones().stream().filter(notificacion -> notificacion.isVisto()).collect(Collectors.toList());
            if (notificacionsFromUser == null) {
                return new ResponseEntity<>("There aren´t notifications to clean", HttpStatus.NO_CONTENT);
            }
            for (Notificacion notificacion : notificacionsFromUser) {
                notificacion.setAsset(false);
                notificacionRepository.save(notificacion);
            }
            return new ResponseEntity<>("Inbox was cleaned successfully", HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }
}
