package com.eschool.schoolpage.models;

import jakarta.persistence.*;

@Entity
public class UsuarioMateria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private JornadaTurno jornadaTurno;
    private boolean isAsset = true;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "materia_id")
    private Materia materia;

    //----------------------Constructor--------------------------------------

    public UsuarioMateria() { }

    public UsuarioMateria(JornadaTurno jornadaTurno) {
        this.jornadaTurno = jornadaTurno;
    }
    //------------------------------------------------------------------------


    //----------------------------Métodos GETTER Y SETTER------------------------------------

    public JornadaTurno getJornadaTurno() {
        return jornadaTurno;
    }

    public void setJornadaTurno(JornadaTurno jornadaTurno) {
        this.jornadaTurno = jornadaTurno;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Long getId() {
        return id;
    }

    public boolean isAsset() {
        return isAsset;
    }

    public void setAsset(boolean asset) {
        isAsset = asset;
    }
    //------------------------------------------------------------------------


}
