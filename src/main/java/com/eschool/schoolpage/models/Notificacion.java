package com.eschool.schoolpage.models;

import com.eschool.schoolpage.dtos.RespuestaDTO;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensajeDe;
    private String userImg;
    private String typeNotification;
    private String texto;
    private boolean visto = false;
    private String materia;
    private String contenido;
    private boolean asset = true;
    private LocalDateTime creacionDeRespuesta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;




    public Notificacion() {}

    public Notificacion(String mensajeDe,String userImg, String typeNotification, String texto, String materia, String contenido, LocalDateTime creacionDeRespuesta) {
        this.mensajeDe = mensajeDe;
        this.userImg = userImg;
        this.typeNotification = typeNotification;
        this.texto = texto;
        this.materia = materia;
        this.contenido = contenido;
        this.creacionDeRespuesta = creacionDeRespuesta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getMensajeDe() {
        return mensajeDe;
    }

    public void setMensajeDe(String mensajeDe) {
        this.mensajeDe = mensajeDe;
    }

    public boolean isVisto() {
        return visto;
    }

    public void setVisto(boolean visto) {
        this.visto = visto;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public boolean isAsset() {
        return asset;
    }

    public void setAsset(boolean asset) {
        this.asset = asset;
    }

    public LocalDateTime getCreacionDeRespuesta() {
        return creacionDeRespuesta;
    }

    public String getTypeNotification() {
        return typeNotification;
    }

    public void setTypeNotification(String typeNotification) {
        this.typeNotification = typeNotification;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public void setCreacionDeRespuesta(LocalDateTime creacionDeRespuesta) {
        this.creacionDeRespuesta = creacionDeRespuesta;
    }

    public String getTiempoTranscurrido() {
        LocalDateTime fechaCreacion = creacionDeRespuesta;
        LocalDateTime fechaActual = LocalDateTime.now();
        return TimeDifference.calcularDiferencia(fechaCreacion, fechaActual);
    }

}
