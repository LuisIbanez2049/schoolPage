package com.eschool.schoolpage.controllers;

import com.eschool.schoolpage.dtos.RecordLogin;
import com.eschool.schoolpage.dtos.RecordRegister;
import com.eschool.schoolpage.models.Rol;
import com.eschool.schoolpage.models.Usuario;
import com.eschool.schoolpage.repositories.UsuarioRepository;
import com.eschool.schoolpage.servicesSecurity.JwtUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.encrypt.RsaAlgorithm;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody RecordLogin recordLogin){
        try {
            System.out.println("Login attempt for: " + recordLogin.email());

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(recordLogin.email(), recordLogin.password()));

            final UserDetails userDetails = userDetailsService.loadUserByUsername(recordLogin.email());

            final String jwt = jwtUtilService.generateToken(userDetails);

            System.out.println("JWT GENERATED: " + jwt);

            return ResponseEntity.ok(jwt);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Email or password invalid.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RecordRegister recordRegister){
        if (usuarioRepository.findByMail(recordRegister.email()) != null) {
            return new ResponseEntity<>("Email not available. Already in use.", HttpStatus.BAD_REQUEST);
        }
        if (recordRegister.name().length() < 2) { return new ResponseEntity<>("Invalid first name. Please provide at least 2 characters.", HttpStatus.BAD_REQUEST);}

        if (recordRegister.name().isBlank()) {
            return new ResponseEntity<>("First name can not be empty.", HttpStatus.BAD_REQUEST);
        }
        if (recordRegister.lastName().isBlank()) {
            return new ResponseEntity<>("Last name can not be empty.", HttpStatus.BAD_REQUEST);
        }
        if (recordRegister.lastName().length() < 2) { return new ResponseEntity<>("Invalid last name. Please provide at least 2 characters.", HttpStatus.BAD_REQUEST);}

        if (recordRegister.email().isBlank()) {
            return new ResponseEntity<>("Email can not be empty.", HttpStatus.BAD_REQUEST);
        }
        if (!recordRegister.email().contains("@")) { return new ResponseEntity<>("Invalid email. It must contain an '@' character.", HttpStatus.BAD_REQUEST); }
        if (!recordRegister.email().contains(".com") && !recordRegister.email().contains(".net") && !recordRegister.email().contains(".org") &&
                !recordRegister.email().contains(".co") && !recordRegister.email().contains(".info")) {
            return new ResponseEntity<>("Invalid email. Please enter a valid domain extension since '.com', '.net', '.org', '.co' or '.info'.", HttpStatus.BAD_REQUEST); }
        if (recordRegister.email().contains("@.")) { return new ResponseEntity<>("Invalid email. Please provide a valid domain since 'gmail', 'yahoo', etc., " +
                "between the characters '@' and the character '.'", HttpStatus.BAD_REQUEST); }
        if (recordRegister.dni().isBlank()) {
            return new ResponseEntity<>("DNI can not be empty.", HttpStatus.BAD_REQUEST);
        }
        if (recordRegister.password().isBlank()) {
            return new ResponseEntity<>("Password can not be empty.", HttpStatus.BAD_REQUEST);
        }
        if (recordRegister.password().length() < 8) {
            return new ResponseEntity<>("Password must be at least 8 characters long.", HttpStatus.BAD_REQUEST);
        }

        Rol rol = Rol.ESTUDIANTE;
        if (recordRegister.email().contains("@profesor")) {
            rol = Rol.PROFESOR;
        }
        if (recordRegister.email().contains("@admin")) {
            rol = Rol.ADMIN;
        }
        String encodePassword = passwordEncoder.encode(recordRegister.password());
        Usuario newUsuario = new Usuario(recordRegister.name(), recordRegister.lastName(), recordRegister.dni(), recordRegister.email(), encodePassword, rol);
        usuarioRepository.save(newUsuario);
        return new ResponseEntity<>("CLIENT REGISTERED SUCCESSFULLY", HttpStatus.OK);

    }
}
