package com.ControlSeguimiento.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ControlSeguimiento.model.entity.Proyecto;

public interface ProyectoDao extends JpaRepository<Proyecto, Long>{
    @Query("SELECT p FROM Proyecto p WHERE p.estado != 'ELIMINADO'")
    List<Proyecto> listar();
}
