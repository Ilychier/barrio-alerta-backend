package com.alertabarrio.adapters.rest.controller;

import com.alertabarrio.adapters.rest.dto.CategoriaDescripcionRequestDTO;
import com.alertabarrio.adapters.rest.dto.CategoriaDescripcionResponseDTO;
import com.alertabarrio.adapters.rest.mapper.CategoriaDescripcionDtoMapper;
import com.alertabarrio.application.dto.CategoriaDescripcionDTO;
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
@RequestMapping("/api/categoria-descripciones")
public class CategoriaDescripcionController {

    private final CrearCategoriaDescripcionUseCase crearCategoriaDescripcionUseCase;
    private final ActualizarCategoriaDescripcionUseCase actualizarCategoriaDescripcionUseCase;
    private final ParchearCategoriaDescripcionUseCase parchearCategoriaDescripcionUseCase;
    private final EliminarCategoriaDescripcionUseCase eliminarCategoriaDescripcionUseCase;
    private final BuscarCategoriaDescripcionUseCase buscarCategoriaDescripcionUseCase;
    private final ListarCategoriaDescripcionesUseCase listarCategoriaDescripcionesUseCase;
    private final CategoriaDescripcionDtoMapper mapper;

    public CategoriaDescripcionController(
            CrearCategoriaDescripcionUseCase crearCategoriaDescripcionUseCase,
            ActualizarCategoriaDescripcionUseCase actualizarCategoriaDescripcionUseCase,
            ParchearCategoriaDescripcionUseCase parchearCategoriaDescripcionUseCase,
            EliminarCategoriaDescripcionUseCase eliminarCategoriaDescripcionUseCase,
            BuscarCategoriaDescripcionUseCase buscarCategoriaDescripcionUseCase,
            ListarCategoriaDescripcionesUseCase listarCategoriaDescripcionesUseCase,
            CategoriaDescripcionDtoMapper mapper) {
        this.crearCategoriaDescripcionUseCase = crearCategoriaDescripcionUseCase;
        this.actualizarCategoriaDescripcionUseCase = actualizarCategoriaDescripcionUseCase;
        this.parchearCategoriaDescripcionUseCase = parchearCategoriaDescripcionUseCase;
        this.eliminarCategoriaDescripcionUseCase = eliminarCategoriaDescripcionUseCase;
        this.buscarCategoriaDescripcionUseCase = buscarCategoriaDescripcionUseCase;
        this.listarCategoriaDescripcionesUseCase = listarCategoriaDescripcionesUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<CategoriaDescripcionResponseDTO> create(@Valid @RequestBody CategoriaDescripcionRequestDTO dto) {
        CategoriaDescripcionDTO result = crearCategoriaDescripcionUseCase.execute(mapper.toCrearCommand(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDescripcionResponseDTO> findById(@PathVariable Long id) {
        Optional<CategoriaDescripcionDTO> result = buscarCategoriaDescripcionUseCase.execute(mapper.toBuscarQuery(id));
        return result.map(dto -> ResponseEntity.ok(mapper.toResponse(dto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDescripcionResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CategoriaDescripcionRequestDTO dto) {
        CategoriaDescripcionDTO result = actualizarCategoriaDescripcionUseCase.execute(mapper.toActualizarCommand(id, dto));
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoriaDescripcionResponseDTO> patch(@PathVariable Long id, @RequestBody CategoriaDescripcionRequestDTO dto) {
        CategoriaDescripcionDTO result = parchearCategoriaDescripcionUseCase.execute(mapper.toParchearCommand(id, dto));
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eliminarCategoriaDescripcionUseCase.execute(mapper.toEliminarCommand(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Pagina<CategoriaDescripcionResponseDTO>> findAllPaginated(
            @RequestParam(required = false) Long categoriaId,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        String orden = pageable.getSort().stream().findFirst()
                .map(Sort.Order::getProperty).orElse(null);
        String direccion = pageable.getSort().stream().findFirst()
                .map(o -> o.getDirection().name().toLowerCase()).orElse(null);
        Paginacion paginacion = Paginacion.of(pageable.getPageNumber(), pageable.getPageSize(), orden, direccion);
        Pagina<CategoriaDescripcionDTO> result = listarCategoriaDescripcionesUseCase.execute(mapper.toListarQuery(categoriaId, paginacion));
        return ResponseEntity.ok(new Pagina<>(
                result.contenido().stream().map(mapper::toResponse).toList(),
                result.pagina(), result.tamanio(), result.totalElementos(), result.totalPaginas()));
    }
}
