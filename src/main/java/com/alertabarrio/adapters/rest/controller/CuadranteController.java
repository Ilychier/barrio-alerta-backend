package com.alertabarrio.adapters.rest.controller;

import com.alertabarrio.adapters.rest.dto.CuadranteRequestDTO;
import com.alertabarrio.adapters.rest.dto.CuadranteResponseDTO;
import com.alertabarrio.adapters.rest.mapper.CuadranteDtoMapper;
import com.alertabarrio.application.dto.CuadranteDTO;
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
@RequestMapping("/api/cuadrantes")
public class CuadranteController {

    private final CrearCuadranteUseCase crearCuadranteUseCase;
    private final ActualizarCuadranteUseCase actualizarCuadranteUseCase;
    private final ParchearCuadranteUseCase parchearCuadranteUseCase;
    private final EliminarCuadranteUseCase eliminarCuadranteUseCase;
    private final BuscarCuadranteUseCase buscarCuadranteUseCase;
    private final ListarCuadrantesUseCase listarCuadrantesUseCase;
    private final CuadranteDtoMapper mapper;

    public CuadranteController(
            CrearCuadranteUseCase crearCuadranteUseCase,
            ActualizarCuadranteUseCase actualizarCuadranteUseCase,
            ParchearCuadranteUseCase parchearCuadranteUseCase,
            EliminarCuadranteUseCase eliminarCuadranteUseCase,
            BuscarCuadranteUseCase buscarCuadranteUseCase,
            ListarCuadrantesUseCase listarCuadrantesUseCase,
            CuadranteDtoMapper mapper) {
        this.crearCuadranteUseCase = crearCuadranteUseCase;
        this.actualizarCuadranteUseCase = actualizarCuadranteUseCase;
        this.parchearCuadranteUseCase = parchearCuadranteUseCase;
        this.eliminarCuadranteUseCase = eliminarCuadranteUseCase;
        this.buscarCuadranteUseCase = buscarCuadranteUseCase;
        this.listarCuadrantesUseCase = listarCuadrantesUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<CuadranteResponseDTO> create(@Valid @RequestBody CuadranteRequestDTO dto) {
        CuadranteDTO result = crearCuadranteUseCase.execute(mapper.toCrearCommand(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuadranteResponseDTO> findById(@PathVariable Long id) {
        Optional<CuadranteDTO> result = buscarCuadranteUseCase.execute(mapper.toBuscarQuery(id));
        return result.map(dto -> ResponseEntity.ok(mapper.toResponse(dto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuadranteResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CuadranteRequestDTO dto) {
        CuadranteDTO result = actualizarCuadranteUseCase.execute(mapper.toActualizarCommand(id, dto));
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CuadranteResponseDTO> patch(@PathVariable Long id, @RequestBody CuadranteRequestDTO dto) {
        CuadranteDTO result = parchearCuadranteUseCase.execute(mapper.toParchearCommand(id, dto));
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eliminarCuadranteUseCase.execute(mapper.toEliminarCommand(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<CuadranteResponseDTO>> findAllPaginated(
            @PageableDefault(sort = "nombreUnidad", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<CuadranteDTO> result = listarCuadrantesUseCase.execute(mapper.toListarQuery(pageable));
        return ResponseEntity.ok(mapper.toResponsePage(result));
    }
}
