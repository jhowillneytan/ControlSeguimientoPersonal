package com.ControlSeguimiento.model.service;

import java.util.List;

import com.ControlSeguimiento.model.entity.Proyecto;

public interface ProyectoService extends GenericoService <Proyecto, Long>{
    List<Proyecto> listar();
}
