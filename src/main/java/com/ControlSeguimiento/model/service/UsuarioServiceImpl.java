package com.ControlSeguimiento.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ControlSeguimiento.model.dao.UsuarioDao;
import com.ControlSeguimiento.model.entity.Usuario;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    @Autowired
    private UsuarioDao usuarioDao;

    @Override
    public List<Usuario> findAll() {
        return usuarioDao.findAll();
    }

    @Override
    public void save(Usuario usuario) {
        usuarioDao.save(usuario);
    }

    @Override
    public Usuario findById(Long id) {
        return usuarioDao.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        Usuario usuario = usuarioDao.findById(id).orElse(null);
        usuario.setEstado("ELIMINADO");
        usuarioDao.save(usuario);
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioDao.listarUsuarios();
    }

    @Override
    public Usuario buscarPorNombreUser(String nombre) {
        return usuarioDao.buscarPorNombreUser(nombre);
    }

    @Override
    public Usuario compararNombreUser(String nombreActual, String nombre) {
        return usuarioDao.compararNombreUser(nombreActual, nombre);
    }

    @Override
    public Usuario getUsuarioPassword(String usuario, String password) {
        return usuarioDao.getUsuarioPassword(usuario, password);
    }

    @Override
    public Usuario buscarPorIdPersona(Long idPersona) {
        // TODO Auto-generated method stub
        return usuarioDao.buscarPorIdPersona(idPersona);
    }
    
}
