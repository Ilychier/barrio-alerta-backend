package com.alertabarrio.ingsoft.models.dtos;

import java.io.Serializable;
import jakarta.validation.constraints.*;

public record ConfiguracionSaveDTO(
    @NotNull(message = "El ID del usuario es requerido")
    Long usuarioId,

    @NotNull(message = "Debe especificar si recibe notificaciones")
    Boolean recibirNotificaciones,

    @NotNull(message = "Debe especificar el modo silencioso")
    Boolean modoSilencioso
) implements Serializable {}
