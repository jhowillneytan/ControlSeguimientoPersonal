package com.ControlSeguimiento.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ControlSeguimiento.model.entity.Rol;

public interface RolDao extends JpaRepository<Rol, Long>{
    Rol findByNombre (String nombre);
}
