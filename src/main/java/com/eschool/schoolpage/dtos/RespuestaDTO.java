package com.eschool.schoolpage.dtos;

import com.eschool.schoolpage.models.Comentario;
import com.eschool.schoolpage.models.Respuesta;

import java.time.LocalDateTime;

public class RespuestaDTO {
    private Long id;

    private String texto;
    private LocalDateTime fecha;

    public RespuestaDTO(Respuesta respuesta) {
        this.id = respuesta.getId();
        this.texto = respuesta.getTexto();
        this.fecha = respuesta.getFecha();
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

}
