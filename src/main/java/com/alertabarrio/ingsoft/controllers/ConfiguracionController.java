package com.alertabarrio.ingsoft.controllers;

import com.alertabarrio.ingsoft.models.entities.Configuracion;
import com.alertabarrio.ingsoft.repositories.ConfiguracionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/configuraciones")
public class ConfiguracionController {

    @Autowired
    private ConfiguracionRepository configuracionRepository;

    // Guardar una Configuración (POST) - http://localhost:8080/api/configuraciones
    @PostMapping
    public ResponseEntity<Configuracion> crearConfiguracion(@RequestBody Configuracion configuracion) {
        Configuracion nuevaConfig = configuracionRepository.save(configuracion);
        return new ResponseEntity<>(nuevaConfig, HttpStatus.CREATED);
    }

    // Obtener todas las Configuraciones (GET) - http://localhost:8080/api/configuraciones
    @GetMapping
    public ResponseEntity<List<Configuracion>> listarConfiguraciones() {
        List<Configuracion> lista = configuracionRepository.findAll();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }
}
