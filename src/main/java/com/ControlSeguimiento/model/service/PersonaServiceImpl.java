package com.ControlSeguimiento.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ControlSeguimiento.model.dao.PersonaDao;
import com.ControlSeguimiento.model.entity.Persona;

@Service
public class PersonaServiceImpl implements PersonaService {

    @Autowired
    private PersonaDao personaDao;

    @Override
    public List<Persona> findAll() {
        return personaDao.findAll();
    }

    @Override
    public void save(Persona persona) {
        personaDao.save(persona);
    }

    @Override
    public Persona findById(Long id) {
        return personaDao.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        Persona persona = personaDao.findById(id).orElse(null);
        persona.setEstado("ELIMINADO");
        personaDao.save(persona);
    }

    @Override
    public Persona buscarPorCi(String ci) {
        return personaDao.buscarPorCi(ci);
    }

    @Override
    public Persona compararCi(String ciActual, String ci) {
        return personaDao.compararCi(ciActual, ci);
    }

    @Override
    public List<Persona> listarPersonas() {
        return personaDao.listarPersonas();
    }

    @Override
    public Persona buscarPersonaPorIdUsuario(Long idUsuario) {
        // TODO Auto-generated method stub
        return personaDao.buscarPersonaPorIdUsuario(idUsuario);
    }

}
