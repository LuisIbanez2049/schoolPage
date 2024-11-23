package com.eschool.schoolpage.dtos;

import com.eschool.schoolpage.models.JornadaTurno;
import com.eschool.schoolpage.models.UsuarioMateria;

public class UsuarioMateriaDTO {
    private JornadaTurno jornadaTurno;
    private String nombreMateria;

    public UsuarioMateriaDTO(UsuarioMateria usuarioMateria) {
        this.jornadaTurno = usuarioMateria.getJornadaTurno();
        this.nombreMateria = usuarioMateria.getMateria().getNombre();
    }

    public JornadaTurno getJornadaTurno() {
        return jornadaTurno;
    }

    public String getNombreMateria() {
        return nombreMateria;
    }
}
