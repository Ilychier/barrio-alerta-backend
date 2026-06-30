package com.alertabarrio.ingsoft.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import com.alertabarrio.ingsoft.models.dtos.ConfiguracionResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.ConfiguracionSaveDTO;
import com.alertabarrio.ingsoft.services.ConfiguracionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/configuraciones")
@CrossOrigin(origins = "*")
public class ConfiguracionController {

    private final ConfiguracionService configuracionService;

    public ConfiguracionController(ConfiguracionService configuracionService) {
        this.configuracionService = configuracionService;
    }

    @PostMapping
    public ResponseEntity<ConfiguracionResponseDTO> save(@Valid @RequestBody ConfiguracionSaveDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(configuracionService.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConfiguracionResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(configuracionService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConfiguracionResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ConfiguracionSaveDTO dto) {
        return ResponseEntity.ok(configuracionService.update(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ConfiguracionResponseDTO> patch(@PathVariable Long id, @RequestBody ConfiguracionSaveDTO dto) {
        return ResponseEntity.ok(configuracionService.patch(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        configuracionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<ConfiguracionResponseDTO>> findAllPaginated(Pageable pageable) {
        return ResponseEntity.ok(configuracionService.findAllPaginated(pageable));
    }
}
