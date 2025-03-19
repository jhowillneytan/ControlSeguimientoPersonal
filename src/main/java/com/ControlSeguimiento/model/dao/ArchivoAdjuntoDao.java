package com.ControlSeguimiento.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ControlSeguimiento.model.entity.ArchivoAdjunto;

public interface ArchivoAdjuntoDao extends JpaRepository<ArchivoAdjunto, Long>{

    @Query("SELECT a FROM ArchivoAdjunto a WHERE a.estado != 'ELIMINADO' AND a.avance.idAvance = ?1")
    List<ArchivoAdjunto> listarArchivosPorIdAvance(Long idAvance);

}