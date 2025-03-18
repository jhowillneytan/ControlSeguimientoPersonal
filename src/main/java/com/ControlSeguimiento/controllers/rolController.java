package com.ControlSeguimiento.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ControlSeguimiento.model.service.RolService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/rol")
@RequiredArgsConstructor
public class rolController {

    private final RolService rolService;

    //@ValidarUsuarioAutenticado
    @GetMapping("/ventana")
    public String inicio() {
        return "rol/ventana";
    }

    //@ValidarUsuarioAutenticado
    @PostMapping("/tablaRegistros")
    public String tablaRegistros(Model model) {
        model.addAttribute("personas", rolService.findAll());
        return "persona/tablaRegistros";
    }
}
