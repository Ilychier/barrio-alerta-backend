package com.alertabarrio.ingsoft.models.dtos;

import java.io.Serializable;

public record EvidenciaResponseDTO(
    Long id,
    String archivoUrl,
    java.time.LocalDateTime fechaSubida,
    Long alertaId
) implements Serializable {}
