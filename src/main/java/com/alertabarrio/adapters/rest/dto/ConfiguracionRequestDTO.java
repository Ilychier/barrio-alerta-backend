package com.alertabarrio.adapters.rest.dto;

public record ConfiguracionRequestDTO(
        Long usuarioId,
        Boolean recibirNotificaciones,
        Boolean modoSilencioso
) {
}
