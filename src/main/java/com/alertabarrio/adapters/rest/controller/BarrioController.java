package com.alertabarrio.adapters.rest.controller;

import com.alertabarrio.adapters.rest.dto.BarrioRequestDTO;
import com.alertabarrio.adapters.rest.dto.BarrioResponseDTO;
import com.alertabarrio.adapters.rest.mapper.BarrioDtoMapper;
import com.alertabarrio.application.dto.BarrioDTO;
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

@RestController
@RequestMapping("/api/barrios")
public class BarrioController {

    private final CrearBarrioUseCase crearBarrioUseCase;
    private final ActualizarBarrioUseCase actualizarBarrioUseCase;
    private final ParchearBarrioUseCase parchearBarrioUseCase;
    private final EliminarBarrioUseCase eliminarBarrioUseCase;
    private final BuscarBarrioUseCase buscarBarrioUseCase;
    private final ListarBarriosUseCase listarBarriosUseCase;
    private final BarrioDtoMapper mapper;

    public BarrioController(
            CrearBarrioUseCase crearBarrioUseCase,
            ActualizarBarrioUseCase actualizarBarrioUseCase,
            ParchearBarrioUseCase parchearBarrioUseCase,
            EliminarBarrioUseCase eliminarBarrioUseCase,
            BuscarBarrioUseCase buscarBarrioUseCase,
            ListarBarriosUseCase listarBarriosUseCase,
            BarrioDtoMapper mapper) {
        this.crearBarrioUseCase = crearBarrioUseCase;
        this.actualizarBarrioUseCase = actualizarBarrioUseCase;
        this.parchearBarrioUseCase = parchearBarrioUseCase;
        this.eliminarBarrioUseCase = eliminarBarrioUseCase;
        this.buscarBarrioUseCase = buscarBarrioUseCase;
        this.listarBarriosUseCase = listarBarriosUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<BarrioResponseDTO> create(@Valid @RequestBody BarrioRequestDTO dto) {
        BarrioDTO result = crearBarrioUseCase.execute(mapper.toCrearCommand(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarrioResponseDTO> findById(@PathVariable Long id) {
        BarrioDTO result = buscarBarrioUseCase.execute(mapper.toBuscarQuery(id));
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarrioResponseDTO> update(@PathVariable Long id, @Valid @RequestBody BarrioRequestDTO dto) {
        BarrioDTO result = actualizarBarrioUseCase.execute(mapper.toActualizarCommand(id, dto));
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BarrioResponseDTO> patch(@PathVariable Long id, @RequestBody BarrioRequestDTO dto) {
        BarrioDTO result = parchearBarrioUseCase.execute(mapper.toParchearCommand(id, dto));
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eliminarBarrioUseCase.execute(mapper.toEliminarCommand(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Pagina<BarrioResponseDTO>> findAllPaginated(
            @PageableDefault(sort = "nombre", direction = Sort.Direction.ASC) Pageable pageable) {
        String orden = pageable.getSort().stream().findFirst()
                .map(Sort.Order::getProperty).orElse(null);
        String direccion = pageable.getSort().stream().findFirst()
                .map(o -> o.getDirection().name().toLowerCase()).orElse(null);
        Paginacion paginacion = Paginacion.of(pageable.getPageNumber(), pageable.getPageSize(), orden, direccion);
        Pagina<BarrioDTO> result = listarBarriosUseCase.execute(mapper.toListarQuery(paginacion));
        return ResponseEntity.ok(new Pagina<>(
                result.contenido().stream().map(mapper::toResponse).toList(),
                result.pagina(), result.tamanio(), result.totalElementos(), result.totalPaginas()));
    }
}
