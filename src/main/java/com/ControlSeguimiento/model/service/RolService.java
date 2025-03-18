package com.ControlSeguimiento.model.service;

import com.ControlSeguimiento.model.entity.Rol;

public interface RolService extends GenericoService <Rol, Long>{
    Rol buscaPorNombre(String nombre);
}
