package com.eschool.schoolpage.controllers;

import com.eschool.schoolpage.dtos.ContenidoAdminDTO;
import com.eschool.schoolpage.dtos.ContenidoDTO;
import com.eschool.schoolpage.dtos.RecordCrearContenido;
import com.eschool.schoolpage.dtos.RecordModificarContenido;
import com.eschool.schoolpage.models.Contenido;
import com.eschool.schoolpage.models.Materia;
import com.eschool.schoolpage.models.Rol;
import com.eschool.schoolpage.models.Usuario;
import com.eschool.schoolpage.repositories.ContenidoRepository;
import com.eschool.schoolpage.repositories.MateriaRepository;
import com.eschool.schoolpage.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contenido")
public class ContenidoController {
    @Autowired
    private ContenidoRepository contenidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    @GetMapping("/")
    public ResponseEntity<?> getAllContenidos(Authentication authentication){
        try {
            Usuario usuario = usuarioRepository.findByMail(authentication.getName());
            if (usuario.getRol() != Rol.ADMIN) {
                return new ResponseEntity<>("NO TIENES PERMISO PARA REALIZAR ESTA ACCION", HttpStatus.FORBIDDEN);
            }
            List<ContenidoDTO> contenidoDTOS = contenidoRepository.findAll().stream().filter(contenido -> contenido.isAsset())
                    .map(contenido -> new ContenidoDTO(contenido)).collect(Collectors.toList());
            return new ResponseEntity<>(contenidoDTOS, HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllContenidosAdmin(Authentication authentication){
        try {
            Usuario usuario = usuarioRepository.findByMail(authentication.getName());
            if (usuario.getRol() != Rol.ADMIN) {
                return new ResponseEntity<>("NO TIENES PERMISO PARA REALIZAR ESTA ACCION", HttpStatus.FORBIDDEN);
            }
            List<ContenidoDTO> contenidoDTOS = contenidoRepository.findAll().stream()
                    .map(contenido -> new ContenidoDTO(contenido)).collect(Collectors.toList());
            return new ResponseEntity<>(contenidoDTOS, HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getContenidoById(Authentication authentication, @PathVariable Long id){
        try {
            Contenido contenido = contenidoRepository.findById(id).orElse(null);
            if (contenido == null) {
                return new ResponseEntity<>("Contenido con id: " + id + " no encontrado", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new ContenidoDTO(contenido), HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<?> getContenidoByIdAdmin(Authentication authentication, @PathVariable Long id){
        try {
            Contenido contenido = contenidoRepository.findById(id).orElse(null);
            if (contenido == null) {
                return new ResponseEntity<>("Contenido con id: " + id + " no encontrado", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new ContenidoAdminDTO(contenido), HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @PostMapping("/create")
    public ResponseEntity<?> crearContenido(Authentication authentication,@RequestBody RecordCrearContenido recordCrearContenido){
        try {
            Usuario usuario = usuarioRepository.findByMail(authentication.getName());
            Materia materia = materiaRepository.findById(recordCrearContenido.idMateria()).orElse(null);
            if (materia == null) {
                return new ResponseEntity<>("Materia no encontrada", HttpStatus.NOT_FOUND);
            }
            if (usuario == null) {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
            if (usuario.getRol().equals(Rol.ESTUDIANTE)) {
                return new ResponseEntity<>("NO TIENES PERMISO PARA REALIZAR ESTA ACCION", HttpStatus.FORBIDDEN);
            }
            if (recordCrearContenido.titulo().isEmpty()) {
                return new ResponseEntity<>("El titulo debe ser especificado.", HttpStatus.BAD_REQUEST);
            }
            if (recordCrearContenido.detalleContenido().isEmpty()) {
                return new ResponseEntity<>("El contenido debe ser especificado.", HttpStatus.BAD_REQUEST);
            }
            if (recordCrearContenido.archivo().isEmpty()) {
                return new ResponseEntity<>("El archivo debe ser especificado.", HttpStatus.BAD_REQUEST);
            }
            Contenido newContenido = new Contenido(recordCrearContenido.titulo(), LocalDateTime.now(), recordCrearContenido.detalleContenido(), recordCrearContenido.archivo());
            materia.addContenido(newContenido);
            contenidoRepository.save(newContenido);



            return new ResponseEntity<>("CONTENIDO CREADO CON EXITO", HttpStatus.OK);

        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @PatchMapping("/modificar")
    public ResponseEntity<?> modificarContenido(Authentication authentication,@RequestBody RecordModificarContenido recordModificarContenido){
        try {
            Usuario usuario = usuarioRepository.findByMail(authentication.getName());
            Contenido contenido = contenidoRepository.findById(recordModificarContenido.idContenido()).orElse(null);
            if (usuario == null) {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
            if (usuario.getRol().equals(Rol.ESTUDIANTE)) {
                return new ResponseEntity<>("NO TIENES PERMISO PARA REALIZAR ESTA ACCION", HttpStatus.FORBIDDEN);
            }
            if (!recordModificarContenido.titulo().isEmpty()) {
                contenido.setTitulo(recordModificarContenido.titulo());
                contenidoRepository.save(contenido);
                return new ResponseEntity<>("Titulo modificado con exito", HttpStatus.OK);
            }
            if (!recordModificarContenido.detalleContenido().isEmpty()) {
                contenido.setDetalleDelContenido(recordModificarContenido.detalleContenido());
                contenidoRepository.save(contenido);
                return new ResponseEntity<>("Detalle de contenido modificado con exito", HttpStatus.OK);
            }
            if (!recordModificarContenido.archivo().isEmpty()) {
                contenido.setArchivo(recordModificarContenido.archivo());
                contenidoRepository.save(contenido);
                return new ResponseEntity<>("Archivo modificado con exito", HttpStatus.OK);
            }
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @DeleteMapping("/desactivar/{id}")
    public ResponseEntity<?> desactivarContenido(Authentication authentication, @PathVariable Long id){
        try {
            Usuario usuario = usuarioRepository.findByMail(authentication.getName());
            Contenido contenido = contenidoRepository.findById(id).orElse(null);
            if (usuario == null) {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
            if (contenido == null) {
                return new ResponseEntity<>("Contenido no encontrado", HttpStatus.NOT_FOUND);
            }
            if (usuario.getRol().equals(Rol.ESTUDIANTE)) {
                return new ResponseEntity<>("NO TIENES PERMISO PARA REALIZAR ESTA ACCION", HttpStatus.FORBIDDEN);
            }
            if (!contenido.isAsset()) {
                return new ResponseEntity<>("El contenido " + contenido.getTitulo() + ", actualmente esta desactivado", HttpStatus.BAD_REQUEST);
            }
            contenido.setAsset(false);
            contenidoRepository.save(contenido);
            return new ResponseEntity<>("Contenido desactivado exitosamente", HttpStatus.OK);
        }  catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @PatchMapping("/activar/{id}")
    public ResponseEntity<?> activarContenido(Authentication authentication, @PathVariable Long id){
        try {
            Usuario usuario = usuarioRepository.findByMail(authentication.getName());
            Contenido contenido = contenidoRepository.findById(id).orElse(null);
            if (usuario == null) {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
            if (contenido == null) {
                return new ResponseEntity<>("Contenido no encontrado", HttpStatus.NOT_FOUND);
            }
            if (usuario.getRol().equals(Rol.ESTUDIANTE)) {
                return new ResponseEntity<>("NO TIENES PERMISO PARA REALIZAR ESTA ACCION", HttpStatus.FORBIDDEN);
            }
            if (contenido.isAsset()) {
                return new ResponseEntity<>("El contenido " + contenido.getTitulo() + ", actualmente esta activado", HttpStatus.BAD_REQUEST);
            }
            contenido.setAsset(true);
            contenidoRepository.save(contenido);
            return new ResponseEntity<>("Contenido activado exitosamente", HttpStatus.OK);
        }  catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }
}
