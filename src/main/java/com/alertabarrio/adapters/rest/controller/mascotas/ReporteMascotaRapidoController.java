package com.alertabarrio.adapters.rest.controller.mascotas;

import com.alertabarrio.adapters.rest.dto.mascotas.RegistrarReporteRapidoRequestDTO;
import com.alertabarrio.adapters.rest.dto.mascotas.RegistrarReporteRapidoResponseDTO;
import com.alertabarrio.adapters.rest.mapper.mascotas.ReporteMascotaDtoMapper;
import com.alertabarrio.application.command.mascotas.RegistrarReporteRapidoCommand;
import com.alertabarrio.application.dto.mascotas.ReporteRapidoResultDTO;
import com.alertabarrio.domain.port.in.mascotas.RegistrarReporteRapidoUseCase;
import com.alertabarrio.domain.port.out.AlmacenamientoArchivoPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Endpoint PÚBLICO del registro rápido de emergencia (BC Mascotas).
 * <p>
 * Vive bajo {@code /api/mascotas/public/**} para heredar la exclusión del
 * AuthInterceptor en WebConfig (cero cambios de seguridad).
 * <p>
 * Un solo round-trip: registra (o reutiliza) el usuario por su celular y crea
 * el reporte del animalito atómicamente (Facade transaccional). Devuelve el
 * JWT para auto-login cuando el usuario es nuevo o tiene clave temporal.
 */
@RestController
@RequestMapping("/api/mascotas/public/reportes")
public class ReporteMascotaRapidoController {

    private final RegistrarReporteRapidoUseCase registrarRapidoUseCase;
    private final AlmacenamientoArchivoPort almacenamiento;
    private final ReporteMascotaDtoMapper mapper;

    /**
     * ObjectMapper local (stateless y thread-safe). No se inyecta como bean
     * porque spring-boot-starter-webmvc (Boot 4 modular) no lo auto-configura.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ReporteMascotaRapidoController(
            RegistrarReporteRapidoUseCase registrarRapidoUseCase,
            AlmacenamientoArchivoPort almacenamiento,
            ReporteMascotaDtoMapper mapper) {
        this.registrarRapidoUseCase = registrarRapidoUseCase;
        this.almacenamiento = almacenamiento;
        this.mapper = mapper;
    }

    @PostMapping(value = "/rapido", consumes = "multipart/form-data")
    public ResponseEntity<RegistrarReporteRapidoResponseDTO> registrarRapido(
            @RequestPart("datos") String datosJson,
            @RequestPart(value = "foto", required = false) MultipartFile foto) throws IOException {
        RegistrarReporteRapidoRequestDTO dto = objectMapper.readValue(datosJson, RegistrarReporteRapidoRequestDTO.class);

        String fotoUrl = null;
        if (foto != null && !foto.isEmpty()) {
            fotoUrl = almacenamiento.guardarImagen(foto.getBytes(), foto.getContentType());
        }

        RegistrarReporteRapidoCommand command = new RegistrarReporteRapidoCommand(
                dto.phonePersonal(),
                dto.telefonoContacto(),
                dto.tipoReporte(),
                dto.tipoMascotaId(),
                dto.otroTipoMascota(),
                dto.ciudadId(),
                dto.ubicacion(),
                dto.descripcion(),
                fotoUrl);

        ReporteRapidoResultDTO result = registrarRapidoUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RegistrarReporteRapidoResponseDTO(
                        mapper.toPublicoResponse(result.reporte()),
                        result.token(),
                        result.passwordTemporal(),
                        result.esNuevo()));
    }
}
