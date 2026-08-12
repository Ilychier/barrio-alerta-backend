package com.alertabarrio.adapters.rest.dto.mascotas;

public record CrearReporteMascotaRequestDTO(
        String tipoReporte,      // LOST | FOUND
        Long tipoMascotaId,
        Long ciudadId,
        String ubicacion,
        String telefono,         // +573001234567
        String descripcion,      // opcional
        Long usuarioId           // dueño del reporte (autenticado)
) {
}
