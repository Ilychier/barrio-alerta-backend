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

import com.alertabarrio.ingsoft.models.dtos.UsuarioBarrioResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.UsuarioBarrioSaveDTO;
import com.alertabarrio.ingsoft.services.UsuarioBarrioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios-barrios")
@CrossOrigin(origins = "*")
public class UsuarioBarrioController {

    private final UsuarioBarrioService usuarioBarrioService;

    public UsuarioBarrioController(UsuarioBarrioService usuarioBarrioService) {
        this.usuarioBarrioService = usuarioBarrioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioBarrioResponseDTO> create(@Valid @RequestBody UsuarioBarrioSaveDTO dto) {
        UsuarioBarrioResponseDTO saved = usuarioBarrioService.save(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioBarrioResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioBarrioService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioBarrioResponseDTO> update(
        @PathVariable Long id, 
        @Valid @RequestBody UsuarioBarrioSaveDTO dto) {
        return ResponseEntity.ok(usuarioBarrioService.update(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioBarrioResponseDTO> patch(
        @PathVariable Long id, 
        @RequestBody UsuarioBarrioSaveDTO dto) {
        return ResponseEntity.ok(usuarioBarrioService.patch(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioBarrioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioBarrioResponseDTO>> getAllPaginated(
        @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(usuarioBarrioService.findAllPaginated(pageable));
    }
}
