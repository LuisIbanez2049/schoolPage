package com.eschool.schoolpage.dtos;

import com.eschool.schoolpage.models.Contenido;
import com.eschool.schoolpage.models.Materia;
import com.eschool.schoolpage.models.Rol;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MateriaDTO {
    private Long id;

    private String nombre;
    private String descripcion;
    private String portada;
    Set<ContenidoDTO> contenidos = new HashSet<>();
    List<UsuarioDTO> alumnos = new ArrayList<>();
    List<UsuarioDTO> profesores = new ArrayList<>();

    public MateriaDTO(Materia materia) {
        this.id = materia.getId();
        this.nombre = materia.getNombre();
        this.descripcion = materia.getDescripcion();
        this.portada = materia.getPortada();
        this.contenidos = materia.getContenidos().stream().map(contenido -> new ContenidoDTO(contenido)).collect(Collectors.toSet());
        this.alumnos = materia.getUsuarioMaterias().stream().filter(usuarioMateria -> usuarioMateria.isAsset()).filter(usuarioMateria -> usuarioMateria.getUsuario().getRol().equals(Rol.ESTUDIANTE))
                .map(usuarioMateria -> new UsuarioDTO(usuarioMateria.getUsuario())).collect(Collectors.toList());
        this.profesores = materia.getUsuarioMaterias().stream().filter(usuarioMateria -> usuarioMateria.getUsuario().getRol().equals(Rol.PROFESOR))
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
}
