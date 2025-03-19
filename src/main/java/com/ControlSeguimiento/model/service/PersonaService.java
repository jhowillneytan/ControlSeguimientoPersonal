package com.ControlSeguimiento.model.service;

import java.util.List;

import com.ControlSeguimiento.model.entity.Persona;

public interface PersonaService extends GenericoService <Persona, Long>{

    List<Persona> listarPersonas();
    Persona buscarPorCi(String ci);
    Persona compararCi(String ciActual, String ci);
    Persona buscarPersonaPorIdUsuario(Long idUsuario);
    List<Persona> listarPersonasAsignadaPorIdActividad(Long idActividad);
}
