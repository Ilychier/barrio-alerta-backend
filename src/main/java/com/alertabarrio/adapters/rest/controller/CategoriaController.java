package com.alertabarrio.adapters.rest.controller;

import com.alertabarrio.adapters.rest.dto.CategoriaRequestDTO;
import com.alertabarrio.adapters.rest.dto.CategoriaResponseDTO;
import com.alertabarrio.adapters.rest.mapper.CategoriaDtoMapper;
import com.alertabarrio.application.dto.CategoriaDTO;
import com.alertabarrio.domain.port.in.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v2/categorias")
public class CategoriaController {

    private final CrearCategoriaUseCase crearCategoriaUseCase;
    private final ActualizarCategoriaUseCase actualizarCategoriaUseCase;
    private final ParchearCategoriaUseCase parchearCategoriaUseCase;
    private final EliminarCategoriaUseCase eliminarCategoriaUseCase;
    private final BuscarCategoriaUseCase buscarCategoriaUseCase;
    private final ListarCategoriasUseCase listarCategoriasUseCase;
    private final CategoriaDtoMapper mapper;

    public CategoriaController(
            CrearCategoriaUseCase crearCategoriaUseCase,
            ActualizarCategoriaUseCase actualizarCategoriaUseCase,
            ParchearCategoriaUseCase parchearCategoriaUseCase,
            EliminarCategoriaUseCase eliminarCategoriaUseCase,
            BuscarCategoriaUseCase buscarCategoriaUseCase,
            ListarCategoriasUseCase listarCategoriasUseCase,
            CategoriaDtoMapper mapper) {
        this.crearCategoriaUseCase = crearCategoriaUseCase;
        this.actualizarCategoriaUseCase = actualizarCategoriaUseCase;
        this.parchearCategoriaUseCase = parchearCategoriaUseCase;
        this.eliminarCategoriaUseCase = eliminarCategoriaUseCase;
        this.buscarCategoriaUseCase = buscarCategoriaUseCase;
        this.listarCategoriasUseCase = listarCategoriasUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> create(@Valid @RequestBody CategoriaRequestDTO dto) {
        CategoriaDTO result = crearCategoriaUseCase.execute(mapper.toCrearCommand(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> findById(@PathVariable Long id) {
        Optional<CategoriaDTO> result = buscarCategoriaUseCase.execute(mapper.toBuscarQuery(id));
        return result.map(dto -> ResponseEntity.ok(mapper.toResponse(dto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CategoriaRequestDTO dto) {
        CategoriaDTO result = actualizarCategoriaUseCase.execute(mapper.toActualizarCommand(id, dto));
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> patch(@PathVariable Long id, @RequestBody CategoriaRequestDTO dto) {
        CategoriaDTO result = parchearCategoriaUseCase.execute(mapper.toParchearCommand(id, dto));
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eliminarCategoriaUseCase.execute(mapper.toEliminarCommand(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<CategoriaResponseDTO>> findAllPaginated(
            @PageableDefault(sort = "nombre", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<CategoriaDTO> result = listarCategoriasUseCase.execute(mapper.toListarQuery(pageable));
        return ResponseEntity.ok(mapper.toResponsePage(result));
    }
}
