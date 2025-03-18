package com.ControlSeguimiento.model.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ControlSeguimiento.config.AuditoriaConfig;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuario")
@Setter
@Getter
public class Usuario extends AuditoriaConfig {
    private static final long serialVersionUID = 2629195288020321924L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;
    private String nombre;
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona")
    private Persona persona;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rol_usuario", 
    joinColumns = @JoinColumn(name = "id_usuario"), 
    inverseJoinColumns = @JoinColumn(name = "id_rol"))
    private Set<Rol> roles;
}
