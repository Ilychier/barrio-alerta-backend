package com.alertabarrio.ingsoft.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.RestController;

import com.alertabarrio.ingsoft.models.dtos.CategoriaResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.CategoriaSaveDTO;
import com.alertabarrio.ingsoft.services.CategoriaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> create(@Valid @RequestBody CategoriaSaveDTO categoriaSaveDTO) {
        CategoriaResponseDTO savedCategoria = categoriaService.save(categoriaSaveDTO);
        return new ResponseEntity<>(savedCategoria, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> update(
        @PathVariable Long id, 
        @Valid @RequestBody CategoriaSaveDTO categoriaSaveDTO) {
        return ResponseEntity.ok(categoriaService.update(id, categoriaSaveDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> patch(
        @PathVariable Long id, 
        @RequestBody CategoriaSaveDTO categoriaSaveDTO) {
        return ResponseEntity.ok(categoriaService.patch(id, categoriaSaveDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<CategoriaResponseDTO>> getAllPaginated(
        @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(categoriaService.findAllPaginated(pageable));
    }

}