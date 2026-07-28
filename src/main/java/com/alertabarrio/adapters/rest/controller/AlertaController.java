package com.alertabarrio.adapters.rest.controller;

import com.alertabarrio.adapters.rest.dto.AlertaRequestDTO;
import com.alertabarrio.adapters.rest.dto.AlertaResponseDTO;
import com.alertabarrio.adapters.rest.mapper.AlertaDtoMapper;
import com.alertabarrio.application.dto.AlertaDTO;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import com.alertabarrio.domain.port.in.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/alertas")
public class AlertaController {

    private final CrearAlertaUseCase crearAlertaUseCase;
    private final ActualizarAlertaUseCase actualizarAlertaUseCase;
    private final ParchearAlertaUseCase parchearAlertaUseCase;
    private final EliminarAlertaUseCase eliminarAlertaUseCase;
    private final BuscarAlertaUseCase buscarAlertaUseCase;
    private final ListarAlertasUseCase listarAlertasUseCase;
    private final AlertaDtoMapper mapper;

    public AlertaController(
            CrearAlertaUseCase crearAlertaUseCase,
            ActualizarAlertaUseCase actualizarAlertaUseCase,
            ParchearAlertaUseCase parchearAlertaUseCase,
            EliminarAlertaUseCase eliminarAlertaUseCase,
            BuscarAlertaUseCase buscarAlertaUseCase,
            ListarAlertasUseCase listarAlertasUseCase,
            AlertaDtoMapper mapper) {
        this.crearAlertaUseCase = crearAlertaUseCase;
        this.actualizarAlertaUseCase = actualizarAlertaUseCase;
        this.parchearAlertaUseCase = parchearAlertaUseCase;
        this.eliminarAlertaUseCase = eliminarAlertaUseCase;
        this.buscarAlertaUseCase = buscarAlertaUseCase;
        this.listarAlertasUseCase = listarAlertasUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<AlertaResponseDTO> create(@Valid @RequestBody AlertaRequestDTO dto) {
        AlertaDTO result = crearAlertaUseCase.execute(mapper.toCrearCommand(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertaResponseDTO> findById(@PathVariable Long id) {
        AlertaDTO result = buscarAlertaUseCase.execute(mapper.toBuscarQuery(id));
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlertaResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AlertaRequestDTO dto) {
        AlertaDTO result = actualizarAlertaUseCase.execute(mapper.toActualizarCommand(id, dto));
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AlertaResponseDTO> patch(@PathVariable Long id, @RequestBody AlertaRequestDTO dto) {
        AlertaDTO result = parchearAlertaUseCase.execute(mapper.toParchearCommand(id, dto));
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eliminarAlertaUseCase.execute(mapper.toEliminarCommand(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Pagina<AlertaResponseDTO>> findAllPaginated(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) Long barrioId,
            @PageableDefault(sort = "fechaHora", direction = Sort.Direction.DESC) Pageable pageable) {
        String orden = pageable.getSort().stream().findFirst()
                .map(Sort.Order::getProperty).orElse(null);
        String direccion = pageable.getSort().stream().findFirst()
                .map(o -> o.getDirection().name().toLowerCase()).orElse(null);
        Paginacion paginacion = Paginacion.of(pageable.getPageNumber(), pageable.getPageSize(), orden, direccion);
        Pagina<AlertaDTO> result = listarAlertasUseCase.execute(mapper.toListarQuery(paginacion, fecha, barrioId));
        return ResponseEntity.ok(new Pagina<>(
                result.contenido().stream().map(mapper::toResponse).toList(),
                result.pagina(), result.tamanio(), result.totalElementos(), result.totalPaginas()));
    }
}
