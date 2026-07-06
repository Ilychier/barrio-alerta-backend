package com.alertabarrio.ingsoft.controllers;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alertabarrio.ingsoft.models.dtos.AlertaResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.AlertaSaveDTO;
import com.alertabarrio.ingsoft.services.AlertaService;

import jakarta.validation.Valid;

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
    public ResponseEntity<Page<AlertaResponseDTO>> findAllPaginated(
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @PageableDefault(sort = "fechaHora", direction = Sort.Direction.DESC) Pageable pageable) {

        LocalDate fechaFiltro = (fecha != null) ? fecha : LocalDate.now();
        return ResponseEntity.ok(alertaService.findByFecha(fechaFiltro, pageable));
    }
}
