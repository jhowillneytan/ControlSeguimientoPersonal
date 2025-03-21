package com.ControlSeguimiento.model.entity;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.ControlSeguimiento.config.AuditoriaConfig;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

    private String titulo;
    private String descripcion;
    private double progreso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prioridad")
    private Prioridad prioridad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proyecto")
    private Proyecto proyecto;

    @OneToMany(mappedBy = "actividad", fetch = FetchType.EAGER)
    private List<Asignacion> asignaciones;

    @JsonIgnore
    public List<Asignacion> getAsignacionesOrdenadas() {
        return asignaciones.stream() // Convierte la lista de asignaciones en un stream.
                .filter(asignacion -> !"ELIMINADO".equals(asignacion.getEstado())) // Filtra las asignaciones cuyo
                                                                                   // estado no es ELIMINADO.
                // .sorted(Comparator.comparing(Asignacion::getFecha)) // Ordena las
                // asignaciones por el campo 'fecha'.
                .collect(Collectors.toList()); // Recoge el resultado en una lista.
    }

}
