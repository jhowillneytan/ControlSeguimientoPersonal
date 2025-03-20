package com.ControlSeguimiento.model.service;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.ControlSeguimiento.model.entity.Avance;

public interface AvanceService extends GenericoService <Avance, Long>{
    List<Avance> listarAvancePoridAsignacion(@Param("idAsignacion") Long idAsignacion);
}
