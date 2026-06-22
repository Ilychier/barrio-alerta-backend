package com.alertabarrio.ingsoft.models.dtos;

import java.io.Serializable;

public record CuadranteResponseDTO(
    Long id,
    String nombreUnidad,
    String telefonoEmergencia
) implements Serializable {}