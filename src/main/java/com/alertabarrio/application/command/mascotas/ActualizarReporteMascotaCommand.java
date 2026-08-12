package com.alertabarrio.application.command.mascotas;

public record ActualizarReporteMascotaCommand(
        Long id,
        String ubicacion,
        String telefono,
        String descripcion
) {
}
