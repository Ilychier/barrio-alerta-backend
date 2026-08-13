package com.alertabarrio.application.dto.mascotas;

import java.time.LocalDateTime;

/**
 * DTO de aplicación para un reporte de mascota.
 * <p>
 * El adapter REST decide cómo exponerlo (público oculta teléfono
 * cuando estado = RESCUED, según Ley 1581 de 2012).
 */
public record ReporteMascotaDTO(
        Long id,
        String tipoReporte,
        Long tipoMascotaId,
        String otroTipoMascota,
        String fotoUrl,
        Long ciudadId,
        String ubicacion,
        String telefono,
        String descripcion,
        String estado,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long usuarioId
) {
}
