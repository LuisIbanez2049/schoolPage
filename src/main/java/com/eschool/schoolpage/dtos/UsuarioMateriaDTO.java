package com.eschool.schoolpage.dtos;

import com.eschool.schoolpage.models.JornadaTurno;
import com.eschool.schoolpage.models.UsuarioMateria;

public class UsuarioMateriaDTO {
    private JornadaTurno jornadaTurno;
    private String nombreMateria;
    private Long id;
    private Long materiaId;

    public UsuarioMateriaDTO(UsuarioMateria usuarioMateria) {
        this.jornadaTurno = usuarioMateria.getJornadaTurno();
        this.nombreMateria = usuarioMateria.getMateria().getNombre();
        this.id = usuarioMateria.getId();
        this.materiaId = usuarioMateria.getMateria().getId();
    }

    public JornadaTurno getJornadaTurno() {
        return jornadaTurno;
    }

    public String getNombreMateria() {
        return nombreMateria;
    }

    public Long getId() {
        return id;
    }

    public Long getMateriaId() {
        return materiaId;
    }
}
