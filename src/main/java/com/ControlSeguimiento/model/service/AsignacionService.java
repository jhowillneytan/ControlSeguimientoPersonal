package com.ControlSeguimiento.model.service;

import java.util.List;

import com.ControlSeguimiento.model.entity.Actividad;
import com.ControlSeguimiento.model.entity.Asignacion;

public interface AsignacionService extends GenericoService <Asignacion, Long>{
    List<Asignacion> findByActividad(Actividad actividad);
}
