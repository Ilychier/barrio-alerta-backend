package com.alertabarrio.adapters.rest.dto;

import java.time.LocalDateTime;

public record AlertaResponseDTO(
        Long id,
        String descripcion,
        Boolean esSos,
        LocalDateTime fechaHora,
        Long usuarioId,
        Long categoriaId
) {
}
