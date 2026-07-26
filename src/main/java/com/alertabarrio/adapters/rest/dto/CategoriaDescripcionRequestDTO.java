package com.alertabarrio.adapters.rest.dto;

public record CategoriaDescripcionRequestDTO(
        String descripcion,
        Long categoriaId,
        String imagenUrl
) {
}
