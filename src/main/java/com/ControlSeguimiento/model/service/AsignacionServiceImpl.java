package com.ControlSeguimiento.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ControlSeguimiento.model.dao.AsignacionDao;
import com.ControlSeguimiento.model.entity.Actividad;
import com.ControlSeguimiento.model.entity.Asignacion;

@Service
public class AsignacionServiceImpl implements AsignacionService{

        @Autowired
    private AsignacionDao dao;

    @Override
    public List<Asignacion> findAll() {
        // TODO Auto-generated method stub
        return dao.findAll();
    }

    @Override
    public Asignacion findById(Long idEntidad) {
        // TODO Auto-generated method stub
        return dao.findById(idEntidad).orElse(null);
    }

    @Override
    public void save(Asignacion entidad) {
        // TODO Auto-generated method stub
        dao.save(entidad);
    }

    @Override
    public void deleteById(Long idEntidad) {
        //Asignacion asignacion = dao.findById(idEntidad).orElse(null);
        dao.deleteById(idEntidad);
    }

    @Override
    public List<Asignacion> findByActividad(Actividad actividad) {
        return dao.findByActividad(actividad);
    }

    @Override
    public List<Asignacion> listarAsignacionesPorIdPersona(Long idPersona) {
        // TODO Auto-generated method stub
        return dao.listarAsignacionesPorIdPersona(idPersona);
    }
    
}
