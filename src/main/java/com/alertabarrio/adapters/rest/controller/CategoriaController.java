package com.alertabarrio.adapters.rest.controller;

import com.alertabarrio.adapters.rest.dto.CategoriaRequestDTO;
import com.alertabarrio.adapters.rest.dto.CategoriaResponseDTO;
import com.alertabarrio.adapters.rest.mapper.CategoriaDtoMapper;
import com.alertabarrio.application.dto.CategoriaDTO;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import com.alertabarrio.domain.port.in.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/categorias")
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
    public ResponseEntity<Pagina<CategoriaResponseDTO>> findAllPaginated(
            @PageableDefault(sort = "nombre", direction = Sort.Direction.ASC) Pageable pageable) {
        Paginacion paginacion = Paginacion.of(pageable.getPageNumber(), pageable.getPageSize(),
                pageable.getSort().toString().isEmpty() ? null : pageable.getSort().toString(), null);
        Pagina<CategoriaDTO> result = listarCategoriasUseCase.execute(mapper.toListarQuery(paginacion));
        return ResponseEntity.ok(new Pagina<>(
                result.contenido().stream().map(mapper::toResponse).toList(),
                result.pagina(), result.tamanio(), result.totalElementos(), result.totalPaginas()));
    }
}
