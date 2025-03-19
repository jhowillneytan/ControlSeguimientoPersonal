package com.ControlSeguimiento.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ControlSeguimiento.model.entity.Actividad;

public interface ActividadDao extends JpaRepository<Actividad, Long>{
    @Query("SELECT a FROM Actividad a WHERE a.estado != 'ELIMINADO'")
    List<Actividad> listarActividades();
}
