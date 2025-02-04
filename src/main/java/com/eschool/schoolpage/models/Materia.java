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

    @Lob // Esto indica que el campo debe almacenarse como un tipo de datos grande. Me permite ingresar cadenas de texto mas largas.
    private String descripcion;

    private String portada;
    private boolean isAsset = true;
    private String color;
    private String accessCode;

    @OneToMany(mappedBy = "materia", fetch = FetchType.EAGER)
    Set<Contenido> contenidos = new HashSet<>();


    @OneToMany(mappedBy = "materia", fetch = FetchType.EAGER)
    List<UsuarioMateria> usuarioMaterias = new ArrayList<>();


    //----------------------------Métodos Constructor------------------------------------
    public Materia() { }

    public Materia(String nombre, String descripcion, String portada, String color, String accessCode) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.portada = portada;
        this.color = color;
        this.accessCode = accessCode;
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

    public boolean isAsset() {
        return isAsset;
    }

    public void setAsset(boolean asset) {
        isAsset = asset;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
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
