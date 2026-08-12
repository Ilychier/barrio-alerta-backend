package com.alertabarrio.adapters.rest.dto.mascotas;

public record ActualizarReporteMascotaRequestDTO(
        String ubicacion,
        String telefono,
        String descripcion
) {
}
