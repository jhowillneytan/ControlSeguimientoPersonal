package com.ControlSeguimiento;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ControlSeguimiento.model.entity.Persona;
import com.ControlSeguimiento.model.entity.Rol;
import com.ControlSeguimiento.model.entity.Usuario;
import com.ControlSeguimiento.model.service.PersonaService;
import com.ControlSeguimiento.model.service.RolService;
import com.ControlSeguimiento.model.service.UsuarioService;
import com.ControlSeguimiento.model.service.UtilidadesService;

@SpringBootApplication
public class ControlSeguimientoApplication {
	private static final Logger logger = LoggerFactory.getLogger(ControlSeguimientoApplication.class);
	public static void main(String[] args) throws IOException {
		SpringApplication.run(ControlSeguimientoApplication.class, args);

		Path rootPathCarnet = Paths.get("uploads/");
		String rutaDirectorioCarnet = rootPathCarnet + "";
		if (!Files.exists(rootPathCarnet)) {
			Files.createDirectories(rootPathCarnet);
			System.out.println("Directorio creado: " + rutaDirectorioCarnet);
		} else {
			System.out.println("El directorio ya existe: " + rutaDirectorioCarnet);
		}
	}

	@Bean
	ApplicationRunner init(UsuarioService usuarioService, PersonaService personaService, RolService rolService,
			UtilidadesService utilidadesService) {

		return args -> {

			logger.info("Sistema Iniciado...");

			// Verificar y crear roles si no existen
			String[] roles = { "SUPER USUARIO", "ADMINISTRADOR", "RESPONSABLE"};
			Rol[] rolObjects = new Rol[roles.length];
			for (int i = 0; i < roles.length; i++) {
				Rol rol = rolService.buscaPorNombre(roles[i]);
				if (rol == null) {
					rol = new Rol();
					rol.setNombre(roles[i]);
					rol.setEstado("ACTIVO");
					rolService.save(rol);
				}
				rolObjects[i] = rol;
			}

			// Crear o actualizar la primera persona y su usuario
			String[] cis = { "111"};
			String[] nombres = { "PRIMER USUARIO"};
			String[] usuarios = { "admin"};
			String[] password = { "123"};
			for (int i = 0; i < cis.length; i++) {
				// Verificar si la persona ya existe
				Persona persona = personaService.buscarPorCi(cis[i]);
				if (persona == null) {
					persona = new Persona();
					persona.setCodFuncionario(0L);
					persona.setNombre(nombres[i]);
					persona.setPaterno("ApellidoP" + (i + 1));
					persona.setMaterno("ApellidoM" + (i + 1));
					persona.setCorreo("admin@gmail.com");
					persona.setCi(cis[i]);
					persona.setEstado("ACTIVO");
					personaService.save(persona);
				}

				// Verificar si el usuario ya existe
				Usuario usuario = usuarioService.buscarPorNombreUser(usuarios[i]);
				if (usuario == null) {
					usuario = new Usuario();
					usuario.setNombre(usuarios[i]);
					// usuario.setPassword(password[i]);
					usuario.setPassword(utilidadesService.encrypt(password[i]));
					usuario.setPersona(persona); // Asociar la persona con el usuario

					// Asignar los roles correspondientes al usuario
					Set<Rol> usuarioRoles = new HashSet<>();
					usuarioRoles.add(rolObjects[i]); // Asigna el rol correspondiente (por ejemplo, rol de admin1,
														// admin2, etc.)
					usuario.setRoles(usuarioRoles); // Asignar el conjunto de roles al usuario
					usuario.setEstado("ACTIVO");
					usuarioService.save(usuario);
				}
			}
		};
	}

}
