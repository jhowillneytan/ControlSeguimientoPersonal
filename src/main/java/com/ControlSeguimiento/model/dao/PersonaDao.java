package com.ControlSeguimiento.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ControlSeguimiento.model.entity.Persona;

public interface PersonaDao extends JpaRepository<Persona, Long>{

    @Query("SELECT p FROM Persona p WHERE p.estado != 'ELIMINADO'")
    List<Persona> listarPersonas();

    @Query("SELECT a.persona FROM Asignacion a WHERE a.actividad.idActividad = ?1")
    List<Persona> listarPersonasAsignadaPorIdActividad(Long idActividad);

    @Query("SELECT p FROM Persona p WHERE p.ci = ?1  AND p.estado != 'ELIMINADO'")
    Persona buscarPorCi(String ci);

    @Query("SELECT p FROM Persona p WHERE p.ci != ?1 AND p.ci = ?2 AND p.estado != 'ELIMINADO'")
    Persona compararCi(String ciActual, String ci);

    @Query("SELECT p FROM Usuario u JOIN u.persona p WHERE p.idPersona = ?1 AND p.estado != 'ELIMINADO'")
    Persona buscarPersonaPorIdUsuario(Long idUsuario);
}
