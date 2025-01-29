package com.eschool.schoolpage.dtos;

import com.eschool.schoolpage.models.Materia;
import com.eschool.schoolpage.models.Rol;

import java.util.*;
import java.util.stream.Collectors;

public class MateriaAdminDTO {

    private Long id;

    private String nombre;
    private String descripcion;
    private String portada;
    private String color;
    private boolean isAsset;
    private String accessCode;
    Set<ContenidoDTO> contenidos = new HashSet<>();
    List<UsuarioDTO> alumnos = new ArrayList<>();
    List<UsuarioDTO> profesores = new ArrayList<>();

    public MateriaAdminDTO(Materia materia) {
        this.id = materia.getId();
        this.nombre = materia.getNombre();
        this.descripcion = materia.getDescripcion();
        this.portada = materia.getPortada();
        this.color = materia.getColor();
        this.isAsset = materia.isAsset();
        this.accessCode = materia.getAccessCode();
        this.contenidos = materia.getContenidos().stream()
                .map(contenido -> new ContenidoDTO(contenido)).collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ContenidoDTO::getId).reversed())));
        this.alumnos = materia.getUsuarioMaterias().stream().filter(usuarioMateria -> usuarioMateria.isAsset()).filter(usuarioMateria -> usuarioMateria.getUsuario().getRol().equals(Rol.ESTUDIANTE))
                .map(usuarioMateria -> new UsuarioDTO(usuarioMateria.getUsuario())).collect(Collectors.toList());
        this.profesores = materia.getUsuarioMaterias().stream().filter(usuarioMateria -> usuarioMateria.isAsset()).filter(usuarioMateria -> usuarioMateria.getUsuario().getRol().equals(Rol.PROFESOR))
                .map(usuarioMateria -> new UsuarioDTO(usuarioMateria.getUsuario())).collect(Collectors.toList());

    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPortada() {
        return portada;
    }

    public Set<ContenidoDTO> getContenidos() {
        return contenidos;
    }

    public List<UsuarioDTO> getAlumnos() {
        return alumnos;
    }

    public List<UsuarioDTO> getProfesores() {
        return profesores;
    }

    public String getColor() {
        return color;
    }

    public boolean isAsset() {
        return isAsset;
    }

    public String getAccessCode() {
        return accessCode;
    }
}
