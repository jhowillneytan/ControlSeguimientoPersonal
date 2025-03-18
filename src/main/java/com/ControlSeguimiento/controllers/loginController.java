package com.ControlSeguimiento.controllers;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ControlSeguimiento.model.entity.Rol;
import com.ControlSeguimiento.model.entity.Usuario;
import com.ControlSeguimiento.model.service.UsuarioService;
import com.ControlSeguimiento.model.service.UtilidadesService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class loginController {

    private final UsuarioService usuarioService;

    private final UtilidadesService utilidadesService;

    @GetMapping("/form-login")
    public String getMethodName() {
        return "usuario/form-login";
    }

    // @PostMapping("/iniciar-sesion")
    // public String iniciarSesion(@RequestParam(value = "usuario") String user,
    //         @RequestParam(value = "contrasena") String contrasena, Model model, HttpServletRequest request,
    //         RedirectAttributes flash) throws Exception {

    //     // los dos parametros de usuario, contraseña vienen del formulario html
    //     Usuario usuario = usuarioService.getUsuarioPassword(user, utilidadesService.encrypt(contrasena));

    //     if (usuario != null) {
    //         if (usuario.getEstado().equals("INACTIVO")) {
    //             return "redirect:/form-login";
    //         }
    //         HttpSession sessionAdministrador = request.getSession(true);
    //         String contraseñaDecrypt;
    //         try {
    //             contraseñaDecrypt = utilidadesService.decrypt(usuario.getPassword());
    //             usuario.setPassword(contraseñaDecrypt);
    //         } catch (Exception e) {
    //             // TODO Auto-generated catch block
    //             e.printStackTrace();
    //         }
    //         sessionAdministrador.setAttribute("usuario", usuario);
    //         sessionAdministrador.setAttribute("persona", usuario.getPersona());
    //         sessionAdministrador.setAttribute("roles", new ArrayList<Rol>(usuario.getRoles()));

    //         flash.addAttribute("success", usuario.getPersona().getNombre());

    //         return "redirect:/Planificacion";

    //     } else {
    //         // flash.addFlashAttribute("error", "Usuario o contraseña incorrectos.");
    //         return "redirect:/form-login";
    //     }

    // }

    @PostMapping("/iniciar-sesion")
    public ResponseEntity<String> iniciarSesion(@RequestParam(value = "usuario") String user,
            @RequestParam(value = "contrasena") String contrasena, Model model, HttpServletRequest request,
            RedirectAttributes flash) throws Exception {

        // los dos parametros de usuario, contraseña vienen del formulario html
        Usuario usuario = usuarioService.getUsuarioPassword(user, utilidadesService.encrypt(contrasena));

        if (usuario != null) {
            if (usuario.getEstado().equals("INACTIVO")) {
                return ResponseEntity.ok("Este usuario esta en estado inactivo!");
            }
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
            sessionAdministrador.setAttribute("persona", usuario.getPersona());
            sessionAdministrador.setAttribute("roles", new ArrayList<Rol>(usuario.getRoles()));

            flash.addAttribute("success", usuario.getPersona().getNombre());

            return ResponseEntity.ok("Iniciando Session");

        } else {
            // flash.addFlashAttribute("error", "Usuario o contraseña incorrectos.");
            return ResponseEntity.ok("Usuario o contraseña incorrectos!");
        }

    }

    //@ValidarUsuarioAutenticado
    @RequestMapping("/cerrar_sesion")
    public String cerrarSesion(HttpServletRequest request, RedirectAttributes flash) {
        Usuario usuarioLogueado = (Usuario) request.getSession().getAttribute("usuario");
        HttpSession sessionAdministrador = request.getSession();
        if (sessionAdministrador != null) {
            sessionAdministrador.invalidate();
            flash.addAttribute("validado", "Se cerro sesion con exito");
            System.out.println("LA PERSONA " + usuarioLogueado.getPersona().getNombre() + " "
                    + usuarioLogueado.getPersona().getPaterno() + " " + usuarioLogueado.getPersona().getMaterno()
                    + " HA CERRADO SESIÓN");
        }
        return "redirect:/form-login";
    }
}
