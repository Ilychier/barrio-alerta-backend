package com.alertabarrio.ingsoft.models.dtos;

import java.io.Serializable;
import jakarta.validation.constraints.*;

public record AlertaSaveDTO(
    @NotBlank(message = "La descripción es requerida")
    String descripcion,

    @NotNull(message = "El indicador de S.O.S es requerido")
    Boolean esSos,

    @NotNull(message = "El ID del usuario es requerido")
    Long usuarioId,

    @NotNull(message = "El ID de la categoría es requerido")
    Long categoriaId
) implements Serializable {}
