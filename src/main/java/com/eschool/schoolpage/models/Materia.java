package com.eschool.schoolpage.models;

import com.eschool.schoolpage.dtos.UsuarioDTO;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Materia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private String portada;

    @OneToMany(mappedBy = "materia", fetch = FetchType.EAGER)
    Set<Contenido> contenidos = new HashSet<>();

    @OneToMany(mappedBy = "materia", fetch = FetchType.EAGER)
    List<UsuarioMateria> usuarioMaterias = new ArrayList<>();


    //----------------------------Métodos Constructor------------------------------------
    public Materia() { }

    public Materia(String nombre, String descripcion, String portada) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.portada = portada;
    }
    //--------------------------------------------------------------------------------------


    //----------------------------Métodos GETTER Y SETTER------------------------------------

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPortada() {
        return portada;
    }

    public void setPortada(String portada) {
        this.portada = portada;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public Set<Contenido> getContenidos() {
        return contenidos;
    }

    public void setContenidos(Set<Contenido> contenidos) {
        this.contenidos = contenidos;
    }

    public List<UsuarioMateria> getUsuarioMaterias() {
        return usuarioMaterias;
    }

    public void setUsuarioMaterias(List<UsuarioMateria> usuarioMaterias) {
        this.usuarioMaterias = usuarioMaterias;
    }
    //--------------------------------------------------------------------------------------


    public void addContenido(Contenido contenido){
        contenido.setMateria(this);
        this.contenidos.add(contenido);
    }

    public void addUsuarioMateria(UsuarioMateria usuarioMateria){
        this.usuarioMaterias.add(usuarioMateria);
        usuarioMateria.setMateria(this);
    }

}
