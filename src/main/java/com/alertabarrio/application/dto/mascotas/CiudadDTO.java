package com.alertabarrio.application.dto.mascotas;

public record CiudadDTO(
        Long id,
        String nombre,
        String departamento,
        String pais
) {
}
