package com.alertabarrio.adapters.rest.controller;

import com.alertabarrio.adapters.rest.dto.ConfiguracionRequestDTO;
import com.alertabarrio.adapters.rest.dto.ConfiguracionResponseDTO;
import com.alertabarrio.adapters.rest.mapper.ConfiguracionDtoMapper;
import com.alertabarrio.application.dto.ConfiguracionDTO;
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
@RequestMapping("/api/configuraciones")
public class ConfiguracionController {

    private final CrearConfiguracionUseCase crearConfiguracionUseCase;
    private final ObtenerConfiguracionUseCase obtenerConfiguracionUseCase;
    private final ActualizarConfiguracionUseCase actualizarConfiguracionUseCase;
    private final EliminarConfiguracionUseCase eliminarConfiguracionUseCase;
    private final ListarConfiguracionesUseCase listarConfiguracionesUseCase;
    private final ConfiguracionDtoMapper mapper;

    public ConfiguracionController(
            CrearConfiguracionUseCase crearConfiguracionUseCase,
            ObtenerConfiguracionUseCase obtenerConfiguracionUseCase,
            ActualizarConfiguracionUseCase actualizarConfiguracionUseCase,
            EliminarConfiguracionUseCase eliminarConfiguracionUseCase,
            ListarConfiguracionesUseCase listarConfiguracionesUseCase,
            ConfiguracionDtoMapper mapper) {
        this.crearConfiguracionUseCase = crearConfiguracionUseCase;
        this.obtenerConfiguracionUseCase = obtenerConfiguracionUseCase;
        this.actualizarConfiguracionUseCase = actualizarConfiguracionUseCase;
        this.eliminarConfiguracionUseCase = eliminarConfiguracionUseCase;
        this.listarConfiguracionesUseCase = listarConfiguracionesUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ConfiguracionResponseDTO> create(@Valid @RequestBody ConfiguracionRequestDTO dto) {
        ConfiguracionDTO result = crearConfiguracionUseCase.execute(mapper.toCrearCommand(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConfiguracionResponseDTO> findById(@PathVariable Long id) {
        ConfiguracionDTO result = obtenerConfiguracionUseCase.execute(mapper.toObtenerQuery(id));
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConfiguracionResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ConfiguracionRequestDTO dto) {
        ConfiguracionDTO result = actualizarConfiguracionUseCase.execute(mapper.toActualizarCommand(id, dto));
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eliminarConfiguracionUseCase.execute(mapper.toEliminarCommand(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Pagina<ConfiguracionResponseDTO>> findAllPaginated(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Paginacion paginacion = Paginacion.of(pageable.getPageNumber(), pageable.getPageSize(),
                pageable.getSort().toString().isEmpty() ? null : pageable.getSort().toString(), null);
        Pagina<ConfiguracionDTO> result = listarConfiguracionesUseCase.execute(mapper.toListarQuery(paginacion));
        return ResponseEntity.ok(new Pagina<>(
                result.contenido().stream().map(mapper::toResponse).toList(),
                result.pagina(), result.tamanio(), result.totalElementos(), result.totalPaginas()));
    }
}
