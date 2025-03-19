package com.ControlSeguimiento.model.service;

import java.util.List;

import com.ControlSeguimiento.model.entity.Avance;

public interface AvanceService extends GenericoService <Avance, Long>{
    List<Avance> listarAvancePorIdAsignacion(Long idAsignacion);
}
