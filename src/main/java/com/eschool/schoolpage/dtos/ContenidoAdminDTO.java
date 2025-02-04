package com.eschool.schoolpage.dtos;

import com.eschool.schoolpage.models.Contenido;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ContenidoAdminDTO {
    private Long id;

    private String titulo;
    private LocalDateTime fechaDePublicacion;
    private String detalleDelContenido;
    private String archivo;
    private boolean isAsset;
    private Set<ArchivoDTO> archivoDTOS = new HashSet<>();
    private Set<ComentarioAdminDTO> comentarios = new HashSet<>();


    public ContenidoAdminDTO(Contenido contenido) {
        this.id = contenido.getId();
        this.titulo = contenido.getTitulo();
        this.detalleDelContenido = contenido.getDetalleDelContenido();
        this.fechaDePublicacion = contenido.getFechaDePublicacion();
        this.archivo = contenido.getArchivo();
        this.isAsset = contenido.isAsset();
        this.archivoDTOS = contenido.getArchivos().stream().map(archivo1 -> new ArchivoDTO(archivo1)).collect(Collectors.toSet());
        this.comentarios = contenido.getComentarios().stream()
                .map(comentario -> new ComentarioAdminDTO(comentario)).collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ComentarioAdminDTO::getId).reversed())));
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

    public boolean isAsset() {
        return isAsset;
    }

    public Set<ComentarioAdminDTO> getComentarios() {
        return comentarios;
    }

    public Set<ArchivoDTO> getArchivoDTOS() {
        return archivoDTOS;
    }
}
