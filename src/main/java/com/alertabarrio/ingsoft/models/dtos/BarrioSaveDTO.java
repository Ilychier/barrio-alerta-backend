package com.alertabarrio.ingsoft.models.dtos;

import java.io.Serializable;
import jakarta.validation.constraints.*;

public record BarrioSaveDTO(
    @NotBlank(message = "El nombre del barrio es requerido")
    @Size(min = 2, max = 100, message = "El nombre del barrio debe tener entre 2 y 100 caracteres")
    String nombre,

    @NotNull(message = "El ID del cuadrante es requerido")
    Long cuadranteId
) implements Serializable {}