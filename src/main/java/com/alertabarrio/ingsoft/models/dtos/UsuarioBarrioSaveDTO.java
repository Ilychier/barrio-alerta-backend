package com.alertabarrio.ingsoft.models.dtos;

import java.io.Serializable;
import jakarta.validation.constraints.NotNull;

public record UsuarioBarrioSaveDTO(
    @NotNull(message = "El ID del usuario es requerido")
    Long usuarioId,

    @NotNull(message = "El ID del barrio es requerido")
    Long barrioId
) implements Serializable {}
