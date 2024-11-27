package com.eschool.schoolpage.servicesSecurity;

import com.eschool.schoolpage.models.Usuario;
import com.eschool.schoolpage.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByMail(username);

        // Si el cliente es nulo, no se encuentra arrojamos la exepcion "UsernameNotFoundException(username)"
        if (usuario == null) {
            throw new UsernameNotFoundException(username);
        }

        if (usuario.getMail().contains("@admin")){
            return User
                    .withUsername(username) // Va a tener el email
                    .password(usuario.getPassword()) // Obtengo la contraseña del cliente
                    .roles("ADMIN") // Le doy el rol de client
                    .build(); // Mando a contruir a este usuario
        }
        if (usuario.getMail().contains("@profesor")){
            return User
                    .withUsername(username) // Va a tener el email
                    .password(usuario.getPassword()) // Obtengo la contraseña del cliente
                    .roles("PROFESOR") // Le doy el rol de client
                    .build(); // Mando a contruir a este usuario
        }


        // Si se encuentra al usuario retornamos al usuario que vamos a guardar en el context holder
        return User
                .withUsername(username) // Va a tener el email
                .password(usuario.getPassword()) // Obtengo la contraseña del cliente
                .roles("ALUMNO") // Le doy el rol de client
                .build(); // Mando a contruir a este usuario

        // Esta instancia de User que creo lo pongo en el contexto para despues mandarle a crear un token
    }
}
