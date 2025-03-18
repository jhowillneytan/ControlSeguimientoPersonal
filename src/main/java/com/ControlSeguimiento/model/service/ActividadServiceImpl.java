package com.ControlSeguimiento.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ControlSeguimiento.model.dao.ActividadDao;
import com.ControlSeguimiento.model.dao.PersonaDao;
import com.ControlSeguimiento.model.entity.Actividad;
import com.ControlSeguimiento.model.entity.Persona;

@Service
public class ActividadServiceImpl implements ActividadService{

    @Autowired
    private ActividadDao dao;

    @Override
    public List<Actividad> findAll() {
        return dao.findAll();
    }

    @Override
    public Actividad findById(Long idEntidad) {
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public void save(Actividad entidad) {
        dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        Actividad actividad = dao.findById(idEntidad).orElse(null);
        actividad.setEstado("ELIMINADO");
        dao.save(actividad);
    }

}
