package com.ControlSeguimiento.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ControlSeguimiento.model.entity.Actividad;
import com.ControlSeguimiento.model.service.ActividadService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/actividad")
public class actividadController {

    @Autowired
    private ActividadService actividadService;

    // @ValidarUsuarioAutenticado
    @GetMapping("/ventana")
    public String inicio() {
        return "actividad/ventana";
    }

    // @ValidarUsuarioAutenticado
    @PostMapping("/tablaRegistros")
    public String tablaRegistros(Model model) {
        model.addAttribute("actividades", actividadService.findAll());
        return "actividad/tablaRegistros";
    }

    // @ValidarUsuarioAutenticado
    @PostMapping("/formulario")
    public String formulario1(Model model) {
        model.addAttribute("actividad", new Actividad());
        return "actividad/formulario";
    }

    // @ValidarUsuarioAutenticado
    @PostMapping("/formulario/{id}")
    public String formulario2(Model model, @PathVariable("id") Long idPerson) {
        model.addAttribute("actividad", actividadService.findById(idPerson));
        model.addAttribute("edit", "true");
        return "actividad/formulario";
    }

    // @ValidarUsuarioAutenticado
    @PostMapping("/Registrar")
public ResponseEntity<String> RegistrarPersona(HttpServletRequest request, @Validated Actividad actividad) {
    try {
        actividad.setEstado("ACTIVO");
        
        //SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        
        // Establecer fecha de registro como la fecha actual
        actividad.setRegistro(new Date());
        
        // Guardar la actividad
        actividadService.save(actividad);
        
        return ResponseEntity.ok("Se realizó el registro correctamente");
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en el registro: " + e.getMessage());
    }
}

    // @ValidarUsuarioAutenticado
    @PostMapping("/Modificar")
    public ResponseEntity<String> ModificarPersona(HttpServletRequest request, @Validated Actividad a) {
        Actividad actividad = actividadService.findById(a.getIdActividad());
        actividad.setFechaInicio(a.getFechaInicio());
        actividad.setFechaFin(a.getFechaFin());
        actividad.setDescripcion(a.getDescripcion());
        actividadService.save(actividad);
        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    // @ValidarUsuarioAutenticado
    @PostMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(Model model, @PathVariable("id") Long aId) {
        Actividad actividad = actividadService.findById(aId);
        actividad.setEstado("ELIMINADO");
        actividadService.save(actividad);
        return ResponseEntity.ok("Registro Eliminado");
    }

}
