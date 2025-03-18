package com.ControlSeguimiento.model.entity;

import java.util.Set;

import com.ControlSeguimiento.config.AuditoriaConfig;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "asignacion")
@Setter
@Getter
public class Asignacion extends AuditoriaConfig {
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAsignacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona")
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_actividad")
    private Actividad actividad;

    // @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    // @ManyToMany(fetch = FetchType.LAZY)
    // @JoinTable(name = "asignacion_persona", 
    // joinColumns = @JoinColumn(name = "id_asignacion"), 
    // inverseJoinColumns = @JoinColumn(name = "id_persona"))
    // private Set<Persona> personas;
}
