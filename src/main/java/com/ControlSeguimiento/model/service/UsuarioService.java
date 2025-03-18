package com.ControlSeguimiento.model.service;

import java.util.List;

import com.ControlSeguimiento.model.entity.Usuario;

public interface UsuarioService extends GenericoService <Usuario, Long>{
    List<Usuario> listarUsuarios();
    Usuario buscarPorNombreUser(String nombre);
    Usuario compararNombreUser(String nombreActual, String nombre);
    Usuario getUsuarioPassword(String usuario, String password);
}
