package com.alertabarrio.ingsoft.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alertabarrio.ingsoft.models.dtos.CategoriaDescripcionResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.CategoriaDescripcionSaveDTO;
import com.alertabarrio.ingsoft.services.CategoriaDescripcionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categoria-descripciones")
@CrossOrigin(origins = "*")
public class CategoriaDescripcionController {

    private final CategoriaDescripcionService categoriaDescripcionService;

    public CategoriaDescripcionController(CategoriaDescripcionService categoriaDescripcionService) {
        this.categoriaDescripcionService = categoriaDescripcionService;
    }

    @PostMapping
    public ResponseEntity<CategoriaDescripcionResponseDTO> save(@Valid @RequestBody CategoriaDescripcionSaveDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaDescripcionService.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDescripcionResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaDescripcionService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDescripcionResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CategoriaDescripcionSaveDTO dto) {
        return ResponseEntity.ok(categoriaDescripcionService.update(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoriaDescripcionResponseDTO> patch(@PathVariable Long id, @RequestBody CategoriaDescripcionSaveDTO dto) {
        return ResponseEntity.ok(categoriaDescripcionService.patch(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoriaDescripcionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<CategoriaDescripcionResponseDTO>> findAllPaginated(
            @RequestParam(required = false) Long categoriaId,
            Pageable pageable) {
        return ResponseEntity.ok(categoriaDescripcionService.findAllPaginated(categoriaId, pageable));
    }
}
