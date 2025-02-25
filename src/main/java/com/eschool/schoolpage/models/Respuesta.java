package com.eschool.schoolpage.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Respuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String texto;
    private LocalDateTime fecha;
    private boolean isAsset = true;
    private String respuestaPara;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "comentario_id")
    private Comentario comentario;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;


    //----------------------------Métodos Constructor------------------------------------
    public Respuesta() { }

    public Respuesta(String texto, LocalDateTime fecha) {
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

    public Comentario getComentario() {
        return comentario;
    }

    public void setComentario(Comentario comentario) {
        this.comentario = comentario;
    }

    public boolean isAsset() {
        return isAsset;
    }

    public void setAsset(boolean asset) {
        isAsset = asset;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getRespuestaPara() {
        return respuestaPara;
    }

    public void setRespuestaPara(String respuestaPara) {
        this.respuestaPara = respuestaPara;
    }


    //--------------------------------------------------------------------------------------

}
