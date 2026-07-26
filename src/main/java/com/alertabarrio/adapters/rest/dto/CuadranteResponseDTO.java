package com.alertabarrio.adapters.rest.dto;

public record CuadranteResponseDTO(
        Long id,
        String nombreUnidad,
        String telefonoEmergencia,
        String emailEmergencia
) {
}
