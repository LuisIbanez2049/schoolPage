package com.eschool.schoolpage.configurations;

import com.eschool.schoolpage.filters.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class WebConfiguration {

    @Autowired
    private JwtRequestFilter jwtRequestFilter; // Filtro personalizado para manejar la autenticación JWT.

    @Autowired
    private CorsConfigurationSource corsConfigurationSource; // Fuente de configuración para CORS (Cross-Origin Resource Sharing).

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                // Configuración de CORS utilizando la fuente de configuración proporcionada.
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                // Desactiva la protección CSRF (Cross-Site Request Forgery).
                .csrf(AbstractHttpConfigurer::disable)
                // Desactiva la autenticación básica HTTP.
                .httpBasic(AbstractHttpConfigurer::disable)
                // Desactiva el formulario de inicio de sesión.
                .formLogin(AbstractHttpConfigurer::disable)

                // Configura los encabezados de seguridad, desactivando la protección contra marcos (frame options).
                .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::disable))

                // Configura las reglas de autorización para las solicitudes HTTP.
                .authorizeHttpRequests(authorize ->
                                authorize
                                        .requestMatchers("/api/materias/", "/api/materias/*","/api/materias/mysubjects", "/api/materias/availablesubjects", "/api/usuarios/leaveSubject",
                                                "/api/usuarios/configuration", "/api/auth/current", "/api/contenido/*", "/api/comentario/*", "/api/respuesta/fromAcomment/*").hasAnyRole("ALUMNO","PROFESOR", "ADMIN")
                                        .requestMatchers("/api/usuarios/loginMateria", "/api/respuesta/create", "/api/respuesta/modificar",
                                                "/api/auth/current", "/api/comentario/create", "/api/comentario/authenticatedUserDesactivar/**",
                                                "/api/respuesta/authenticatedUserDesactivar/**").hasAnyRole("ALUMNO", "PROFESOR")
                                        .requestMatchers("/api/materias/modificarMateria", "/api/contenido/create", "/api/contenido/modificar", "/api/contenido/desactivar/**", "/api/respuesta/authenticatedUserDesactivar/**",
                                                "/api/contenido/activar/**", "/api/comentario/authenticatedUserDesactivar/**").hasAnyRole("PROFESOR", "ADMIN")

                                        //.requestMatchers("/h2-console/**").permitAll()
                                        // Permite el acceso sin autenticación a las rutas especificadas (login, registro, y consola H2).
                                        .requestMatchers( "/api/usuarios/", "/api/respuesta/", "/api/materias/create","/api/materias/all", "/api/materias/modificarMateriaAdmin", "/api/materias/admin/**", "/api/contenido/", "/api/contenido/all",
                                                "/api/contenido/admin/**","/api/comentario/", "/api/usuarios/configurationAdmin", "/api/comentario/admin/**", "/api/respuesta/**", "/api/respuesta/admin/**", "/api/usuarios/**",
                                                "/api/respuesta/adminDesactivar/**", "/api/respuesta/adminActivar/**", "/h2-console/**", "/api/materias/activar/**", "/api/contenido/", "/api/materias/desactivar/**",
                                                "/api/comentario/", "/api/comentario/adminDesactivar/**", "/api/comentario/adminActivar/**").hasRole("ADMIN")
                                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                                        // Permite el acceso sin autenticación a cualquier otra solicitud (esto puede ser modificado según los requisitos).
                                        .anyRequest().authenticated()
                                        //.anyRequest().permitAll()
                )

                // Agrega el filtro JWT antes del filtro de autenticación por nombre de usuario y contraseña.
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                // Configura la política de creación de sesiones como sin estado (stateless), sin crear sesiones en el servidor.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Construye y retorna la configuración de seguridad.
        return httpSecurity.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
