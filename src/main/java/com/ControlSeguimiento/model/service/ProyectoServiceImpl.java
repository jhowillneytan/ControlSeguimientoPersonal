package com.ControlSeguimiento.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ControlSeguimiento.model.dao.ProyectoDao;
import com.ControlSeguimiento.model.entity.Persona;
import com.ControlSeguimiento.model.entity.Proyecto;

@Service
public class ProyectoServiceImpl implements ProyectoService {

    @Autowired
    private ProyectoDao dao;

    @Override
    public List<Proyecto> findAll() {
        // TODO Auto-generated method stub
        return dao.findAll();
    }

    @Override
    public Proyecto findById(Long idEntidad) {
        // TODO Auto-generated method stub
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public void save(Proyecto entidad) {
        // TODO Auto-generated method stub
        dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        // TODO Auto-generated method stub
        Proyecto proyecto = dao.findById(idEntidad).orElse(null);
        proyecto.setEstado("ELIMINADO");
        dao.save(proyecto);
    }

    @Override
    public List<Proyecto> listar() {
        // TODO Auto-generated method stub
        return dao.listar();
    }
    
}
