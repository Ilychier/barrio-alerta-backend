package com.alertabarrio.adapters.rest.controller.mascotas;

import com.alertabarrio.adapters.rest.dto.mascotas.*;
import com.alertabarrio.adapters.rest.mapper.mascotas.ReporteMascotaDtoMapper;
import com.alertabarrio.application.dto.mascotas.ReporteMascotaDTO;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import com.alertabarrio.domain.port.in.mascotas.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints AUTHENTICATED del BC Mascotas (protegidos por AuthInterceptor).
 * <p>
 * Rutas públicas (feed, catálogos) viven en {@link ReporteMascotaPublicoController}.
 */
@RestController
@RequestMapping("/api/mascotas/reportes")
public class ReporteMascotaController {

    private final CrearReporteMascotaUseCase crearReporteUseCase;
    private final ActualizarReporteMascotaUseCase actualizarReporteUseCase;
    private final CambiarEstadoReporteMascotaUseCase cambiarEstadoUseCase;
    private final EliminarReporteMascotaUseCase eliminarReporteUseCase;
    private final BuscarReporteMascotaUseCase buscarReporteUseCase;
    private final ListarMisReportesMascotaUseCase listarMisReportesUseCase;
    private final ReporteMascotaDtoMapper mapper;

    public ReporteMascotaController(
            CrearReporteMascotaUseCase crearReporteUseCase,
            ActualizarReporteMascotaUseCase actualizarReporteUseCase,
            CambiarEstadoReporteMascotaUseCase cambiarEstadoUseCase,
            EliminarReporteMascotaUseCase eliminarReporteUseCase,
            BuscarReporteMascotaUseCase buscarReporteUseCase,
            ListarMisReportesMascotaUseCase listarMisReportesUseCase,
            ReporteMascotaDtoMapper mapper) {
        this.crearReporteUseCase = crearReporteUseCase;
        this.actualizarReporteUseCase = actualizarReporteUseCase;
        this.cambiarEstadoUseCase = cambiarEstadoUseCase;
        this.eliminarReporteUseCase = eliminarReporteUseCase;
        this.buscarReporteUseCase = buscarReporteUseCase;
        this.listarMisReportesUseCase = listarMisReportesUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ReporteMascotaResponseDTO> create(@RequestBody CrearReporteMascotaRequestDTO dto) {
        ReporteMascotaDTO result = crearReporteUseCase.execute(mapper.toCrearCommand(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReporteMascotaResponseDTO> findById(@PathVariable Long id) {
        ReporteMascotaDTO result = buscarReporteUseCase.execute(mapper.toBuscarQuery(id));
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @GetMapping("/mios")
    public ResponseEntity<Pagina<ReporteMascotaResponseDTO>> findMine(
            @RequestParam Long usuarioId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        String orden = pageable.getSort().stream().findFirst()
                .map(Sort.Order::getProperty).orElse(null);
        String direccion = pageable.getSort().stream().findFirst()
                .map(o -> o.getDirection().name().toLowerCase()).orElse(null);
        Paginacion paginacion = Paginacion.of(pageable.getPageNumber(), pageable.getPageSize(), orden, direccion);
        Pagina<ReporteMascotaDTO> result = listarMisReportesUseCase.execute(usuarioId, paginacion);
        return ResponseEntity.ok(new Pagina<>(
                result.contenido().stream().map(mapper::toResponse).toList(),
                result.pagina(), result.tamanio(), result.totalElementos(), result.totalPaginas()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReporteMascotaResponseDTO> update(@PathVariable Long id,
                                                            @RequestBody ActualizarReporteMascotaRequestDTO dto) {
        ReporteMascotaDTO result = actualizarReporteUseCase.execute(mapper.toActualizarCommand(id, dto));
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ReporteMascotaResponseDTO> changeState(@PathVariable Long id,
                                                                 @RequestBody CambiarEstadoReporteMascotaRequestDTO dto) {
        ReporteMascotaDTO result = cambiarEstadoUseCase.execute(mapper.toCambiarEstadoCommand(id, dto));
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eliminarReporteUseCase.execute(mapper.toEliminarCommand(id));
        return ResponseEntity.noContent().build();
    }
}
