package com.alertabarrio.adapters.rest.controller.mascotas;

import com.alertabarrio.adapters.rest.dto.mascotas.CiudadResponseDTO;
import com.alertabarrio.adapters.rest.mapper.mascotas.CiudadDtoMapper;
import com.alertabarrio.application.dto.mascotas.CiudadDTO;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import com.alertabarrio.domain.port.in.mascotas.ListarCiudadesUseCase;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Catálogo de ciudades del BC Mascotas (público).
 * Consulta sin autenticación.
 */
@RestController
@RequestMapping("/api/mascotas/public/ciudades")
public class CiudadPublicoController {

    private final ListarCiudadesUseCase listarCiudadesUseCase;
    private final CiudadDtoMapper mapper;

    public CiudadPublicoController(ListarCiudadesUseCase listarCiudadesUseCase, CiudadDtoMapper mapper) {
        this.listarCiudadesUseCase = listarCiudadesUseCase;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<Pagina<CiudadResponseDTO>> findAll(
            @PageableDefault(sort = "nombre", direction = Sort.Direction.ASC) Pageable pageable) {
        String orden = pageable.getSort().stream().findFirst()
                .map(Sort.Order::getProperty).orElse(null);
        String direccion = pageable.getSort().stream().findFirst()
                .map(o -> o.getDirection().name().toLowerCase()).orElse(null);
        Paginacion paginacion = Paginacion.of(pageable.getPageNumber(), pageable.getPageSize(), orden, direccion);
        Pagina<CiudadDTO> result = listarCiudadesUseCase.execute(paginacion);
        return ResponseEntity.ok(mapper.toResponsePage(result));
    }
}
