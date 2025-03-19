package com.ControlSeguimiento.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ControlSeguimiento.model.dao.PrioridadDao;
import com.ControlSeguimiento.model.entity.Prioridad;

public class PrioridadServiceImpl implements PrioridadService{
    
    @Autowired
    private PrioridadDao dao;

    @Override
    public List<Prioridad> findAll() {
        // TODO Auto-generated method stub
        return dao.findAll();
    }

    @Override
    public Prioridad findById(Long idEntidad) {
        // TODO Auto-generated method stub
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public void save(Prioridad entidad) {
        // TODO Auto-generated method stub
        dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }
    
}
