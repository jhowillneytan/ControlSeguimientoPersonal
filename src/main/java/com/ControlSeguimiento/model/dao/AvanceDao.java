package com.ControlSeguimiento.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ControlSeguimiento.model.entity.Avance;

public interface AvanceDao extends JpaRepository<Avance, Long>{
    @Query("SELECT a FROM Avance a WHERE a.estado != 'ELIMINADO' AND a.actividad.idActividad = ?1")
    List<Avance> listarAvancePorIdAsignacion(Long idAsignacion);
}
