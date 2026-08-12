package com.alertabarrio.application.command.mascotas;

public record CambiarEstadoReporteMascotaCommand(
        Long id,
        String estado
) {
}
