package com.alertabarrio.ingsoft.models.dtos;

import java.io.Serializable;
import jakarta.validation.constraints.*;

public record EvidenciaSaveDTO(
    @NotBlank(message = "La ruta del archivo de evidencia es requerida")
    String archivoUrl,

    @NotNull(message = "El ID de la alerta asociada es requerido")
    Long alertaId
) implements Serializable {}
