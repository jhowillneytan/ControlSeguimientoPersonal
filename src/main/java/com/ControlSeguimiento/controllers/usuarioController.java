package com.ControlSeguimiento.controllers;

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

import com.ControlSeguimiento.model.entity.Persona;
import com.ControlSeguimiento.model.entity.Usuario;
import com.ControlSeguimiento.model.service.PersonaService;
import com.ControlSeguimiento.model.service.RolService;
import com.ControlSeguimiento.model.service.UsuarioService;
import com.ControlSeguimiento.model.service.UtilidadesService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class usuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private RolService rolService;

    @Autowired
    private UtilidadesService utilidadesService;

    //@ValidarUsuarioAutenticado
    @GetMapping("/ventana")
    public String inicio(HttpSession session) {

        if (session.getAttribute("usuario") == null) {
            // La sesión ha expirado o no existe
            return "redirect:/form-login";
        }

        return "usuario/ventana";
    }

    //@ValidarUsuarioAutenticado
    @PostMapping("/tablaRegistros")
    public String tablaRegistros(Model model) {
        List<Usuario> listUsuarios = usuarioService.listarUsuarios();
        for (Usuario usuario : listUsuarios) {
            String contraseñaDecrypt;
            try {
                contraseñaDecrypt = utilidadesService.decrypt(usuario.getPassword());
                usuario.setPassword(contraseñaDecrypt);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        model.addAttribute("usuarios", listUsuarios);
        return "usuario/tablaRegistros";
    }

    //@ValidarUsuarioAutenticado
    @PostMapping("/formulario")
    public String formulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("personas", personaService.listarPersonas());
        model.addAttribute("roles", rolService.findAll());
        return "usuario/formulario";
    }

    //@ValidarUsuarioAutenticado
    @PostMapping("/formulario/{id}")
    public String formulario(Model model, @PathVariable("id") Long idUser) {
        Usuario usuario = usuarioService.findById(idUser);

        try {
            String contraseñaDecryp = utilidadesService.decrypt(usuario.getPassword());
            usuario.setPassword(contraseñaDecryp);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("personas", personaService.listarPersonas());
        model.addAttribute("roles", rolService.findAll());
        model.addAttribute("edit", "true");
        return "usuario/formulario";
    }

    //@ValidarUsuarioAutenticado
    @PostMapping("/RegistrarUsuario")
    public ResponseEntity<String> RegistrarUsuario(HttpServletRequest request, @Validated Usuario usuario) {
        if (usuarioService.buscarPorNombreUser(usuario.getNombre()) == null) {
            try {
                String contraseñaEncrypt = utilidadesService.encrypt(usuario.getPassword());
                usuario.setPassword(contraseñaEncrypt);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
                return ResponseEntity.ok("Error: " + e.getMessage());
            }
            usuario.setEstado("ACTIVO");
            usuario.setRegistro(new Date());
            usuarioService.save(usuario);
            return ResponseEntity.ok("Se realizó el registro correctamente");
        } else {
            return ResponseEntity.ok("Ya existe un registro con este nombre");
        }
    }

    //@ValidarUsuarioAutenticado
    @PostMapping("/ModificarUsuario")
    public ResponseEntity<String> ModificarUsuario(HttpServletRequest request, @Validated Usuario usuario) {
        Usuario user = usuarioService.findById(usuario.getIdUsuario());
        if (usuarioService.compararNombreUser(user.getNombre(), usuario.getNombre()) == null) {
            try {
                String contraseñaEncrypt = utilidadesService.encrypt(usuario.getPassword());
                user.setPassword(contraseñaEncrypt);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
                return ResponseEntity.ok("Error: " + e.getMessage());
            }
            user.setNombre(usuario.getNombre());
            user.setRoles(usuario.getRoles());
            user.setPersona(usuario.getPersona());
            user.setModificacion(new Date());
            usuarioService.save(user);
            return ResponseEntity.ok("Se realizó el registro correctamente");
        } else {
            return ResponseEntity.ok("Ya existe un registro con este nombre");
        }
    }

    //@ValidarUsuarioAutenticado
    @PostMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(Model model, @PathVariable("id") Long idPerson) {
        Usuario usuario = usuarioService.findById(idPerson);
        usuario.setEstado("ELIMINADO");
        usuarioService.save(usuario);
        return ResponseEntity.ok("Registro Eliminado");
    }

    @PostMapping("/ModificarPerfilUsuario")
    public ResponseEntity<String> ModificarPerfilUsuario(HttpServletRequest request, @RequestParam("idUsuario")Long idUsuario,
    @RequestParam("nombreUser")String nombreUser, @RequestParam("password")String password,
    @RequestParam("ci")String ci, @RequestParam("nombre")String nombre,
    @RequestParam("paterno")String paterno, @RequestParam("materno")String materno) {

        Usuario user = usuarioService.findById(idUsuario);
        if (usuarioService.compararNombreUser(user.getNombre(), nombreUser) == null) {
            try {
                String contraseñaEncrypt = utilidadesService.encrypt(password);
                user.setPassword(contraseñaEncrypt);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
                return ResponseEntity.ok("Error: " + e.getMessage());
            }
            user.setNombre(nombreUser);
            user.setModificacion(new Date());
            usuarioService.save(user);

            Persona persona = personaService.findById(user.getPersona().getIdPersona());
            persona.setCi(ci);
            persona.setNombre(nombre);
            persona.setPaterno(paterno);
            persona.setMaterno(materno);
            personaService.save(persona);
            return ResponseEntity.ok("Se realizó el registro correctamente");
        } else {
            return ResponseEntity.ok("Ya existe un registro con este nombre");
        }
    }

}
