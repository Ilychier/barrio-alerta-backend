package com.alertabarrio.application.command.mascotas;

public record CrearReporteMascotaCommand(
        String tipoReporte,
        Long tipoMascotaId,
        String otroTipoMascota,
        Long ciudadId,
        String ubicacion,
        String telefono,
        String descripcion,
        Long usuarioId
) {
}
