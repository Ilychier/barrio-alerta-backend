package com.alertabarrio.adapters.rest.dto;

public record ConfiguracionResponseDTO(
        Long id,
        Long usuarioId,
        Boolean recibirNotificaciones,
        Boolean modoSilencioso
) {
}
