package com.eschool.schoolpage.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Contenido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private LocalDateTime fechaDePublicacion;
    private String detalleDelContenido;
    private String archivo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "materia_id")
    private Materia materia;

    @OneToMany(mappedBy = "contenido", fetch = FetchType.EAGER)
    Set<Comentario> comentarios = new HashSet<>();

    //----------------------------Métodos Constructor------------------------------------
    public Contenido() { }

    public Contenido(String titulo, LocalDateTime fechaDePublicacion, String detalleDelContenido, String archivo) {
        this.titulo = titulo;
        this.fechaDePublicacion = fechaDePublicacion;
        this.detalleDelContenido = detalleDelContenido;
        this.archivo = archivo;
    }
    //--------------------------------------------------------------------------------------


    //----------------------------Métodos GETTER Y SETTER------------------------------------
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDateTime getFechaDePublicacion() {
        return fechaDePublicacion;
    }

    public void setFechaDePublicacion(LocalDateTime fechaDePublicacion) {
        this.fechaDePublicacion = fechaDePublicacion;
    }

    public String getDetalleDelContenido() {
        return detalleDelContenido;
    }

    public void setDetalleDelContenido(String detalleDelContenido) {
        this.detalleDelContenido = detalleDelContenido;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public Long getId() {
        return id;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Set<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(Set<Comentario> comentarios) {
        this.comentarios = comentarios;
    }
    //--------------------------------------------------------------------------------------


    public void addComentario(Comentario comentario){
        comentario.setContenido(this);
        this.comentarios.add(comentario);
    }

}
