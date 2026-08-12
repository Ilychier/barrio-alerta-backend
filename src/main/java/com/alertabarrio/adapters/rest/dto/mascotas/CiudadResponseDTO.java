package com.alertabarrio.adapters.rest.dto.mascotas;

public record CiudadResponseDTO(
        Long id,
        String nombre,
        String departamento,
        String pais
) {
}
