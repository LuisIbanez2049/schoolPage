package com.eschool.schoolpage.models;

import jakarta.persistence.*;

@Entity
public class Archivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String tipoArchivo;
    private String link;
    private boolean isAsset = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contenido_id")
    private Contenido contenido;

    public Archivo() {

    }

    public Archivo(String name, String tipoArchivo, String link) {
        this.name = name;
        this.tipoArchivo = tipoArchivo;
        this.link = link;
    }



    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Contenido getContenido() {
        return contenido;
    }

    public void setContenido(Contenido contenido) {
        this.contenido = contenido;
    }

    public boolean isAsset() {
        return isAsset;
    }

    public void setAsset(boolean asset) {
        isAsset = asset;
    }
}
