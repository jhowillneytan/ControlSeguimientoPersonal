package com.ControlSeguimiento.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ControlSeguimiento.model.entity.Persona;
import com.ControlSeguimiento.model.service.PersonaService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/persona")
public class personaController {

    @Autowired
    private PersonaService personaService;

    //@ValidarUsuarioAutenticado
    @GetMapping("/ventana")
    public String inicio(HttpSession session, Model model) {

        model.addAttribute("opcionSeccionUsuarios", "true");
        model.addAttribute("opcionPersona", "true");
        if (session.getAttribute("usuario") == null) {
            // La sesión ha expirado o no existe
            return "redirect:/form-login";
        }

        return "persona/ventana";
    }

    //@ValidarUsuarioAutenticado
    @PostMapping("/tablaRegistros")
    public String tablaRegistros(Model model) {
        model.addAttribute("personas", personaService.listarPersonas());
        return "persona/tablaRegistros";
    }

    //@ValidarUsuarioAutenticado
    @PostMapping("/formulario")
    public String formulario1(Model model) {
        model.addAttribute("persona", new Persona());
        return "persona/formulario";
    }

    //@ValidarUsuarioAutenticado
    @PostMapping("/formulario/{id}")
    public String formulario2(Model model, @PathVariable("id") Long idPerson) {
        model.addAttribute("persona", personaService.findById(idPerson));
        model.addAttribute("edit", "true");
        return "persona/formulario";
    }

    //@ValidarUsuarioAutenticado
    @PostMapping("/RegistrarPersona")
    public ResponseEntity<String> RegistrarPersona(HttpServletRequest request, @Validated Persona persona) {
        if (personaService.buscarPorCi(persona.getCi()) == null) {
            persona.setEstado("ACTIVO");
            persona.setRegistro(new Date());
            personaService.save(persona);
            return ResponseEntity.ok("Se realizó el registro correctamente");
        } else {
            return ResponseEntity.ok("Ya existe un registro con este C.I.");
        }
    }

    //@ValidarUsuarioAutenticado
    @PostMapping("/ModificarPersona")
    public ResponseEntity<String> ModificarPersona(HttpServletRequest request, @Validated Persona persona) {
            Persona person = personaService.findById(persona.getIdPersona());
        if (personaService.compararCi(person.getCi(), persona.getCi()) == null) {
            person.setCi(persona.getCi());
            person.setCodFuncionario(persona.getCodFuncionario());
            person.setNombre(persona.getNombre());
            person.setPaterno(persona.getPaterno());
            person.setMaterno(persona.getMaterno());
            person.setCorreo(persona.getCorreo());
            person.setModificacion(new Date());
            personaService.save(person);
            return ResponseEntity.ok("Se realizó el registro correctamente");
        } else {
            return ResponseEntity.ok("Ya existe un registro con este C.I.");
        }
    }

    //@ValidarUsuarioAutenticado
    @PostMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(Model model, @PathVariable("id") Long idPerson) {
        Persona persona = personaService.findById(idPerson);
        persona.setEstado("ELIMINADO");
        personaService.save(persona);
        return ResponseEntity.ok("Registro Eliminado");
    }

}
