package com.alertabarrio.ingsoft.models.dtos;

import java.io.Serializable;
import jakarta.validation.constraints.*;

public record AlertaSaveDTO(
    @NotBlank(message = "El tipo de alerta es requerido")
    String tipo,

    @NotBlank(message = "La descripción es requerida")
    String descripcion,

    @NotBlank(message = "La ubicación es requerida")
    String ubicacion,

    @NotNull(message = "El ID del usuario es requerido")
    Long usuarioId
) implements Serializable {}
