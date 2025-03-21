package com.ControlSeguimiento.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ControlSeguimiento.model.entity.Actividad;
import com.ControlSeguimiento.model.entity.Asignacion;

public interface AsignacionDao extends JpaRepository<Asignacion, Long>{

    @Query("SELECT a FROM Asignacion a WHERE a.actividad = ?1 AND a.estado != 'ELIMINADO'")
    List<Asignacion> findByActividad(Actividad actividad);

    @Query("SELECT a FROM Asignacion a WHERE a.persona.idPersona = ?1 AND a.estado != 'ELIMINADO'")
    List<Asignacion> listarAsignacionesPorIdPersona(Long idPersona);
}
