package com.ControlSeguimiento.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.ControlSeguimiento.model.entity.Actividad;
import com.ControlSeguimiento.model.entity.Asignacion;
import com.ControlSeguimiento.model.entity.Persona;
import com.ControlSeguimiento.model.entity.Usuario;
import com.ControlSeguimiento.model.service.ActividadService;
import com.ControlSeguimiento.model.service.AsignacionService;
import com.ControlSeguimiento.model.service.EmailServiceImpl;
import com.ControlSeguimiento.model.service.PersonaService;
import com.ControlSeguimiento.model.service.PrioridadService;
import com.ControlSeguimiento.model.service.ProyectoService;
import com.ControlSeguimiento.model.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/asignacion")
public class asignacionController {
    @Autowired
    private ActividadService actividadService;

    @Autowired
    private AsignacionService asignacionService;

    @Autowired
    private UsuarioService usuarioService;

        @Autowired
    private PersonaService personaService;

    @Autowired
    private PrioridadService prioridadService;

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private EmailServiceImpl emailServiceImpl;

    // @ValidarUsuarioAutenticado
    @GetMapping("/ventana")
    public String inicio(HttpSession session, Model model) {

        model.addAttribute("opcionSeccionActividades", "true");
        model.addAttribute("opcionAsignaciones", "true");

        if (session.getAttribute("usuario") == null) {
            // La sesión ha expirado o no existe
            return "redirect:/form-login";
        }

        return "asignacion/ventana";
    }

    // @ValidarUsuarioAutenticado
    @PostMapping("/tablaRegistros")
    public String tablaRegistros(Model model, HttpSession session) {
        Usuario u = (Usuario)session.getAttribute("usuario");
        Usuario usuario = usuarioService.findById(u.getIdUsuario());
        model.addAttribute("asignaciones", asignacionService.listarAsignacionesPorIdPersona(usuario.getPersona().getIdPersona()));
        return "asignacion/tablaRegistros";
    }

    public static Date convertirFecha(String fechaStr) throws ParseException {
        // Definir el formato de fecha original
        SimpleDateFormat formatoOriginal = new SimpleDateFormat("dd/MM/yyyy");
        
        // Parsear la fecha del String al objeto Date
        Date fecha = formatoOriginal.parse(fechaStr);
        
        return fecha;
    }

}
