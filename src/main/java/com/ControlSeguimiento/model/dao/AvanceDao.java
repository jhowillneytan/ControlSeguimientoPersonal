package com.ControlSeguimiento.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ControlSeguimiento.model.entity.Avance;

public interface AvanceDao extends JpaRepository<Avance, Long>{

    @Query("SELECT a FROM Avance a WHERE a.estado != 'ELIMINADO' AND a.asignacion.idAsignacion = :idAsignacion")
    List<Avance> listarAvancePoridAsignacion(@Param("idAsignacion") Long idAsignacion);
//     @Query(value = """
//     select av.* from avance av
//     inner join asignacion a on a.id_asignacion = av.id_asignacion
//     where av.id_asignacion = :idAsignacion and AV.estado != 'ELIMINADO'
//     """, nativeQuery = true)
// List<Avance> listarAvancePorIdActividad(@Param("idAsignacion") Long idAsignacion);
}
