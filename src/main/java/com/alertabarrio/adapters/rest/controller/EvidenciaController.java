package com.alertabarrio.adapters.rest.controller;

import com.alertabarrio.adapters.rest.dto.EvidenciaRequestDTO;
import com.alertabarrio.adapters.rest.dto.EvidenciaResponseDTO;
import com.alertabarrio.adapters.rest.mapper.EvidenciaDtoMapper;
import com.alertabarrio.application.dto.EvidenciaDTO;
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
@RequestMapping("/api/evidencias")
public class EvidenciaController {

    private final CrearEvidenciaUseCase crearEvidenciaUseCase;
    private final ActualizarEvidenciaUseCase actualizarEvidenciaUseCase;
    private final ParchearEvidenciaUseCase parchearEvidenciaUseCase;
    private final EliminarEvidenciaUseCase eliminarEvidenciaUseCase;
    private final BuscarEvidenciaUseCase buscarEvidenciaUseCase;
    private final ListarEvidenciasUseCase listarEvidenciasUseCase;
    private final EvidenciaDtoMapper mapper;

    public EvidenciaController(
            CrearEvidenciaUseCase crearEvidenciaUseCase,
            ActualizarEvidenciaUseCase actualizarEvidenciaUseCase,
            ParchearEvidenciaUseCase parchearEvidenciaUseCase,
            EliminarEvidenciaUseCase eliminarEvidenciaUseCase,
            BuscarEvidenciaUseCase buscarEvidenciaUseCase,
            ListarEvidenciasUseCase listarEvidenciasUseCase,
            EvidenciaDtoMapper mapper) {
        this.crearEvidenciaUseCase = crearEvidenciaUseCase;
        this.actualizarEvidenciaUseCase = actualizarEvidenciaUseCase;
        this.parchearEvidenciaUseCase = parchearEvidenciaUseCase;
        this.eliminarEvidenciaUseCase = eliminarEvidenciaUseCase;
        this.buscarEvidenciaUseCase = buscarEvidenciaUseCase;
        this.listarEvidenciasUseCase = listarEvidenciasUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<EvidenciaResponseDTO> create(@Valid @RequestBody EvidenciaRequestDTO dto) {
        EvidenciaDTO result = crearEvidenciaUseCase.execute(mapper.toCrearCommand(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvidenciaResponseDTO> findById(@PathVariable Long id) {
        EvidenciaDTO result = buscarEvidenciaUseCase.execute(mapper.toBuscarQuery(id));
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvidenciaResponseDTO> update(@PathVariable Long id, @Valid @RequestBody EvidenciaRequestDTO dto) {
        EvidenciaDTO result = actualizarEvidenciaUseCase.execute(mapper.toActualizarCommand(id, dto));
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EvidenciaResponseDTO> patch(@PathVariable Long id, @RequestBody EvidenciaRequestDTO dto) {
        EvidenciaDTO result = parchearEvidenciaUseCase.execute(mapper.toParchearCommand(id, dto));
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eliminarEvidenciaUseCase.execute(mapper.toEliminarCommand(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Pagina<EvidenciaResponseDTO>> findAllPaginated(
            @RequestParam(required = false) Long alertaId,
            @PageableDefault(sort = "fechaSubida", direction = Sort.Direction.DESC) Pageable pageable) {
        String orden = pageable.getSort().stream().findFirst()
                .map(Sort.Order::getProperty).orElse(null);
        String direccion = pageable.getSort().stream().findFirst()
                .map(o -> o.getDirection().name().toLowerCase()).orElse(null);
        Paginacion paginacion = Paginacion.of(pageable.getPageNumber(), pageable.getPageSize(), orden, direccion);
        Pagina<EvidenciaDTO> result = listarEvidenciasUseCase.execute(mapper.toListarQuery(alertaId, paginacion));
        return ResponseEntity.ok(new Pagina<>(
                result.contenido().stream().map(mapper::toResponse).toList(),
                result.pagina(), result.tamanio(), result.totalElementos(), result.totalPaginas()));
    }
}
