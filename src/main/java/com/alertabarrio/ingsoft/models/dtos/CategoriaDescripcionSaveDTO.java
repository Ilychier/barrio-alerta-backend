package com.alertabarrio.ingsoft.models.dtos;

import java.io.Serializable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoriaDescripcionSaveDTO(
    @NotBlank(message = "La descripcion es requerida")
    String descripcion,

    @NotNull(message = "El ID de la categoria asociada es requerido")
    Long categoriaId
) implements Serializable {}
