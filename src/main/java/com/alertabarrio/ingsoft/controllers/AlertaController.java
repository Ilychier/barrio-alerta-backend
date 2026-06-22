package com.alertabarrio.ingsoft.controllers;

import com.alertabarrio.ingsoft.models.entities.Alerta;
import com.alertabarrio.ingsoft.repositories.AlertaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alertas")
public class AlertaController {

    @Autowired
    private AlertaRepository alertaRepository;

    // Crear una Alerta (POST) - http://localhost:8080/api/alertas
    @PostMapping
    public ResponseEntity<Alerta> crearAlerta(@RequestBody Alerta alerta) {
        Alerta nuevaAlerta = alertaRepository.save(alerta);
        return new ResponseEntity<>(nuevaAlerta, HttpStatus.CREATED);
    }

    // Listar todas las Alertas (GET) - http://localhost:8080/api/alertas
    @GetMapping
    public ResponseEntity<List<Alerta>> listarAlertas() {
        List<Alerta> alertas = alertaRepository.findAll();
        return new ResponseEntity<>(alertas, HttpStatus.OK);
    }
}
