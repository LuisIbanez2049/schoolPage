package com.eschool.schoolpage.dtos;

import com.eschool.schoolpage.models.Archivo;
import com.eschool.schoolpage.models.TipoArchivo;

public class ArchivoDTO {
    private Long id;

    private String name;
    private String tipoArchivo;
    private String link;
    private boolean isAsset;

    public ArchivoDTO(Archivo archivo) {
        this.id = archivo.getId();
        this.name = archivo.getName();
        this.tipoArchivo = archivo.getTipoArchivo();
        this.link = archivo.getLink();
        this.isAsset = archivo.isAsset();
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public String getLink() {
        return link;
    }

    public boolean isAsset() {
        return isAsset;
    }
}
