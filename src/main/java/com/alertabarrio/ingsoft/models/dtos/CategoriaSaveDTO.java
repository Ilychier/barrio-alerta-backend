package com.alertabarrio.ingsoft.models.dtos;

import java.io.Serializable;
import jakarta.validation.constraints.*;

public record CategoriaSaveDTO(
    @NotBlank(message = "El nombre de la categoría es requerido")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    String nombre,

    @NotBlank(message = "El icono de referencia es requerido")
    @Size(max = 255, message = "El icono de referencia no debe exceder los 255 caracteres")
    String iconoReferencia
) implements Serializable {}