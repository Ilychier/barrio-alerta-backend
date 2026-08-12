package com.alertabarrio.adapters.rest.controller.mascotas;

import com.alertabarrio.adapters.rest.dto.mascotas.ReporteMascotaPublicoResponseDTO;
import com.alertabarrio.adapters.rest.mapper.mascotas.ReporteMascotaDtoMapper;
import com.alertabarrio.application.dto.mascotas.ReporteMascotaDTO;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import com.alertabarrio.domain.port.in.mascotas.BuscarReporteMascotaUseCase;
import com.alertabarrio.domain.port.in.mascotas.ListarReportesMascotaUseCase;
import com.alertabarrio.domain.port.in.mascotas.ListarReportesRescatadosUseCase;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints PUBLICOS del BC Mascotas (excluidos de AuthInterceptor en WebConfig).
 * <p>
 * Consulta sin autenticación: feed, filtros, detalle e historias de rescate.
 * El response oculta el teléfono cuando el reporte está RESCUED (Ley 1581).
 */
@RestController
@RequestMapping("/api/mascotas/public")
public class ReporteMascotaPublicoController {

    private final ListarReportesMascotaUseCase listarReportesUseCase;
    private final ListarReportesRescatadosUseCase listarRescatadosUseCase;
    private final BuscarReporteMascotaUseCase buscarReporteUseCase;
    private final ReporteMascotaDtoMapper mapper;

    public ReporteMascotaPublicoController(
            ListarReportesMascotaUseCase listarReportesUseCase,
            ListarReportesRescatadosUseCase listarRescatadosUseCase,
            BuscarReporteMascotaUseCase buscarReporteUseCase,
            ReporteMascotaDtoMapper mapper) {
        this.listarReportesUseCase = listarReportesUseCase;
        this.listarRescatadosUseCase = listarRescatadosUseCase;
        this.buscarReporteUseCase = buscarReporteUseCase;
        this.mapper = mapper;
    }

    @GetMapping("/reportes")
    public ResponseEntity<Pagina<ReporteMascotaPublicoResponseDTO>> findAll(
            @RequestParam(required = false) String estado,        // ACTIVE | RESCUED
            @RequestParam(required = false) String tipoReporte,   // LOST | FOUND
            @RequestParam(required = false) Long ciudadId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        String orden = pageable.getSort().stream().findFirst()
                .map(Sort.Order::getProperty).orElse(null);
        String direccion = pageable.getSort().stream().findFirst()
                .map(o -> o.getDirection().name().toLowerCase()).orElse(null);
        Paginacion paginacion = Paginacion.of(pageable.getPageNumber(), pageable.getPageSize(), orden, direccion);
        Pagina<ReporteMascotaDTO> result = listarReportesUseCase.execute(
                mapper.toListarQuery(paginacion, estado, tipoReporte, ciudadId));
        return ResponseEntity.ok(mapper.toPublicoResponsePage(result));
    }

    @GetMapping("/reportes/rescatados")
    public ResponseEntity<Pagina<ReporteMascotaPublicoResponseDTO>> findRescatados(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        String orden = pageable.getSort().stream().findFirst()
                .map(Sort.Order::getProperty).orElse(null);
        String direccion = pageable.getSort().stream().findFirst()
                .map(o -> o.getDirection().name().toLowerCase()).orElse(null);
        Paginacion paginacion = Paginacion.of(pageable.getPageNumber(), pageable.getPageSize(), orden, direccion);
        Pagina<ReporteMascotaDTO> result = listarRescatadosUseCase.execute(paginacion);
        return ResponseEntity.ok(mapper.toPublicoResponsePage(result));
    }

    @GetMapping("/reportes/{id}")
    public ResponseEntity<ReporteMascotaPublicoResponseDTO> findById(@PathVariable Long id) {
        ReporteMascotaDTO result = buscarReporteUseCase.execute(mapper.toBuscarQuery(id));
        return ResponseEntity.ok(mapper.toPublicoResponse(result));
    }
}
