package com.ControlSeguimiento.controllers;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ControlSeguimiento.model.entity.Rol;
import com.ControlSeguimiento.model.entity.Usuario;
import com.ControlSeguimiento.model.service.PersonaService;
import com.ControlSeguimiento.model.service.UsuarioService;
import com.ControlSeguimiento.model.service.UtilidadesService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class indexController {
    private static final Logger logger = LoggerFactory.getLogger(indexController.class);

    private final UsuarioService usuarioService;
    private final PersonaService personaService;

    private final UtilidadesService utilidadesService;
    

    @GetMapping("/")
    public String getMethodName() {
        return "redirect:/ControlSeguimiento";
    }
    
    //@ValidarUsuarioAutenticado
    @GetMapping("/ControlSeguimiento")
    @Transactional
    public String inicio(HttpServletRequest request) {

        Usuario usuario = usuarioService.buscarPorNombreUser("admin1");
        HttpSession sessionAdministrador = request.getSession(true);
            String contraseñaDecrypt;
            try {
                contraseñaDecrypt = utilidadesService.decrypt(usuario.getPassword());
                usuario.setPassword(contraseñaDecrypt);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            sessionAdministrador.setAttribute("usuario", usuario);
            sessionAdministrador.setAttribute("persona", personaService.findById(usuario.getPersona().getIdPersona()));
            sessionAdministrador.setAttribute("roles", new ArrayList<Rol>(usuario.getRoles()));
            System.out.println("USUARIO INICIADO");

        //Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        usuario = (Usuario) request.getSession().getAttribute("usuario");
        logger.info("Usuario en sesión: {}", usuario.getPersona().getNombre());
        return "index";
        
    }

    @GetMapping("/cargar-datos")
    @ResponseBody
    public ResponseEntity<String> cargarDatos(HttpSession session) {
        if (session.getAttribute("usuario") == null) {
            // La sesión ha expirado o no existe
            return new ResponseEntity<>("Sesión expirada", HttpStatus.UNAUTHORIZED);
        }
        // Si la sesión está activa, devuelve el contenido
        return new ResponseEntity<>("Datos del contenido", HttpStatus.OK);
    }
    
}
