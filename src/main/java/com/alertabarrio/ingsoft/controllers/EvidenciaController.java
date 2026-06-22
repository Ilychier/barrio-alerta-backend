package com.alertabarrio.ingsoft.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alertabarrio.ingsoft.models.dtos.EvidenciaResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.EvidenciaSaveDTO;
import com.alertabarrio.ingsoft.services.EvidenciaService;

@RestController
@RequestMapping("/api/evidencias")
public class EvidenciaController {

    private final EvidenciaService evidenciaService;

    public EvidenciaController(EvidenciaService evidenciaService) {
        this.evidenciaService = evidenciaService;
    }

    @PostMapping
    public ResponseEntity<EvidenciaResponseDTO> save(@Valid @RequestBody EvidenciaSaveDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(evidenciaService.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvidenciaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(evidenciaService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvidenciaResponseDTO> update(@PathVariable Long id, @Valid @RequestBody EvidenciaSaveDTO dto) {
        return ResponseEntity.ok(evidenciaService.update(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EvidenciaResponseDTO> patch(@PathVariable Long id, @RequestBody EvidenciaSaveDTO dto) {
        return ResponseEntity.ok(evidenciaService.patch(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        evidenciaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<EvidenciaResponseDTO>> findAllPaginated(Pageable pageable) {
        return ResponseEntity.ok(evidenciaService.findAllPaginated(pageable));
    }
}
