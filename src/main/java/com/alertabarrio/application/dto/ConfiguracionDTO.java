package com.alertabarrio.application.dto;

public record ConfiguracionDTO(Long id, Long usuarioId, boolean recibirNotificaciones, boolean modoSilencioso) {
}
