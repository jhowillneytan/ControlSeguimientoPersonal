package com.ControlSeguimiento.model.entity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.ControlSeguimiento.config.AuditoriaConfig;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import jakarta.persistence.OneToMany;
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

    @OneToMany(mappedBy = "asignacion", fetch = FetchType.EAGER)
    private List<Avance> avances;

        @JsonIgnore
    public List<Avance> getAvancesOrdenadas() {
        return avances.stream() // Convierte la lista de asignaciones en un stream.
                .filter(avance -> !"ELIMINADO".equals(avance.getEstado())) // Filtra las asignaciones cuyo
                                                                                   // estado no es ELIMINADO.
                // .sorted(Comparator.comparing(Asignacion::getFecha)) // Ordena las
                // asignaciones por el campo 'fecha'.
                .collect(Collectors.toList()); // Recoge el resultado en una lista.
    }

    // @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    // @ManyToMany(fetch = FetchType.LAZY)
    // @JoinTable(name = "asignacion_persona", 
    // joinColumns = @JoinColumn(name = "id_asignacion"), 
    // inverseJoinColumns = @JoinColumn(name = "id_persona"))
    // private Set<Persona> personas;
}
