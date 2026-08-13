package com.alertabarrio.adapters.rest.controller.mascotas;

import com.alertabarrio.adapters.rest.dto.mascotas.TipoMascotaResponseDTO;
import com.alertabarrio.adapters.rest.mapper.mascotas.TipoMascotaDtoMapper;
import com.alertabarrio.application.dto.mascotas.TipoMascotaDTO;
import com.alertabarrio.domain.port.in.mascotas.ListarTiposMascotaUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Catálogo de tipos de mascota del BC Mascotas (público).
 * Consulta sin autenticación.
 */
@RestController
@RequestMapping("/api/mascotas/public/tipos-mascota")
public class TipoMascotaPublicoController {

    private final ListarTiposMascotaUseCase listarTiposMascotaUseCase;
    private final TipoMascotaDtoMapper mapper;

    public TipoMascotaPublicoController(ListarTiposMascotaUseCase listarTiposMascotaUseCase,
                                        TipoMascotaDtoMapper mapper) {
        this.listarTiposMascotaUseCase = listarTiposMascotaUseCase;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<TipoMascotaResponseDTO>> findAll() {
        List<TipoMascotaDTO> result = listarTiposMascotaUseCase.execute();
        return ResponseEntity.ok(mapper.toResponseList(result));
    }
}
