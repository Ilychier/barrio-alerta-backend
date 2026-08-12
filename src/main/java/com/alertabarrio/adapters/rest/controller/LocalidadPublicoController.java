package com.alertabarrio.adapters.rest.controller;

import com.alertabarrio.adapters.rest.dto.LocalidadResponseDTO;
import com.alertabarrio.adapters.rest.mapper.LocalidadDtoMapper;
import com.alertabarrio.application.dto.LocalidadDTO;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import com.alertabarrio.domain.port.in.ListarLocalidadesUseCase;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Catálogo de localidades (público, sin autenticación).
 * Consulta las localidades de un municipio: GET /api/public/localidades?municipioId=X
 */
@RestController
@RequestMapping("/api/public/localidades")
public class LocalidadPublicoController {

    private final ListarLocalidadesUseCase listarLocalidadesUseCase;
    private final LocalidadDtoMapper mapper;

    public LocalidadPublicoController(ListarLocalidadesUseCase listarLocalidadesUseCase, LocalidadDtoMapper mapper) {
        this.listarLocalidadesUseCase = listarLocalidadesUseCase;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<Pagina<LocalidadResponseDTO>> findAll(
            @RequestParam Long municipioId,
            @PageableDefault(sort = "nombre", direction = Sort.Direction.ASC) Pageable pageable) {
        String orden = pageable.getSort().stream().findFirst()
                .map(Sort.Order::getProperty).orElse(null);
        String direccion = pageable.getSort().stream().findFirst()
                .map(o -> o.getDirection().name().toLowerCase()).orElse(null);
        Paginacion paginacion = Paginacion.of(pageable.getPageNumber(), pageable.getPageSize(), orden, direccion);
        Pagina<LocalidadDTO> result = listarLocalidadesUseCase.execute(mapper.toListarQuery(municipioId, paginacion));
        return ResponseEntity.ok(mapper.toResponsePage(result));
    }
}
