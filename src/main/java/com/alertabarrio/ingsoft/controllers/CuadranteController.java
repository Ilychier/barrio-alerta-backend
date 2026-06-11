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

import com.alertabarrio.ingsoft.models.dtos.CuadranteResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.CuadranteSaveDTO;
import com.alertabarrio.ingsoft.services.CuadranteService; // <-- Import corregido apuntando a la nueva ruta

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cuadrantes")
public class CuadranteController {

    private final CuadranteService cuadranteService;

    public CuadranteController(CuadranteService cuadranteService) {
        this.cuadranteService = cuadranteService;
    }

    @PostMapping
    public ResponseEntity<CuadranteResponseDTO> create(@Valid @RequestBody CuadranteSaveDTO cuadranteSaveDTO) {
        CuadranteResponseDTO savedCuadrante = cuadranteService.save(cuadranteSaveDTO);
        return new ResponseEntity<>(savedCuadrante, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuadranteResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(cuadranteService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuadranteResponseDTO> update(
        @PathVariable Long id, 
        @Valid @RequestBody CuadranteSaveDTO cuadranteSaveDTO) {
        return ResponseEntity.ok(cuadranteService.update(id, cuadranteSaveDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CuadranteResponseDTO> patch(
        @PathVariable Long id, 
        @RequestBody CuadranteSaveDTO cuadranteSaveDTO) {
        return ResponseEntity.ok(cuadranteService.patch(id, cuadranteSaveDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cuadranteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<CuadranteResponseDTO>> getAllPaginated(
        @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(cuadranteService.findAllPaginated(pageable));
    }

}