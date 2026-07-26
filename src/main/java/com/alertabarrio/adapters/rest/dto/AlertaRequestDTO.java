package com.alertabarrio.adapters.rest.dto;

public record AlertaRequestDTO(
        String descripcion,
        Boolean esSos,
        Long usuarioId,
        Long categoriaId
) {
}
