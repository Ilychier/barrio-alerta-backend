package com.alertabarrio.ingsoft.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alertabarrio.ingsoft.models.dtos.BarrioResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.BarrioSaveDTO;
import com.alertabarrio.ingsoft.services.BarrioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/barrios")
public class BarrioController {

    private final BarrioService barrioService;

    public BarrioController(BarrioService barrioService) {
        this.barrioService = barrioService;
    }

    @PostMapping
    public ResponseEntity<BarrioResponseDTO> create(@Valid @RequestBody BarrioSaveDTO barrioSaveDTO) {
        BarrioResponseDTO savedBarrio = barrioService.save(barrioSaveDTO);
        return new ResponseEntity<>(savedBarrio, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarrioResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(barrioService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarrioResponseDTO> update(
        @PathVariable Long id, 
        @Valid @RequestBody BarrioSaveDTO barrioSaveDTO) {
        return ResponseEntity.ok(barrioService.update(id, barrioSaveDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BarrioResponseDTO> patch(
        @PathVariable Long id, 
        @RequestBody BarrioSaveDTO barrioSaveDTO) {
        return ResponseEntity.ok(barrioService.patch(id, barrioSaveDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        barrioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<BarrioResponseDTO>> getAllPaginated(
        @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(barrioService.findAllPaginated(pageable));
    }

}