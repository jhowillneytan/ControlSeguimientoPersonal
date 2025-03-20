package com.ControlSeguimiento.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ControlSeguimiento.model.dao.AvanceDao;
import com.ControlSeguimiento.model.entity.Avance;

@Service
public class AvanceServiceImpl implements AvanceService {

    @Autowired
    private AvanceDao dao;

    @Override
    public List<Avance> findAll() {
        // TODO Auto-generated method stub
        return dao.findAll();
    }

    @Override
    public Avance findById(Long idEntidad) {
        // TODO Auto-generated method stub
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public void save(Avance entidad) {
        // TODO Auto-generated method stub
        dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public List<Avance> listarAvancePoridAsignacion(Long idAsignacion) {
        // TODO Auto-generated method stub
        return dao.listarAvancePoridAsignacion(idAsignacion);
    }
    
}
