package com.eschool.schoolpage.dtos;

import com.eschool.schoolpage.models.Comentario;
import com.eschool.schoolpage.models.Notificacion;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class NotificacionDTO {
    private Long id;
    private String texto;




    public NotificacionDTO(Notificacion notificacion) {
        this.id = notificacion.getId();
        this.texto = notificacion.getTexto();
    }

    public Long getId() {
        return id;
    }

    public String getTexto() {
        return texto;
    }

}
