package com.eschool.schoolpage.dtos;

import com.eschool.schoolpage.models.Comentario;
import com.eschool.schoolpage.models.Contenido;
import com.eschool.schoolpage.models.Respuesta;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ComentarioDTO {
    private Long id;

    private String texto;
    private LocalDateTime fecha;
    Set<RespuestaDTO> respuestas = new HashSet<>();
    private String nombreUsuario;
    private String nombreContenido;
    private Long userId;

    public ComentarioDTO(Comentario comentario) {
        this.id = comentario.getId();
        this.texto = comentario.getTexto();
        this.fecha = comentario.getFecha();
        this.nombreContenido = comentario.getContenido().getTitulo();
        this.nombreUsuario = comentario.getUsuario().getName() + " " + comentario.getUsuario().getLastName();
        this.userId = comentario.getUsuario().getId();
        this.respuestas = comentario.getRespuestas().stream().filter(respuesta -> respuesta.isAsset()).map(respuesta -> new RespuestaDTO(respuesta))
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
}
