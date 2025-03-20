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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ControlSeguimiento.model.entity.Actividad;
import com.ControlSeguimiento.model.entity.ArchivoAdjunto;
import com.ControlSeguimiento.model.entity.Asignacion;
import com.ControlSeguimiento.model.entity.Avance;
import com.ControlSeguimiento.model.entity.Persona;
import com.ControlSeguimiento.model.entity.Usuario;
import com.ControlSeguimiento.model.service.ActividadService;
import com.ControlSeguimiento.model.service.ArchivoAdjuntoService;
import com.ControlSeguimiento.model.service.AsignacionService;
import com.ControlSeguimiento.model.service.AvanceService;
import com.ControlSeguimiento.model.service.PersonaService;
import com.ControlSeguimiento.model.service.UsuarioService;
import com.ControlSeguimiento.model.service.UtilidadesService;

import jakarta.servlet.http.HttpServletRequest;
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

    @Autowired
    private PersonaService personaService;

    @Autowired
    private ArchivoAdjuntoService archivoAdjuntoService;

    @Autowired
    private UtilidadesService utilidadesService;

    @Autowired
    private ActividadService actividadService;

    // @ValidarUsuarioAutenticado
    @PostMapping("/subVentana/{id}")
    public String inicio(HttpSession session, Model model, @PathVariable("id") Long IdAsignacion) {

        // model.addAttribute("opcionSeccionUsuarios", "true");
        // model.addAttribute("opcionPersona", "true");
        if (session.getAttribute("usuario") == null) {
            // La sesión ha expirado o no existe
            return "redirect:/form-login";
        }

        Asignacion asignacion = asignacionService.findById(IdAsignacion);
        model.addAttribute("asignacion", asignacion);
        model.addAttribute("actividad", actividadService.findById(asignacion.getActividad().getIdActividad()));
        return "avance/ventana";
    }

    @PostMapping("/formulario/{id}")
    public String formulario1(Model model, @PathVariable("id") Long IdAsignacion) {
        model.addAttribute("asignacion", asignacionService.findById(IdAsignacion));
        model.addAttribute("avance", new Avance());
        return "avance/formulario";
    }

    @PostMapping("/formularioEdit/{id}/{idAsignacion}")
    public String formularioEdit(Model model, @PathVariable("id") Long idAvance,
     @PathVariable("idAsignacion") Long IdAsignacion) {
        model.addAttribute("asignacion", asignacionService.findById(IdAsignacion));
        model.addAttribute("avance", avanceService.findById(idAvance));
        model.addAttribute("edit", "true");
        return "avance/formulario";
    }


    // @ValidarUsuarioAutenticado
    @PostMapping("/tablaRegistros/{id}")
    public String tablaRegistros(Model model, @PathVariable("id") Long IdAsignacion) {
        model.addAttribute("avances", avanceService.listarAvancePoridAsignacion(IdAsignacion));
        return "avance/tablaRegistros";
    }

    @PostMapping("/RegistrarAvance")
    public ResponseEntity<String> Registrar(@Validated Avance avance, HttpServletRequest request,
    @RequestParam("idAsignacion") Long IdAsignacion,
    @RequestParam(value = "filepond", required = false) MultipartFile[] adjuntos) {

        Usuario user = (Usuario) request.getSession().getAttribute("usuario");
        Persona persona = personaService.buscarPersonaPorIdUsuario(user.getIdUsuario());

        avance.setAsignacion(asignacionService.findById(IdAsignacion));
        avance.setRegistroIdUsuario(user.getIdUsuario());
        avance.setRegistro(new Date());
        avance.setEstado("ACTIVO");
        avanceService.save(avance);

        System.out.println("CANTIDAD FILES: " + adjuntos.length);
        for (MultipartFile multipartFile : adjuntos) {
            if (!multipartFile.isEmpty()) {
                ArchivoAdjunto archivo = new ArchivoAdjunto();
                archivo.setAvance(avance);
                archivo.setNombre(multipartFile.getOriginalFilename());
                archivo.setRuta(utilidadesService.guardarArchivo(multipartFile));
                archivo.setTipo(multipartFile.getContentType());
                archivo.setRegistroIdUsuario(user.getIdUsuario());
                archivo.setEstado("ACTIVO");
                archivo.setRegistro(new Date());
                archivoAdjuntoService.save(archivo);
            }
        }

        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    @PostMapping("/ModificarAvance")
    public ResponseEntity<String> Modificar(@Validated Avance a, HttpServletRequest request,
    @RequestParam("idAsignacion") Long IdAsignacion,
    @RequestParam(value = "filepond", required = false) MultipartFile[] adjuntos) {
        Avance avance = avanceService.findById(a.getIdAvance());
        Usuario user = (Usuario) request.getSession().getAttribute("usuario");

        avance.setObservacion(a.getObservacion());
        avance.setValorProgreso(a.getValorProgreso());
        avanceService.save(avance);

        System.out.println("CANTIDAD FILES: " + adjuntos.length);
        for (MultipartFile multipartFile : adjuntos) {
            if (!multipartFile.isEmpty()) {
                ArchivoAdjunto archivo = new ArchivoAdjunto();
                archivo.setAvance(avance);
                archivo.setNombre(multipartFile.getOriginalFilename());
                archivo.setRuta(utilidadesService.guardarArchivo(multipartFile));
                archivo.setTipo(multipartFile.getContentType());
                archivo.setRegistroIdUsuario(user.getIdUsuario());
                archivo.setEstado("ACTIVO");
                archivo.setRegistro(new Date());
                archivoAdjuntoService.save(archivo);
            }
        }

        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    @PostMapping("/verAdjuntos/{idAvance}")
    public String verAdjuntos(Model model, @PathVariable("idAvance") Long idAvance) {
        Avance avance = avanceService.findById(idAvance);
        model.addAttribute("avance", avance);
        model.addAttribute("archivos", archivoAdjuntoService.listarArchivosPorIdAvance(idAvance));
        return "avance/ventanaAdjuntos";
    }

        @PostMapping("/eliminar/{id}")
        public ResponseEntity<String> eliminar(Model model, @PathVariable("id") Long aId) {
        Avance avance = avanceService.findById(aId);
        avance.setEstado("ELIMINADO");
        avanceService.save(avance);
        return ResponseEntity.ok("Registro Eliminado");
    }

}
