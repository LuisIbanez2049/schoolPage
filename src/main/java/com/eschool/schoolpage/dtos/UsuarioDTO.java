package com.eschool.schoolpage.dtos;

import com.eschool.schoolpage.models.Rol;
import com.eschool.schoolpage.models.Usuario;
import com.eschool.schoolpage.models.UsuarioMateria;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioDTO {
    private Long id;
    private String name;
    private String lastName;
    private String mail;
    private String DNI;
    private boolean asset = true; // "Activo"
    private Rol rol;
    private List<UsuarioMateriaDTO> usuarioMaterias = new ArrayList<>();

    public UsuarioDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.name = usuario.getName();
        this.lastName = usuario.getLastName();
        this.mail = usuario.getMail();
        this.DNI= usuario.getDNI();
        this.asset = usuario.isAsset();
        this.rol = usuario.getRol();
        this.usuarioMaterias = usuario.getUsuarioMaterias().stream().map(usuarioMateria -> new UsuarioMateriaDTO(usuarioMateria)).collect(Collectors.toList());
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
}
