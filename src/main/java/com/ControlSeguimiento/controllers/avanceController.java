package com.ControlSeguimiento.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        Actividad actividad = actividadService.findById(asignacion.getActividad().getIdActividad());

        // double total = 0.0;
        // //List<Avance> avances = avanceService.listarAvancePoridAsignacion(IdAsignacion);
        // for (Asignacion as : actividad.getAsignaciones()) {
        //     List<Avance> avances = avanceService.listarAvancePoridAsignacion(as.getIdAsignacion());
        //     for (Avance av : avances) {
        //         total = total + av.getValorProgreso();
        //     }
        // }

        model.addAttribute("asignacion", asignacion);
        model.addAttribute("actividad", actividad);
        //model.addAttribute("totalProgreso", total);
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

        Asignacion asignacion = asignacionService.findById(IdAsignacion);
        Actividad actividad = actividadService.findById(asignacion.getActividad().getIdActividad());
        double total = actividad.getProgreso();
        //List<Avance> avances = avanceService.listarAvancePoridAsignacion(IdAsignacion);
        // for (Asignacion as : actividad.getAsignacionesOrdenadas()) {
        //     List<Avance> avances = avanceService.listarAvancePoridAsignacion(as.getIdAsignacion());
        //     for (Avance av : avances) {
        //         total = total + av.getValorProgreso();
        //     }
        // }
        total = total + avance.getValorProgreso();
        
        if (total > 100.0) {
            return ResponseEntity.ok("El total valor del % del avance supera el 100%");
        }

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

        
        actividad.setProgreso(total);
        actividadService.save(actividad);

        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    @PostMapping("/ModificarAvance")
    public ResponseEntity<String> Modificar(@Validated Avance a, HttpServletRequest request,
    @RequestParam("idAsignacion") Long IdAsignacion,
    @RequestParam(value = "filepond", required = false) MultipartFile[] adjuntos) {
        Avance avance = avanceService.findById(a.getIdAvance());
        Asignacion asignacion = asignacionService.findById(IdAsignacion);
        Actividad actividad = actividadService.findById(asignacion.getActividad().getIdActividad());
        double total = actividad.getProgreso();
        //List<Avance> avances = avanceService.listarAvancePoridAsignacion(IdAsignacion);
        // for (Asignacion as : actividad.getAsignacionesOrdenadas()) {
        //     List<Avance> avances = avanceService.listarAvancePoridAsignacion(as.getIdAsignacion());
        //     for (Avance av : avances) {
        //         total = total + av.getValorProgreso();
        //     }
        // }
        total = total + avance.getValorProgreso();
        
        if (total > 100.0) {
            return ResponseEntity.ok("El total valor del % del avance supera el 100%");
        }


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
        
        actividad.setProgreso(total);
        actividadService.save(actividad);

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

        Actividad actividad = actividadService.findById(avance.getAsignacion().getActividad().getIdActividad());
        double total = actividad.getProgreso();
        total = total - avance.getValorProgreso();
        actividad.setProgreso(total);
        return ResponseEntity.ok("Registro Eliminado");
    }


    // ===================== SECCION PARA EVALUADORES =====================
    @PostMapping("/subVentanaEvaluador/{id}")
    public String subVentanaEvaluador(HttpSession session, Model model, @PathVariable("id") Long idActividad) {

        if (session.getAttribute("usuario") == null) {
            // La sesión ha expirado o no existe
            return "redirect:/form-login";
        }

        Actividad actividad = actividadService.findById(idActividad);

        model.addAttribute("actividad", actividad);
        return "avance/evaluacion/ventana";
    }

    @PostMapping("/tablaRegistrosEvaluador/{id}")
    public String tablaRegistrosEvaluador(Model model, @PathVariable("id") Long idActividad) {
        List<Avance> avances = new ArrayList<>();
        Actividad actividad = actividadService.findById(idActividad);
        for (Asignacion asignacion : actividad.getAsignacionesOrdenadas()) {
            for (Avance avance : asignacion.getAvancesOrdenadas()) {
                avances.add(avance);
            }
        }
        model.addAttribute("actividad", actividad);
        model.addAttribute("avances", avances);
        return "avance/evaluacion/tablaRegistros";
    }

    @PostMapping("/verAdjuntosEvaluador/{idAvance}")
    public String verAdjuntosEvaluador(Model model, @PathVariable("idAvance") Long idAvance) {
        Avance avance = avanceService.findById(idAvance);
        model.addAttribute("avance", avance);
        model.addAttribute("archivos", archivoAdjuntoService.listarArchivosPorIdAvance(idAvance));
        return "avance/evaluacion/ventanaAdjuntos";
    }

    @PostMapping("/validarAvances/{id}")
    public ResponseEntity<String> aprobarAvances(Model model, @PathVariable("id") Long aId) {
        Actividad actividad = actividadService.findById(aId);
        for (Asignacion asignacion : actividad.getAsignacionesOrdenadas()) {
            for (Avance avance : asignacion.getAvancesOrdenadas()) {
                avance.setEstado("VALIDO");
                avanceService.save(avance);
            }
        }
        if (actividad.getFechaFin().before(new Date())) {
            actividad.setEstado("FINALIZADO_VALIDADO_RETRASO");
        } else {
            actividad.setEstado("FINALIZADO_VALIDADO");
            
        }
        actividadService.save(actividad);
        return ResponseEntity.ok("Actividad Finalizada");
    }

    @PostMapping("/rechazarAvances/{id}")
    public ResponseEntity<String> rechazarAvances(Model model, @PathVariable("id") Long aId) {
        Actividad actividad = actividadService.findById(aId);
        for (Asignacion asignacion : actividad.getAsignacionesOrdenadas()) {
            for (Avance avance : asignacion.getAvancesOrdenadas()) {
                avance.setEstado("RECHAZADO");
                avanceService.save(avance);
            }
        }
        if (actividad.getFechaFin().before(new Date())) {
            actividad.setEstado("FINALIZADO_RECHAZADO_RETRASO");
        } else {
            actividad.setEstado("FINALIZADO_RECHAZADO");
            
        }
        actividadService.save(actividad);
        return ResponseEntity.ok("Actividad Finalizada");
    }

}
