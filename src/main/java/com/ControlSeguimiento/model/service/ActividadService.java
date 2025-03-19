package com.ControlSeguimiento.model.service;


import java.util.List;

import com.ControlSeguimiento.model.entity.Actividad;

public interface ActividadService extends GenericoService <Actividad, Long>{
    List<Actividad> listarActividades();
}
