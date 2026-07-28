package com.alertabarrio.application.dto;

import java.time.LocalDateTime;

public record EvidenciaDTO(Long id, String archivoUrl, LocalDateTime fechaSubida, Long alertaId) {
}
