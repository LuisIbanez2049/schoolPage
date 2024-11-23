package com.eschool.schoolpage.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String texto;
    private LocalDateTime fecha;
    private boolean isAsset = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contenido_id")
    private Contenido contenido;


    @OneToMany(mappedBy = "comentario",fetch = FetchType.EAGER)
    Set<Respuesta> respuestas = new HashSet<>();

    //----------------------------Métodos Constructor------------------------------------
    public Comentario() { }

    public Comentario(String texto, LocalDateTime fecha) {
        this.texto = texto;
        this.fecha = fecha;
    }
    //--------------------------------------------------------------------------------------


    //----------------------------Métodos GETTER Y SETTER------------------------------------
    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public Contenido getContenido() {
        return contenido;
    }

    public void setContenido(Contenido contenido) {
        this.contenido = contenido;
    }

    public Set<Respuesta> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(Set<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }

    public boolean isAsset() {
        return isAsset;
    }

    public void setAsset(boolean asset) {
        isAsset = asset;
    }
    //--------------------------------------------------------------------------------------

    public void addRespuesta(Respuesta respuesta){
        respuesta.setComentario(this);
        this.respuestas.add(respuesta);
    }

}
