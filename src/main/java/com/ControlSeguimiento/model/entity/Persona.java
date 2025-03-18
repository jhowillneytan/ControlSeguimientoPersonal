package com.ControlSeguimiento.model.entity;

import java.util.List;

import com.ControlSeguimiento.config.AuditoriaConfig;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "persona")
@Setter
@Getter
public class Persona extends AuditoriaConfig {
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPersona;
    private String nombre;
    private String paterno;
    private String materno;
    private String ci;
    private String correo;

    @OneToMany(mappedBy = "persona", fetch = FetchType.EAGER)
    private List<Usuario> usuarios;

    public String getNombreCompleto(){
        if(this.getMaterno() == null){
            return this.getNombre()+" "+this.getPaterno();
        }

        if(this.getPaterno() == null){
            return this.getNombre()+" "+this.getMaterno();
        }

        return this.getNombre()+" "+this.getPaterno()+" "+this.getMaterno();
    }
}
