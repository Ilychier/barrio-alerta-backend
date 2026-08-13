package com.alertabarrio.adapters.rest.controller.mascotas;

import com.alertabarrio.adapters.rest.dto.mascotas.*;
import com.alertabarrio.adapters.rest.mapper.mascotas.ReporteMascotaDtoMapper;
import com.alertabarrio.application.dto.mascotas.ReporteMascotaDTO;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import com.alertabarrio.domain.port.in.mascotas.*;
import com.alertabarrio.domain.port.out.AlmacenamientoArchivoPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    private final AlmacenamientoArchivoPort almacenamiento;
    private final ReporteMascotaDtoMapper mapper;

    /**
     * ObjectMapper local (stateless y thread-safe). No se inyecta como bean
     * porque spring-boot-starter-webmvc (Boot 4 modular) no lo auto-configura.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ReporteMascotaController(
            CrearReporteMascotaUseCase crearReporteUseCase,
            ActualizarReporteMascotaUseCase actualizarReporteUseCase,
            CambiarEstadoReporteMascotaUseCase cambiarEstadoUseCase,
            EliminarReporteMascotaUseCase eliminarReporteUseCase,
            BuscarReporteMascotaUseCase buscarReporteUseCase,
            ListarMisReportesMascotaUseCase listarMisReportesUseCase,
            AlmacenamientoArchivoPort almacenamiento,
            ReporteMascotaDtoMapper mapper) {
        this.crearReporteUseCase = crearReporteUseCase;
        this.actualizarReporteUseCase = actualizarReporteUseCase;
        this.cambiarEstadoUseCase = cambiarEstadoUseCase;
        this.eliminarReporteUseCase = eliminarReporteUseCase;
        this.buscarReporteUseCase = buscarReporteUseCase;
        this.listarMisReportesUseCase = listarMisReportesUseCase;
        this.almacenamiento = almacenamiento;
        this.mapper = mapper;
    }

    /**
     * Crea un reporte con foto opcional (multipart/form-data).
     * <p>
     * El controller (adapter REST) recibe el archivo, delega la persistencia
     * al puerto de almacenamiento y solo inyecta la URL resultante en el
     * command. El dominio nunca ve {@link MultipartFile}.
     * <p>
     * El part "datos" llega como JSON string (React Native lo envía como
     * text/plain) y se deserializa aquí con ObjectMapper.
     */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ReporteMascotaResponseDTO> create(
            @RequestPart("datos") String datosJson,
            @RequestPart(value = "foto", required = false) MultipartFile foto) throws IOException {
        CrearReporteMascotaRequestDTO dto = objectMapper.readValue(datosJson, CrearReporteMascotaRequestDTO.class);
        String fotoUrl = null;
        if (foto != null && !foto.isEmpty()) {
            fotoUrl = almacenamiento.guardarImagen(foto.getBytes(), foto.getContentType());
        }
        ReporteMascotaDTO result = crearReporteUseCase.execute(mapper.toCrearCommand(dto, fotoUrl));
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
