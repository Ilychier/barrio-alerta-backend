package com.alertabarrio.adapters.rest.dto.mascotas;

public record TipoMascotaResponseDTO(
        Long id,
        String nombre,
        boolean activo
) {
}
