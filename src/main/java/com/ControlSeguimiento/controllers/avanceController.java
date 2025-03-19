package com.ControlSeguimiento.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ControlSeguimiento.model.entity.Usuario;
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


    //@ValidarUsuarioAutenticado
    @GetMapping("/subventana")
    public String inicio(HttpSession session, Model model) {

        //model.addAttribute("opcionSeccionUsuarios", "true");
        //model.addAttribute("opcionPersona", "true");
        if (session.getAttribute("usuario") == null) {
            // La sesión ha expirado o no existe
            return "redirect:/form-login";
        }

        return "persona/ventana";
    }

        // @ValidarUsuarioAutenticado
    @PostMapping("/tablaRegistros")
    public String tablaRegistros(Model model, HttpSession session) {
        Usuario u = (Usuario)session.getAttribute("usuario");
        Usuario usuario = usuarioService.findById(u.getIdUsuario());
        model.addAttribute("avance", avanceService.listarAvancePorIdAsignacion(null));
        return "asignacion/tablaRegistros";
    }

}
