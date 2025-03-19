package com.ControlSeguimiento.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ControlSeguimiento.model.entity.Prioridad;

public interface PrioridadDao extends JpaRepository<Prioridad, Long>{
    
}
