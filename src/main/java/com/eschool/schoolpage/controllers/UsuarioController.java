package com.eschool.schoolpage.controllers;

import com.eschool.schoolpage.dtos.*;
import com.eschool.schoolpage.models.JornadaTurno;
import com.eschool.schoolpage.models.Materia;
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

    @GetMapping("/admin/{id}")
    public ResponseEntity<?> findUsuarioAdmin(Authentication authentication, @PathVariable Long id){
        try {
            Usuario usuario = usuarioRepository.findById(id).orElse(null);
            if (usuario == null) {
                return new ResponseEntity<>("Usuario no encontrado.", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new UsuarioAdminDTO(usuario), HttpStatus.OK);
        }  catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }

    @PatchMapping("/configuration")
    public ResponseEntity<?> editUserInformation(Authentication authentication,@RequestBody RecordEditUserInformation recordEditUserInformation){
        try {
            Usuario usuario = usuarioRepository.findByMail(authentication.getName());
            if (usuario == null) {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }

            if (!recordEditUserInformation.name().isEmpty()) {
                if (recordEditUserInformation.name().length() < 2) { return new ResponseEntity<>("Invalid first name. Please provide at least 2 characters.", HttpStatus.BAD_REQUEST);}
                usuario.setName(recordEditUserInformation.name());
                usuarioRepository.save(usuario);
                return new ResponseEntity<>("Name was updated successfully", HttpStatus.OK);
            }

            if (!recordEditUserInformation.lastName().isEmpty()) {
                if (recordEditUserInformation.lastName().length() < 2) { return new ResponseEntity<>("Invalid last name. Please provide at least 2 characters.", HttpStatus.BAD_REQUEST);}
                usuario.setLastName(recordEditUserInformation.lastName());
                usuarioRepository.save(usuario);
                return new ResponseEntity<>("Last Name was updated successfully", HttpStatus.OK);
            }

            if (!recordEditUserInformation.dni().isEmpty()) {
                if (recordEditUserInformation.dni().length() < 2) { return new ResponseEntity<>("Invalid DNI. Please provide at least 2 characters.", HttpStatus.BAD_REQUEST);}
                usuario.setDNI(recordEditUserInformation.dni());
                usuarioRepository.save(usuario);
                return new ResponseEntity<>("DNI was updated successfully", HttpStatus.OK);
            }

            if (!recordEditUserInformation.email().isEmpty()) {
                if (!recordEditUserInformation.email().contains("@")) { return new ResponseEntity<>("Invalid email. It must contain an '@' character.", HttpStatus.BAD_REQUEST); }
                if (!recordEditUserInformation.email().contains(".com") && !recordEditUserInformation.email().contains(".net") && !recordEditUserInformation.email().contains(".org") &&
                        !recordEditUserInformation.email().contains(".co") && !recordEditUserInformation.email().contains(".info")) {
                    return new ResponseEntity<>("Invalid email. Please enter a valid domain extension since '.com', '.net', '.org', '.co' or '.info'.", HttpStatus.BAD_REQUEST); }
                if (recordEditUserInformation.email().contains("@.")) { return new ResponseEntity<>("Invalid email. Please provide a valid domain since 'gmail', 'yahoo', etc., " +
                        "between the characters '@' and the character '.'", HttpStatus.BAD_REQUEST); }
                if (usuarioRepository.findByMail(recordEditUserInformation.email()) != null) {
                    return new ResponseEntity<>("Email not available. Already in use.", HttpStatus.BAD_REQUEST);
                }
                usuario.setMail(recordEditUserInformation.email());
                usuarioRepository.save(usuario);
                return new ResponseEntity<>("Email was updated successfully", HttpStatus.OK);
            }

            if (!recordEditUserInformation.profileImg().isEmpty()) {
                usuario.setProfileUserImage(recordEditUserInformation.profileImg());
                usuarioRepository.save(usuario);
                return new ResponseEntity<>("Profile image was updated successfully", HttpStatus.OK);
            }

            return new ResponseEntity<>("Any parameter was not changed", HttpStatus.BAD_REQUEST);

        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }


    @PatchMapping("/configurationAdmin")
    public ResponseEntity<?> editUserInformationAdmin(Authentication authentication,@RequestBody RecordUserInformationAdmin recordUserInformationAdmin){
        try {
            Usuario usuario = usuarioRepository.findById(recordUserInformationAdmin.id()).orElse(null);
            if (usuario == null) {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }

            if (!recordUserInformationAdmin.name().isEmpty()) {
                if (recordUserInformationAdmin.name().length() < 2) { return new ResponseEntity<>("Invalid first name. Please provide at least 2 characters.", HttpStatus.BAD_REQUEST);}
                usuario.setName(recordUserInformationAdmin.name());
                usuarioRepository.save(usuario);
                return new ResponseEntity<>("Name was updated successfully", HttpStatus.OK);
            }

            if (!recordUserInformationAdmin.lastName().isEmpty()) {
                if (recordUserInformationAdmin.lastName().length() < 2) { return new ResponseEntity<>("Invalid last name. Please provide at least 2 characters.", HttpStatus.BAD_REQUEST);}
                usuario.setLastName(recordUserInformationAdmin.lastName());
                usuarioRepository.save(usuario);
                return new ResponseEntity<>("Last Name was updated successfully", HttpStatus.OK);
            }

            if (!recordUserInformationAdmin.dni().isEmpty()) {
                if (recordUserInformationAdmin.dni().length() < 2) { return new ResponseEntity<>("Invalid DNI. Please provide at least 2 characters.", HttpStatus.BAD_REQUEST);}
                usuario.setDNI(recordUserInformationAdmin.dni());
                usuarioRepository.save(usuario);
                return new ResponseEntity<>("DNI was updated successfully", HttpStatus.OK);
            }

            if (!recordUserInformationAdmin.email().isEmpty()) {
                if (!recordUserInformationAdmin.email().contains("@")) { return new ResponseEntity<>("Invalid email. It must contain an '@' character.", HttpStatus.BAD_REQUEST); }
                if (!recordUserInformationAdmin.email().contains(".com") && !recordUserInformationAdmin.email().contains(".net") && !recordUserInformationAdmin.email().contains(".org") &&
                        !recordUserInformationAdmin.email().contains(".co") && !recordUserInformationAdmin.email().contains(".info")) {
                    return new ResponseEntity<>("Invalid email. Please enter a valid domain extension since '.com', '.net', '.org', '.co' or '.info'.", HttpStatus.BAD_REQUEST); }
                if (recordUserInformationAdmin.email().contains("@.")) { return new ResponseEntity<>("Invalid email. Please provide a valid domain since 'gmail', 'yahoo', etc., " +
                        "between the characters '@' and the character '.'", HttpStatus.BAD_REQUEST); }
                if (usuarioRepository.findByMail(recordUserInformationAdmin.email()) != null) {
                    return new ResponseEntity<>("Email not available. Already in use.", HttpStatus.BAD_REQUEST);
                }
                usuario.setMail(recordUserInformationAdmin.email());
                usuarioRepository.save(usuario);
                return new ResponseEntity<>("Email was updated successfully", HttpStatus.OK);
            }

            if (!recordUserInformationAdmin.profileImg().isEmpty()) {
                usuario.setProfileUserImage(recordUserInformationAdmin.profileImg());
                usuarioRepository.save(usuario);
                return new ResponseEntity<>("Profile image was updated successfully", HttpStatus.OK);
            }

            return new ResponseEntity<>("Any parameter was not changed", HttpStatus.BAD_REQUEST);

        } catch (Exception e) { return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
    }


    @PatchMapping("/leaveSubject")
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
            if (!materia.getAccessCode().equals(recordLoginMateria.accessCode())) {
                return new ResponseEntity<>("Invalid access code.", HttpStatus.NOT_FOUND);
            }
            UsuarioMateria usuarioMateriaa = materia.getUsuarioMaterias().stream().filter(usuarioMateria -> usuarioMateria.getUsuario().getId().equals(usuario.getId())).findFirst().orElse(null);
            JornadaTurno jornadaTurno = JornadaTurno.MORNING;
            if (recordLoginMateria.turno().equalsIgnoreCase("EVENING")) {
                jornadaTurno = JornadaTurno.EVENING;
            }
            if (recordLoginMateria.turno().equalsIgnoreCase("NIGHT")) {
                jornadaTurno = JornadaTurno.NIGHT;
            }
            if (usuarioMateriaa != null && usuarioMateriaa.isAsset()) {
                return new ResponseEntity<>("YA ESTAS DENTRO DE LA MATERIA " + materia.getNombre(), HttpStatus.OK);
            }
            if (recordLoginMateria.turno().isEmpty() || recordLoginMateria.turno().isBlank()) {
                return new ResponseEntity<>("Please specify a shift.", HttpStatus.NOT_FOUND); 
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
