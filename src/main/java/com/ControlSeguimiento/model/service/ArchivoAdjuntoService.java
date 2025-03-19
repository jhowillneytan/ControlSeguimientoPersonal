package com.ControlSeguimiento.model.service;

import java.util.List;

import com.ControlSeguimiento.model.entity.ArchivoAdjunto;

public interface ArchivoAdjuntoService extends GenericoService <ArchivoAdjunto, Long>{
    List<ArchivoAdjunto> listarArchivosPorIdAvance(Long idAvance);
}
