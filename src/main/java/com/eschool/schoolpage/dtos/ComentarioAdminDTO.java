package com.eschool.schoolpage.dtos;

import com.eschool.schoolpage.models.Comentario;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ComentarioAdminDTO {
    private Long id;
    private boolean isAsset;

    private String texto;
    private LocalDateTime fecha;
    Set<RespuestaDTO> respuestas = new HashSet<>();
    private String nombreUsuario;
    private String nombreContenido;
    private Long userId;
    private String nombreDeMateria;
    private String profileImgFromUserComment;

    public ComentarioAdminDTO(Comentario comentario) {
        this.id = comentario.getId();
        this.isAsset = comentario.isAsset();
        this.texto = comentario.getTexto();
        this.fecha = comentario.getFecha();
        this.nombreDeMateria = comentario.getContenido().getMateria().getNombre();
        this.nombreContenido = comentario.getContenido().getTitulo();
        this.nombreUsuario = comentario.getUsuario().getName() + " " + comentario.getUsuario().getLastName();
        this.profileImgFromUserComment = comentario.getUsuario().getProfileUserImage();
        this.userId = comentario.getUsuario().getId();
        this.respuestas = comentario.getRespuestas().stream().map(respuesta -> new RespuestaDTO(respuesta))
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(RespuestaDTO::getId))));
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }


    public void setRespuestas(Set<RespuestaDTO> respuestas) {
        this.respuestas = respuestas;
    }

    public Long getId() {
        return id;
    }

    public boolean isAsset() {
        return isAsset;
    }

    public String getTexto() {
        return texto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public Set<RespuestaDTO> getRespuestas() {
        return respuestas;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getNombreContenido() {
        return nombreContenido;
    }

    public Long getUserId() {
        return userId;
    }

    public String getProfileImgFromUserComment() {
        return profileImgFromUserComment;
    }

    public String getNombreDeMateria() {
        return nombreDeMateria;
    }
}
