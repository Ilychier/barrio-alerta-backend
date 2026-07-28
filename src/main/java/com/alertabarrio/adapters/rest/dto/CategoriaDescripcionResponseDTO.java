package com.alertabarrio.adapters.rest.dto;

public record CategoriaDescripcionResponseDTO(
        Long id,
        String descripcion,
        Long categoriaId,
        String imagenUrl
) {
}
