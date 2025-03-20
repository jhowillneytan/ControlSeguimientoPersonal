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
import com.ControlSeguimiento.model.entity.Proyecto;
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
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/actividad")
public class actividadController {

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
    private EmailServiceImpl emailService;

    // @ValidarUsuarioAutenticado
    @GetMapping("/ventana")
    public String inicio(HttpSession session, Model model) {

        model.addAttribute("opcionSeccionActividades", "true");
        model.addAttribute("opcionActividades", "true");

        if (session.getAttribute("usuario") == null) {
            // La sesión ha expirado o no existe
            return "redirect:/form-login";
        }

        return "actividad/ventana";
    }

    // @ValidarUsuarioAutenticado
    @PostMapping("/tablaRegistros")
    public String tablaRegistros(Model model) {
        model.addAttribute("actividades", actividadService.listarActividades());
        return "actividad/tablaRegistros";
    }

    // @ValidarUsuarioAutenticado
    @PostMapping("/formulario")
    public String formulario1(Model model) {
        model.addAttribute("actividad", new Actividad());
        model.addAttribute("prioridades", prioridadService.findAll());
        model.addAttribute("proyectos", proyectoService.listar());
        return "actividad/formulario";
    }

    // @ValidarUsuarioAutenticado
    @PostMapping("/formulario/{id}")
    public String formulario2(Model model, @PathVariable("id") Long idPerson) {
        model.addAttribute("actividad", actividadService.findById(idPerson));
        model.addAttribute("prioridades", prioridadService.findAll());
        model.addAttribute("proyectos", proyectoService.listar());
        model.addAttribute("edit", "true");
        return "actividad/formulario";
    }

    @PostMapping("/ventanaAsignaciones/{id}")
    public String ventanaAsignaciones(Model model, @PathVariable("id") Long id) {
        model.addAttribute("actividad", actividadService.findById(id));
        model.addAttribute("personas", personaService.findAll());
        return "actividad/asignaciones";
    }

    // @ValidarUsuarioAutenticado
    @PostMapping("/Registrar")
public ResponseEntity<String> RegistrarPersona(HttpServletRequest request, @Validated Actividad actividad, HttpSession session,
@RequestParam(value = "fechaInicio")String fechaInicio, @RequestParam(value = "fechaFin")String fechaFin) {
    try {
        // Establecer estado de la actividad
        actividad.setEstado("EN PROCESO");
        actividad.setProgreso(0.0);
        // Establecer las fechas en la entidad de actividad
        actividad.setFechaInicio(convertirFecha(fechaInicio)); // Establecer la fecha de inicio convertida
        actividad.setFechaFin(convertirFecha(fechaFin)); // Establecer la fecha de fin convertida
        
        // Establecer la fecha de registro como la fecha actual
        actividad.setRegistro(new Date());
        
        Usuario u = (Usuario)session.getAttribute("usuario");
        //Usuario usuario = usuarioService.findById(u.getIdUsuario());
        actividad.setRegistroIdUsuario(u.getIdUsuario());

        // Guardar la actividad
        actividadService.save(actividad);
        
        return ResponseEntity.ok("Se realizó el registro correctamente");
    } catch (ParseException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en el formato de fecha: " + e.getMessage());
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en el registro: " + e.getMessage());
    }
}


    // @ValidarUsuarioAutenticado
    @PostMapping("/Modificar")
    public ResponseEntity<String> ModificarPersona(HttpServletRequest request, @Validated Actividad a,
    @RequestParam(value = "fechaInicio")String fechaInicio, @RequestParam(value = "fechaFin")String fechaFin) {
        Actividad actividad = actividadService.findById(a.getIdActividad());
        try {
            actividad.setFechaInicio(convertirFecha(fechaInicio));
            actividad.setFechaFin(convertirFecha(fechaFin)); // Establecer la fecha de fin convertida
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // Establecer la fecha de inicio convertida
        actividad.setTitulo(a.getTitulo());
        actividad.setDescripcion(a.getDescripcion());
        actividad.setPrioridad(a.getPrioridad());
        actividad.setProgreso(a.getProgreso());
        actividadService.save(actividad);
        return ResponseEntity.ok("Se realizó el registro correctamente");
    }

    @PostMapping("/GuardarAsignaciones")
public ResponseEntity<String> GuardarAsignaciones(HttpServletRequest request, 
        @RequestParam(value = "idActividad") Long idActividad, HttpSession session,
        @RequestParam(value = "asignados", required = false) Long[] idPersonas) {
    
    try {
        Usuario u = (Usuario)session.getAttribute("usuario");
        // Obtener la actividad por su ID
        Actividad actividad = actividadService.findById(idActividad);
        
        if (actividad == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la actividad");
        }
        
        // Limpiar asignaciones existentes
        // Primero eliminar las asignaciones actuales de la base de datos
        List<Asignacion> asignacionesActuales = asignacionService.findByActividad(actividad);
        for (Asignacion asig : asignacionesActuales) {
            asignacionService.deleteById(asig.getIdAsignacion());
        }
        
        // Limpiar la lista en la entidad
        actividad.getAsignaciones().clear();
        
        // Crear nuevas asignaciones si hay personas seleccionadas
        if (idPersonas != null && idPersonas.length > 0) {
            for (Long idPersona : idPersonas) {
                Persona persona = personaService.findById(idPersona);
                String mensaje = """
                        <h1>Asignación de Actividad</h1>
                        <p>Estimado Señor/a %s Se le ha asignado una nueva actividad sobre %s.</p>
                        <p>Por favor, ingrese al sistema para revisar la actividad asignada con mas detalle.</p>
                """;
                mensaje = String.format(mensaje, persona.getNombreCompleto(), actividad.getDescripcion());
                
				emailService.enviarEmail(persona.getCorreo(),
						"Informaciones RRHH: " + persona.getNombreCompleto(), mensaje);

                Asignacion asignacion = new Asignacion();
                asignacion.setEstado("ACTIVO");
                asignacion.setRegistro(new Date());
                asignacion.setPersona(persona);
                asignacion.setActividad(actividad);
                asignacion.setRegistroIdUsuario(u.getIdUsuario());
                // Guardar la asignación
                asignacionService.save(asignacion);
            }
        }
        
        return ResponseEntity.ok("Se realizaron las asignaciones correctamente");
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al guardar las asignaciones: " + e.getMessage());
    }
}

    // @ValidarUsuarioAutenticado
    @PostMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(Model model, @PathVariable("id") Long aId) {
        Actividad actividad = actividadService.findById(aId);
        actividad.setEstado("ELIMINADO");
        actividadService.save(actividad);
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
