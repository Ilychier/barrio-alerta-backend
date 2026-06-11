package com.alertabarrio.ingsoft.models.dtos;

import java.io.Serializable;
import jakarta.validation.constraints.*;

public record CuadranteSaveDTO(
    @NotBlank(message = "El nombre de la unidad es requerido")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    String nombreUnidad,

    @NotBlank(message = "El teléfono de emergencia es requerido")
    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "El teléfono debe tener entre 7 y 15 dígitos")
    String telefonoEmergencia
) implements Serializable {}