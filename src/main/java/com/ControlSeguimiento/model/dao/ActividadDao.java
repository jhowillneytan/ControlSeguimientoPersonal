package com.ControlSeguimiento.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ControlSeguimiento.model.entity.Actividad;

public interface ActividadDao extends JpaRepository<Actividad, Long>{
    
}
