package com.ControlSeguimiento.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ControlSeguimiento.model.dao.ArchivoAdjuntoDao;
import com.ControlSeguimiento.model.entity.ArchivoAdjunto;

@Service
public class ArchivoAdjuntoServiceImpl implements ArchivoAdjuntoService {
    
    @Autowired
    private ArchivoAdjuntoDao dao;

    @Override
    public List<ArchivoAdjunto> findAll() {
        // TODO Auto-generated method stub
        return dao.findAll();
    }

    @Override
    public ArchivoAdjunto findById(Long idEntidad) {
        // TODO Auto-generated method stub
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public void save(ArchivoAdjunto entidad) {
        // TODO Auto-generated method stub
        dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public List<ArchivoAdjunto> listarArchivosPorIdAvance(Long idAvance) {
        // TODO Auto-generated method stub
        return dao.listarArchivosPorIdAvance(idAvance);
    }
}
