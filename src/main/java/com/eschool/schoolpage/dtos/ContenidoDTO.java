package com.eschool.schoolpage.dtos;

import com.eschool.schoolpage.models.Comentario;
import com.eschool.schoolpage.models.Contenido;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ContenidoDTO {
    private Long id;

    private String titulo;
    private LocalDateTime fechaDePublicacion;
    private String detalleDelContenido;
    private String archivo;
    private Set<ComentarioDTO> comentarios = new HashSet<>();


    public ContenidoDTO(Contenido contenido) {
        this.id = contenido.getId();
        this.titulo = contenido.getTitulo();
        this.detalleDelContenido = contenido.getDetalleDelContenido();
        this.fechaDePublicacion = contenido.getFechaDePublicacion();
        this.archivo = contenido.getArchivo();
        this.comentarios = contenido.getComentarios().stream().filter(comentario -> comentario.isAsset()).map(comentario -> new ComentarioDTO(comentario)).collect(Collectors.toSet());
    }


    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public LocalDateTime getFechaDePublicacion() {
        return fechaDePublicacion;
    }

    public String getDetalleDelContenido() {
        return detalleDelContenido;
    }

    public String getArchivo() {
        return archivo;
    }

    public Set<ComentarioDTO> getComentarios() {
        return comentarios;
    }
}
