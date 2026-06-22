package com.alertabarrio.ingsoft.controllers;

import com.alertabarrio.ingsoft.models.entities.Evidencia;
import com.alertabarrio.ingsoft.repositories.EvidenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evidencias")
public class EvidenciaController {

    @Autowired
    private EvidenciaRepository evidenciaRepository;

    // Subir un registro de Evidencia (POST) - http://localhost:8080/api/evidencias
    @PostMapping
    public ResponseEntity<Evidencia> crearEvidencia(@RequestBody Evidencia evidencia) {
        Evidencia nuevaEvidencia = evidenciaRepository.save(evidencia);
        return new ResponseEntity<>(nuevaEvidencia, HttpStatus.CREATED);
    }

    // Listar las Evidencias (GET) - http://localhost:8080/api/evidencias
    @GetMapping
    public ResponseEntity<List<Evidencia>> listarEvidencias() {
        List<Evidencia> evidencias = evidenciaRepository.findAll();
        return new ResponseEntity<>(evidencias, HttpStatus.OK);
    }
}
