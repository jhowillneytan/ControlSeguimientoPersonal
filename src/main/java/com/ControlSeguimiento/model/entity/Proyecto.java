package com.ControlSeguimiento.model.entity;

import java.util.Date;

import com.ControlSeguimiento.config.AuditoriaConfig;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "proyecto")
@Setter
@Getter
public class Proyecto extends AuditoriaConfig {
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProyecto;

    private String nombre;
    private String descripcion;

    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date fechaInicio;

    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date fechaFin;
    
    private double progreso;
    
}
