package com.ControlSeguimiento.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.ControlSeguimiento.model.entity.Proyecto;
import com.ControlSeguimiento.model.service.ProyectoService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/proyecto")
public class proyectoController {
    @Autowired
    private ProyectoService proyectoService;

    // @ValidarUsuarioAutenticado
    @GetMapping("/ventana")
    public String inicio(HttpSession session, Model model) {

        model.addAttribute("opcionSeccionActividades", "true");
        model.addAttribute("opcionProyecto", "true");
        if (session.getAttribute("usuario") == null) {
            // La sesión ha expirado o no existe
            return "redirect:/form-login";
        }

        return "proyecto/ventana";
    }

    // @ValidarUsuarioAutenticado
    @PostMapping("/tablaRegistros")
    public String tablaRegistros(Model model) {
        model.addAttribute("proyectos", proyectoService.listar());
        return "proyecto/tablaRegistros";
    }

    // @ValidarUsuarioAutenticado
    @PostMapping("/formulario")
    public String formulario1(Model model) {
        model.addAttribute("proyecto", new Proyecto());
        return "proyecto/formulario";
    }

    // @ValidarUsuarioAutenticado
    @PostMapping("/formulario/{id}")
    public String formulario2(Model model, @PathVariable("id") Long idPerson) {
        model.addAttribute("proyecto", proyectoService.findById(idPerson));
        model.addAttribute("edit", "true");
        return "proyecto/formulario";
    }

    // @ValidarUsuarioAutenticado
    @PostMapping("/RegistrarProyecto")
    public ResponseEntity<String> Registrarproyecto(HttpServletRequest request, @Validated Proyecto proyecto,
    @RequestParam(value = "fechaInicio")String fechaInicio, @RequestParam(value = "fechaFin")String fechaFin) {
        try {
            proyecto.setFechaInicio(convertirFecha(fechaInicio));
            proyecto.setFechaFin(convertirFecha(fechaFin)); // Establecer la fecha de fin convertida
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // Establecer la fecha de inicio convertida
        proyecto.setProgreso(0.0);
        proyecto.setEstado("ACTIVO");
        proyecto.setRegistro(new Date());
        proyectoService.save(proyecto);
        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    // @ValidarUsuarioAutenticado
    @PostMapping("/ModificarProyecto")
    public ResponseEntity<String> Modificarproyecto(HttpServletRequest request, @Validated Proyecto p,
    @RequestParam(value = "fechaInicio")String fechaInicio, @RequestParam(value = "fechaFin")String fechaFin) {
        Proyecto proyecto = proyectoService.findById(p.getIdProyecto());
        proyecto.setNombre(p.getNombre());
        proyecto.setDescripcion(p.getDescripcion());
        try {
            proyecto.setFechaInicio(convertirFecha(fechaInicio));
            proyecto.setFechaFin(convertirFecha(fechaFin)); // Establecer la fecha de fin convertida
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // Establecer la fecha de inicio convertida
        proyectoService.save(proyecto);
        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    // @ValidarUsuarioAutenticado
    @PostMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(Model model, @PathVariable("id") Long idPerson) {
        Proyecto proyecto = proyectoService.findById(idPerson);
        proyecto.setEstado("ELIMINADO");
        proyectoService.save(proyecto);
        return ResponseEntity.ok("Registro Eliminado");
    }

        public static Date convertirFecha(String fechaStr) throws ParseException {
        // Definir el formato de fecha original
        SimpleDateFormat formatoOriginal = new SimpleDateFormat("dd/MM/yyyy");
        
        // Parsear la fecha del String al objeto Date
        Date fecha = formatoOriginal.parse(fechaStr);
        
        return fecha;
    }


}
