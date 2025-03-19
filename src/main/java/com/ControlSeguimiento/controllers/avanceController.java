package com.ControlSeguimiento.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ControlSeguimiento.model.entity.Asignacion;
import com.ControlSeguimiento.model.entity.Avance;
import com.ControlSeguimiento.model.entity.Persona;
import com.ControlSeguimiento.model.entity.Usuario;
import com.ControlSeguimiento.model.service.AsignacionService;
import com.ControlSeguimiento.model.service.AvanceService;
import com.ControlSeguimiento.model.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/avance")
public class avanceController {

    @Autowired
    private AvanceService avanceService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AsignacionService asignacionService;

    // @ValidarUsuarioAutenticado
    @GetMapping("/subVentana")
    public String inicio(HttpSession session, Model model, @PathVariable("id") Long IdAsignacion) {

        // model.addAttribute("opcionSeccionUsuarios", "true");
        // model.addAttribute("opcionPersona", "true");
        if (session.getAttribute("usuario") == null) {
            // La sesión ha expirado o no existe
            return "redirect:/form-login";
        }

        Asignacion asignacion = asignacionService.findById(IdAsignacion);
        model.addAttribute("asignacion", asignacion);
        return "avance/ventana";
    }

    @PostMapping("/formulario")
    public String formulario1(Model model) {
        model.addAttribute("avance", new Avance());
        return "avance/formulario";
    }

    // @ValidarUsuarioAutenticado
    @PostMapping("/tablaRegistros/{id}")
    public String tablaRegistros(Model model, HttpSession session, @PathVariable("id") Long IdAsignacion) {
        model.addAttribute("avances", avanceService.listarAvancePorIdAsignacion(IdAsignacion));
        return "avance/tablaRegistros";
    }

}
