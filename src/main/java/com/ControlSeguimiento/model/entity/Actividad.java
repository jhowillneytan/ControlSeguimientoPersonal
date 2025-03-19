package com.ControlSeguimiento.model.entity;

import java.util.Date;
import java.util.List;

import com.ControlSeguimiento.config.AuditoriaConfig;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "actividad")
@Setter
@Getter
public class Actividad extends AuditoriaConfig {
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idActividad;

    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date fechaInicio;

    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date fechaFin;

    private String descripcion;

    @OneToMany(mappedBy = "actividad", fetch = FetchType.EAGER)
    private List<Asignacion> asignaciones;

}
