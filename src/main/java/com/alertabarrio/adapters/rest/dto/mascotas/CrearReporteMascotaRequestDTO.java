package com.alertabarrio.adapters.rest.dto.mascotas;

public record CrearReporteMascotaRequestDTO(
        String tipoReporte,      // LOST | FOUND
        Long tipoMascotaId,
        String otroTipoMascota,  // opcional: texto libre cuando tipoMascotaId = "Otro"
        Long ciudadId,
        String ubicacion,
        String telefono,         // +573001234567
        String descripcion,      // opcional
        Long usuarioId           // dueño del reporte (autenticado)
) {
}
