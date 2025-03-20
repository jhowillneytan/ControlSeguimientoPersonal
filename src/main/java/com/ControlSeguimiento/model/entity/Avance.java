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
@Table(name = "avance")
@Setter
@Getter
public class Avance extends AuditoriaConfig {
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAvance;

    private String descripcion;

    private String observacion;

    private double valorProgreso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_actividad")
    private Actividad actividad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_asignacion")
    private Asignacion asignacion;

    @OneToMany(mappedBy = "avance", fetch = FetchType.EAGER)
    private List<ArchivoAdjunto> archivoAdjuntos;
}
