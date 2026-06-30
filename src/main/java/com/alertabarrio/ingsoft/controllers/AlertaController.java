package com.alertabarrio.ingsoft.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alertabarrio.ingsoft.models.dtos.AlertaResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.AlertaSaveDTO;
import com.alertabarrio.ingsoft.services.AlertaService;

@RestController
@RequestMapping("/api/alertas")
@CrossOrigin(origins = "*")
public class AlertaController {

    private final AlertaService alertaService;

    public AlertaController(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    @PostMapping
    public ResponseEntity<AlertaResponseDTO> save(@Valid @RequestBody AlertaSaveDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(alertaService.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(alertaService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlertaResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AlertaSaveDTO dto) {
        return ResponseEntity.ok(alertaService.update(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AlertaResponseDTO> patch(@PathVariable Long id, @RequestBody AlertaSaveDTO dto) {
        return ResponseEntity.ok(alertaService.patch(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        alertaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<AlertaResponseDTO>> findAllPaginated(Pageable pageable) {
        return ResponseEntity.ok(alertaService.findAllPaginated(pageable));
    }
}
