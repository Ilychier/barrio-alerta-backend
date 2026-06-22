package com.alertabarrio.ingsoft.models.dtos;

import java.io.Serializable;

public record ConfiguracionResponseDTO(
    Long id,
    Long usuarioId,
    Boolean recibirNotificaciones,
    Boolean modoSilencioso
) implements Serializable {}
