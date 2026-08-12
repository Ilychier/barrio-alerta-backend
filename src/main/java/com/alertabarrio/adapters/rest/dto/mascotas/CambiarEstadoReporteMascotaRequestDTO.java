package com.alertabarrio.adapters.rest.dto.mascotas;

public record CambiarEstadoReporteMascotaRequestDTO(
        String estado   // ACTIVE | RESCUED | DELETED
) {
}
