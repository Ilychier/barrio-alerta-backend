package com.alertabarrio.adapters.rest.dto.mascotas;

import java.time.LocalDateTime;

/**
 * Response público de un reporte de mascota.
 * <p>
 * Privacidad (Ley 1581 de 2012): cuando {@code estado=RESCUED},
 * {@code telefono} es null (el caso ya se resolvió, no se expone
 * el dato de contacto del ciudadano).
 * <p>
 * Los nombres de catálogos se resuelven en el frontend (que carga
 * los catálogos completos por separado). Aquí van los IDs crudos.
 */
public record ReporteMascotaPublicoResponseDTO(
        Long id,
        String tipoReporte,
        Long tipoMascotaId,      // resuelve nombre via GET /public/tipos-mascota
        String otroTipoMascota,  // texto libre cuando tipoMascotaId = "Otro"
        Long ciudadId,           // resuelve nombre via GET /public/ciudades
        String ubicacion,
        String telefono,         // null si estado=RESCUED
        String descripcion,
        String estado,
        LocalDateTime createdAt
) {
}
