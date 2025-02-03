package com.eschool.schoolpage.dtos;

import com.eschool.schoolpage.models.Rol;
import com.eschool.schoolpage.models.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioAdminDTO {
    private Long id;
    private String name;
    private String lastName;
    private String mail;
    private String DNI;
    private boolean asset = true; // "Activo"
    private Rol rol;
    private String userProfileImg;
    private boolean estaEnUnaMateria = false;
    private List<UsuarioMateriaDTO> usuarioMaterias = new ArrayList<>();
    private List<ComentarioAdminDTO> comentarioAdminDTOS = new ArrayList<>();
    private List<RespuestaDTO> respuestaDTOS = new ArrayList<>();

    public UsuarioAdminDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.name = usuario.getName();
        this.lastName = usuario.getLastName();
        this.mail = usuario.getMail();
        this.DNI= usuario.getDNI();
        this.asset = usuario.isAsset();
        this.rol = usuario.getRol();
        this.userProfileImg = usuario.getProfileUserImage();
        this.estaEnUnaMateria = usuario.isEstaEnUnaMateria();
        this.usuarioMaterias = usuario.getUsuarioMaterias().stream().filter(usuarioMateria -> usuarioMateria.isAsset())
                .map(usuarioMateria -> new UsuarioMateriaDTO(usuarioMateria)).collect(Collectors.toList());
        this.comentarioAdminDTOS = usuario.getComentarios().stream()
                .map(comentario -> new ComentarioAdminDTO(comentario)).collect(Collectors.toList());
        this.respuestaDTOS = usuario.getRespuestas().stream()
                .map(respuesta -> new RespuestaDTO(respuesta)).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMail() {
        return mail;
    }

    public String getDNI() {
        return DNI;
    }

    public boolean isAsset() {
        return asset;
    }

    public List<UsuarioMateriaDTO> getUsuarioMaterias() {
        return usuarioMaterias;
    }

    public Rol getRol() {
        return rol;
    }

    public boolean isEstaEnUnaMateria() {
        return estaEnUnaMateria;
    }

    public List<ComentarioAdminDTO> getComentarioAdminDTOS() {
        return comentarioAdminDTOS;
    }

    public List<RespuestaDTO> getRespuestaDTOS() {
        return respuestaDTOS;
    }

    public String getUserProfileImg() {
        return userProfileImg;
    }
}
