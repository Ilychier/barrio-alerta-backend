package com.alertabarrio.adapters.rest.dto;

import java.time.LocalDateTime;

public record EvidenciaResponseDTO(
        Long id,
        String archivoUrl,
        LocalDateTime fechaSubida,
        Long alertaId
) {
}
