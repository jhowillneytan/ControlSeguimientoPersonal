package com.ControlSeguimiento.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ControlSeguimiento.model.entity.ArchivoAdjunto;
import com.ControlSeguimiento.model.service.ArchivoAdjuntoService;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Controller
@RequestMapping("/archivoAjunto")
public class archivoAdjuntoController {

    @Autowired
    private ArchivoAdjuntoService adjuntoService;

    @PostMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(Model model, @PathVariable("id") Long id) {
        adjuntoService.deleteById(id);
        return ResponseEntity.ok("Registro Eliminado");
    }

    @GetMapping("/optener/{idArchivo}")
    public ResponseEntity<Resource> verDocumento(@PathVariable("idArchivo") Long id) throws IOException {
        ArchivoAdjunto archivoAdjunto = adjuntoService.findById(id);

        // Obtener la ruta completa del archivo
        Path projectPath = Paths.get("").toAbsolutePath();
        String ruta = projectPath + "/uploads/" + archivoAdjunto.getRuta();
        System.out.println(ruta);
        // Cargar el archivo PDF como recurso
        Resource resource = new InputStreamResource(new FileInputStream(ruta));

        // Configurar las cabeceras de la respuesta
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + archivoAdjunto.getNombre()); // "inline"
                                                                                                      // para
                                                                                                      // visualizar
                                                                                                      // en el
                                                                                                      // navegador
        MediaType mediaType = MediaType.parseMediaType(archivoAdjunto.getTipo());
        headers.setContentType(mediaType);
        // Devolver la respuesta con el archivo PDF
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
