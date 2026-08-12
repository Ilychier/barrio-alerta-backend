package com.alertabarrio.adapters.rest.dto.mascotas;

import java.time.LocalDateTime;

/**
 * Response para "mis reportes" (usuario autenticado): incluye todos
 * los campos, incluso DELETED y teléfono siempre visible.
 */
public record ReporteMascotaResponseDTO(
        Long id,
        String tipoReporte,
        Long tipoMascotaId,
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
