package com.ControlSeguimiento.config;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Setter
@Getter
public class AuditoriaConfig implements Serializable{
    private static final long serialVersionUID = 2629195288020321924L;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_registro")
    @CreatedDate
    private Date registro = new Timestamp(System.currentTimeMillis());

    @CreatedBy
    @Column(name = "registro_idUsuario")
    private Long registroIdUsuario;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_modificacion")
    @LastModifiedDate
    private Date modificacion = new Timestamp(System.currentTimeMillis());

    @CreatedBy
    @Column(name = "modificacion_idUsuario")
    private Long modificacionIdUsuario;

    @CreatedBy
    @Column(name = "estado")
    private String estado;
}