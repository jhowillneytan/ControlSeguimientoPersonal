package com.ControlSeguimiento.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import jakarta.mail.MessagingException;
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
    public ResponseEntity<String> RegistrarPersona(HttpServletRequest request, @Validated Actividad actividad,
            HttpSession session,
            @RequestParam(value = "fechaInicio") String fechaInicio,
            @RequestParam(value = "fechaFin") String fechaFin) {
        try {
            // Establecer estado de la actividad
            actividad.setEstado("EN PROCESO");
            actividad.setProgreso(0.0);
            // Establecer las fechas en la entidad de actividad
            actividad.setFechaInicio(convertirFecha(fechaInicio)); // Establecer la fecha de inicio convertida
            actividad.setFechaFin(convertirFecha(fechaFin)); // Establecer la fecha de fin convertida

            // Establecer la fecha de registro como la fecha actual
            actividad.setRegistro(new Date());

            Usuario u = (Usuario) session.getAttribute("usuario");
            // Usuario usuario = usuarioService.findById(u.getIdUsuario());
            actividad.setRegistroIdUsuario(u.getIdUsuario());

            // Guardar la actividad
            actividadService.save(actividad);

            return ResponseEntity.ok("Se realizó el registro correctamente");
        } catch (ParseException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error en el formato de fecha: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en el registro: " + e.getMessage());
        }
    }

    // @ValidarUsuarioAutenticado
    @PostMapping("/Modificar")
    public ResponseEntity<String> ModificarPersona(HttpServletRequest request, @Validated Actividad a,
            @RequestParam(value = "fechaInicio") String fechaInicio,
            @RequestParam(value = "fechaFin") String fechaFin) {
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

    // @PostMapping("/GuardarAsignaciones")
    // public ResponseEntity<String> GuardarAsignaciones(HttpServletRequest request,
    //         @RequestParam(value = "idActividad") Long idActividad, HttpSession session,
    //         @RequestParam(value = "asignados", required = false) Long[] idPersonas) {

    //     try {

    //         Actividad actividad = actividadService.findById(idActividad);
    //         List<Asignacion> asignacionesActuales = asignacionService.findByActividad(actividad);
    //         if (idPersonas == null || idPersonas.length == 0) {

    //             for (Asignacion asignacion : asignacionesActuales) {
    //                 asignacion.setEstado("ELIMINADO");
    //                 asignacionService.save(asignacion);
    //             }

    //             return ResponseEntity.ok("Se realizaron las asignaciones correctamente");
                
    //         }

    //         Usuario u = (Usuario) session.getAttribute("usuario");
    //         // Obtener la actividad por su ID
            

    //         if (actividad == null) {
    //             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la actividad");
    //         }

    //         Long[] idPersonasAuxiliar = idPersonas;

            
    //         for (Long idPersona : idPersonas) {
    //             for (Asignacion asig : asignacionesActuales) {
    //                 if (asig.getPersona().getIdPersona() == idPersona) {
    //                     idPersonas = null;
    //                 }
    //             }
    //         }

    //         List<Asignacion> asignacionesElimnar = new ArrayList<>();
            
    //         for (Asignacion asig : asignacionesActuales) {
    //             for (Long idPersona : idPersonasAuxiliar) {
    //                 if (asig.getPersona().getIdPersona() == idPersona) {
    //                     idPersona = null;
    //                     break;
    //                 } else {
    //                     asignacionesElimnar.add(asig);
    //                 }
    //             }
    //         }
            
    //         for (Asignacion asignacion : asignacionesElimnar) {
    //             System.out.println("Eliminando Persona: " + asignacion.getPersona().getNombreCompleto());
    //         }

    //         // Limpiar la lista en la entidad
    //         //actividad.getAsignaciones().clear();

    //         // Crear nuevas asignaciones si hay personas seleccionadas
    //         if (idPersonas != null && idPersonas.length > 0) {
    //             for (Long idPersona : idPersonas) {
    //                 Persona persona = personaService.findById(idPersona);

    //                 String fechaInicioFormateada = "";
    //                 String fechaFinFormateada = "";

    //                 try {
    //                     // Crear formatos para parsear y formatear
    //                     SimpleDateFormat formatoOriginal = new SimpleDateFormat("yyyy-MM-dd");
    //                     SimpleDateFormat formatoDeseado = new SimpleDateFormat("dd/MM/yyyy");

    //                     // Parsear las fechas de la actividad a objetos Date
    //                     Date fechaInicio = formatoOriginal.parse(actividad.getFechaInicio().toString());
    //                     Date fechaFin = formatoOriginal.parse(actividad.getFechaFin().toString());

    //                     // Formatear las fechas al formato deseado
    //                     fechaInicioFormateada = formatoDeseado.format(fechaInicio);
    //                     fechaFinFormateada = formatoDeseado.format(fechaFin);

    //                 } catch (ParseException e) {
    //                     // Manejar posibles errores
    //                     e.printStackTrace();
    //                     fechaInicioFormateada = actividad.getFechaInicio().toString(); // Mantener formato original en caso de
    //                                                                         // error
    //                     fechaFinFormateada = actividad.getFechaFin().toString();
    //                 }

    //                 String mensaje = """
    //                         <!DOCTYPE html>
    //                             <html lang="es">
    //                             <head>
    //                                 <meta charset="UTF-8">
    //                                 <meta name="viewport" content="width=device-width, initial-scale=1.0">
    //                                 <title>Universidad Amazónica de Pando</title>
    //                             </head>
    //                             <body style="font-family: Arial, sans-serif; background-color: #dee2e6; padding: 0; margin: 0;">
    //                                 <table width="100%" height="100%" cellpadding="0" cellspacing="0" border="0" style="background-color: #e9ecef; margin-top: 50px;" >
    //                                     <tr>
    //                                         <td align="center" valign="top">
    //                                             <table cellpadding="0" cellspacing="0" border="0" style="background-color: #ffffff; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); max-width: 400px; width: 100%; padding: 20px; box-sizing: border-box;">
    //                                                 <tr>
    //                                                     <td>
    //                                                         <table width="100%" cellpadding="0" cellspacing="0" border="0">
    //                                                             <tr>
    //                                                                 <td align="center" style="background-color: #ef233c; color: white; text-align: center; padding: 5px 0; margin-bottom: 50px;">
    //                                                                     <h1 style="font-size: 12px; margin: 0;">Universidad Amazónica de Pando</h1>
    //                                                                     <p style="font-size: 10px; margin: 0;">La preservación de la Amazonía es parte de la subsistencia de la vida, del progreso y desarrollo de la bella tierra Pandina</p>
    //                                                                 </td>
    //                                                             </tr>
    //                                                             <tr>
    //                                                                 <td>
    //                                                                     <p style="font-size: 11px; color: #131313;"><em>Señor(a)</em></p>
    //                                                                     <p style="font-size: 11px; color: #131313;"><em>%s</em></p>
    //                                                                     <p style="text-align: justify; color: #131313;">La Unidad de Gestión Recursos Humanos de la Universidad Amazónica de Pando informa:</p>
    //                                                                     <h2 style="font-size: 14px; color: #333; text-align: center; margin: 0 0 10px;">LA ASIGNACIÓN DE SU ACTIVIDAD A REALIZAR:</h2>
    //                                                                 </td>
    //                                                             </tr>
    //                                                             <tr>
    //                                                                 <td style="background-color: #e0f7fa; padding: 15px; border-radius: 5px; text-align: justify; margin-bottom: 20px;">
    //                                                                     <p style="margin: 5px 0; color: #131313;"><b>DETALLES: </b></p>
    //                                                                     <p style="margin: 5px 0; color: #131313;"><b>Título: </b><span>%s</span></p>
    //                                                                     <p style="margin: 5px 0; color: #131313;"><b>Descripción: </b><span>%s</span></p>
    //                                                                     <p style="margin: 5px 0; color: #131313;"><b>Fecha Inicio: </b><span>%s</span></p>
    //                                                                     <p style="margin: 5px 0; color: #131313;"><b>Fecha Fin: </b><span>%s</span></p>
    //                                                                 </td>
    //                                                             </tr>
    //                                                             <tr>
    //                                                                 <td align="center">
    //                                                                     <p style="color: #131313;">La actividad tiene una prioridad: %s</p>
    //                                                                 </td>
    //                                                             </tr>
    //                                                             <tr>
    //                                                                 <td align="center">
    //                                                                     <p style="color: #131313;">Enlace al Sistema: <a href="http://virtual.uap.edu.bo:8083/" style="color: #007bff; text-decoration: none;">http://virtual.uap.edu.bo:8083/</a></p>
    //                                                                 </td>
    //                                                             </tr>
    //                                                             <tr>
    //                                                                 <td align="center">
    //                                                                     <p><b style="font-size: 12px; color: #333;">UNIDAD DE GESTIÓN DE RECURSOS HUMANOS</b></p>
    //                                                                     <p style="font-size: 12px;">Cobija, Pando</p>
    //                                                                     <p style="font-size: 11px;"><small>Este correo fue enviado por un sistema automatizado por lo cual no debe responder al mismo.</small></p>
    //                                                                 </td>
    //                                                             </tr>
    //                                                             <tr>
    //                                                                 <td align="center" style="background-color: #003566; color: white; padding: 5px 0; margin-top: 50px;">
    //                                                                     <h1 style="font-size: 12px; margin: 0;">RRHH 2025 | Universidad Amazónica de Pando</h1>
    //                                                                     <p style="font-size: 10px; margin: 0;">Escribiendo una nueva historia con vos</p>
    //                                                                 </td>
    //                                                             </tr>
    //                                                         </table>
    //                                                     </td>
    //                                                 </tr>
    //                                             </table>
    //                                         </td>
    //                                     </tr>
    //                                 </table>
    //                             </body>
    //                             </html>
    //                 """;
    //                         // Escapar cualquier % que no sea parte del formato reemplazándolo por %%
    //         String mensajeProcesado = mensaje.replace("%", "%%")
    //         .replace("%%s", "%s");
    //         String mensajeFormateado = String.format(mensajeProcesado,
    //         persona.getNombreCompleto(),
    //         actividad.getTitulo(),
    //         actividad.getDescripcion(),
    //         fechaInicioFormateada,
    //         fechaFinFormateada,
    //         actividad.getPrioridad() != null ? actividad.getPrioridad().getNivel() : "No especificada");
    //                 emailService.enviarEmail(persona.getCorreo(),
    //                         "Informaciones RRHH: " + persona.getNombreCompleto(), mensajeFormateado);

    //                 Asignacion asignacion = new Asignacion();
    //                 asignacion.setEstado("ACTIVO");
    //                 asignacion.setRegistro(new Date());
    //                 asignacion.setPersona(persona);
    //                 asignacion.setActividad(actividad);
    //                 asignacion.setRegistroIdUsuario(u.getIdUsuario());
    //                 // Guardar la asignación
    //                 asignacionService.save(asignacion);
    //             }
    //         }

    //         return ResponseEntity.ok("Se realizaron las asignaciones correctamente");
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //                 .body("Error al guardar las asignaciones: " + e.getMessage());
    //     }
    // }

    @PostMapping("/GuardarAsignaciones")
public ResponseEntity<String> GuardarAsignaciones(HttpServletRequest request,
        @RequestParam(value = "idActividad") Long idActividad, HttpSession session,
        @RequestParam(value = "asignados", required = false) Long[] idPersonas) {

    try {
        Usuario u = (Usuario) session.getAttribute("usuario");
        Actividad actividad = actividadService.findById(idActividad);

        if (actividad == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la actividad");
        }

        // Obtener asignaciones actuales
        List<Asignacion> asignacionesActuales = asignacionService.findByActividad(actividad);
        
        // Caso 1: Si no hay personas seleccionadas, marcar todas las asignaciones como eliminadas
        if (idPersonas == null || idPersonas.length == 0) {
            for (Asignacion asignacion : asignacionesActuales) {
                asignacion.setEstado("ELIMINADO");
                asignacionService.save(asignacion);
            }
            return ResponseEntity.ok("Se eliminaron todas las asignaciones correctamente");
        }
        
        // Convertir el array de IDs a un Set para facilitar operaciones
        Set<Long> nuevosAsignadosSet = new HashSet<>();
        if (idPersonas != null) {
            for (Long id : idPersonas) {
                nuevosAsignadosSet.add(id);
            }
        }
        
        // Crear mapa de asignaciones actuales por ID de persona
        Map<Long, Asignacion> asignacionesPorPersona = new HashMap<>();
        for (Asignacion asig : asignacionesActuales) {
            if ("ACTIVO".equals(asig.getEstado())) {
                asignacionesPorPersona.put(asig.getPersona().getIdPersona(), asig);
            }
        }
        
        // PASO 1: Identificar y procesar personas a eliminar (ya no están en la nueva lista)
        for (Long personaId : asignacionesPorPersona.keySet()) {
            if (!nuevosAsignadosSet.contains(personaId)) {
                // Esta persona ya no está asignada
                Asignacion asignacion = asignacionesPorPersona.get(personaId);
                asignacion.setEstado("ELIMINADO");
                asignacionService.save(asignacion);
                System.out.println("Eliminando asignación para: " + asignacion.getPersona().getNombreCompleto());
            }
        }
        
        // PASO 2: Identificar y procesar nuevas asignaciones (no existían antes)
        for (Long personaId : nuevosAsignadosSet) {
            if (!asignacionesPorPersona.containsKey(personaId)) {
                // Esta es una nueva asignación, crear y enviar correo
                Persona persona = personaService.findById(personaId);
                if (persona != null) {
                    // Formatear fechas para el correo
                    String fechaInicioFormateada = formatearFecha(actividad.getFechaInicio());
                    String fechaFinFormateada = formatearFecha(actividad.getFechaFin());
                    
                    // Enviar correo solo a las nuevas asignaciones
                    enviarCorreoAsignacion(
                        persona, 
                        actividad, 
                        fechaInicioFormateada, 
                        fechaFinFormateada
                    );
                    
                    // Crear nueva asignación
                    Asignacion nuevaAsignacion = new Asignacion();
                    nuevaAsignacion.setEstado("ACTIVO");
                    nuevaAsignacion.setRegistro(new Date());
                    nuevaAsignacion.setPersona(persona);
                    nuevaAsignacion.setActividad(actividad);
                    nuevaAsignacion.setRegistroIdUsuario(u.getIdUsuario());
                    asignacionService.save(nuevaAsignacion);
                    
                    System.out.println("Nueva asignación creada para: " + persona.getNombreCompleto());
                }
            }
            // Si ya existe la asignación, no hacemos nada (mantiene su estado ACTIVO)
        }

        return ResponseEntity.ok("Se actualizaron las asignaciones correctamente");
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al guardar las asignaciones: " + e.getMessage());
    }
}

// Método auxiliar para formatear fechas
private String formatearFecha(Object fecha) {
    if (fecha == null) {
        return "";
    }
    
    try {
        SimpleDateFormat formatoOriginal = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatoDeseado = new SimpleDateFormat("dd/MM/yyyy");
        
        Date fechaConvertida = formatoOriginal.parse(fecha.toString());
        return formatoDeseado.format(fechaConvertida);
    } catch (ParseException e) {
        e.printStackTrace();
        return fecha.toString();
    }
}

// Método auxiliar para enviar correo electrónico
private void enviarCorreoAsignacion(Persona persona, Actividad actividad, 
                                 String fechaInicioFormateada, String fechaFinFormateada) {
    String mensaje = """
            <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Universidad Amazónica de Pando</title>
                </head>
                <body style="font-family: Arial, sans-serif; background-color: #dee2e6; padding: 0; margin: 0;">
                    <table width="100%" height="100%" cellpadding="0" cellspacing="0" border="0" style="background-color: #e9ecef; margin-top: 50px;" >
                        <tr>
                            <td align="center" valign="top">
                                <table cellpadding="0" cellspacing="0" border="0" style="background-color: #ffffff; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); max-width: 400px; width: 100%; padding: 20px; box-sizing: border-box;">
                                    <tr>
                                        <td>
                                            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                                <tr>
                                                    <td align="center" style="background-color: #ef233c; color: white; text-align: center; padding: 5px 0; margin-bottom: 50px;">
                                                        <h1 style="font-size: 12px; margin: 0;">Universidad Amazónica de Pando</h1>
                                                        <p style="font-size: 10px; margin: 0;">La preservación de la Amazonía es parte de la subsistencia de la vida, del progreso y desarrollo de la bella tierra Pandina</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p style="font-size: 11px; color: #131313;"><em>Señor(a)</em></p>
                                                        <p style="font-size: 11px; color: #131313;"><em>%s</em></p>
                                                        <p style="text-align: justify; color: #131313;">La Unidad de Gestión Recursos Humanos de la Universidad Amazónica de Pando informa:</p>
                                                        <h2 style="font-size: 14px; color: #333; text-align: center; margin: 0 0 10px;">LA ASIGNACIÓN DE SU ACTIVIDAD A REALIZAR:</h2>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td style="background-color: #e0f7fa; padding: 15px; border-radius: 5px; text-align: justify; margin-bottom: 20px;">
                                                        <p style="margin: 5px 0; color: #131313;"><b>DETALLES: </b></p>
                                                        <p style="margin: 5px 0; color: #131313;"><b>Título: </b><span>%s</span></p>
                                                        <p style="margin: 5px 0; color: #131313;"><b>Descripción: </b><span>%s</span></p>
                                                        <p style="margin: 5px 0; color: #131313;"><b>Fecha Inicio: </b><span>%s</span></p>
                                                        <p style="margin: 5px 0; color: #131313;"><b>Fecha Fin: </b><span>%s</span></p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td align="center">
                                                        <p style="color: #131313;">La actividad tiene una prioridad: %s</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td align="center">
                                                        <p style="color: #131313;">Enlace al Sistema: <a href="http://virtual.uap.edu.bo:8083/" style="color: #007bff; text-decoration: none;">http://virtual.uap.edu.bo:8083/</a></p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td align="center">
                                                        <p><b style="font-size: 12px; color: #333;">UNIDAD DE GESTIÓN DE RECURSOS HUMANOS</b></p>
                                                        <p style="font-size: 12px;">Cobija, Pando</p>
                                                        <p style="font-size: 11px;"><small>Este correo fue enviado por un sistema automatizado por lo cual no debe responder al mismo.</small></p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td align="center" style="background-color: #003566; color: white; padding: 5px 0; margin-top: 50px;">
                                                        <h1 style="font-size: 12px; margin: 0;">RRHH 2025 | Universidad Amazónica de Pando</h1>
                                                        <p style="font-size: 10px; margin: 0;">Escribiendo una nueva historia con vos</p>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
    """;
    
    // Escapar cualquier % que no sea parte del formato reemplazándolo por %%
    //String mensajeProcesado = mensaje.replace("%", "%%").replace("%%s", "%s");
               String mensajeProcesado = mensaje.replace("%", "%%")
            .replace("%%s", "%s");
            String mensajeFormateado = String.format(mensajeProcesado,
            persona.getNombreCompleto(),
            actividad.getTitulo(),
            actividad.getDescripcion(),
            fechaInicioFormateada,
            fechaFinFormateada,
            actividad.getPrioridad() != null ? actividad.getPrioridad().getNivel() : "No especificada");
                    try {
                        emailService.enviarEmail(persona.getCorreo(),
                                "Informaciones RRHH: " + persona.getNombreCompleto(), mensajeFormateado);
                    } catch (MessagingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
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
