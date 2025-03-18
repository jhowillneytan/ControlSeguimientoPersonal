package com.ControlSeguimiento.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ControlSeguimiento.model.entity.Usuario;

public interface UsuarioDao extends JpaRepository<Usuario, Long>{
    
    @Query("SELECT u FROM Usuario u WHERE u.estado != 'ELIMINADO'")
    List<Usuario> listarUsuarios();

    @Query("SELECT u FROM Usuario u WHERE u.nombre = ?1  AND u.estado != 'ELIMINADO'")
    Usuario buscarPorNombreUser(String nombre);

    @Query("SELECT u FROM Usuario u WHERE u.nombre != ?1 AND u.nombre = ?2 AND u.estado != 'ELIMINADO'")
    Usuario compararNombreUser(String nombreActual, String nombre);

    @Query(value = "select * from usuario u where u.nombre = ?1 and u.password = ?2", nativeQuery = true)
    Usuario getUsuarioPassword(String usuario, String password);
}
