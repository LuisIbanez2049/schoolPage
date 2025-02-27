package com.eschool.schoolpage.dtos;

import com.eschool.schoolpage.models.Comentario;
import com.eschool.schoolpage.models.Notificacion;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class NotificacionDTO {
    private Long id;
    private String mensajeDe;
    private String texto;
    private boolean visto = false;
    private String materia;
    private String contenido;
    private boolean asset;
    private String tiempoTranscurrido;




    public NotificacionDTO(Notificacion notificacion) {
        this.id = notificacion.getId();
        this.texto = notificacion.getTexto();
        this.mensajeDe = notificacion.getMensajeDe();
        this.visto = notificacion.isVisto();
        this.asset = notificacion.isAsset();
        this.materia = notificacion.getMateria();
        this.contenido = notificacion.getContenido();
        this.tiempoTranscurrido = notificacion.getTiempoTranscurrido();
    }

    public Long getId() {
        return id;
    }

    public String getTexto() {
        return texto;
    }

    public String getMensajeDe() {
        return mensajeDe;
    }

    public boolean isVisto() {
        return visto;
    }

    public String getMateria() {
        return materia;
    }

    public String getContenido() {
        return contenido;
    }

    public boolean isAsset() {
        return asset;
    }

    public String getTiempoTranscurrido() {
        return tiempoTranscurrido;
    }
}
