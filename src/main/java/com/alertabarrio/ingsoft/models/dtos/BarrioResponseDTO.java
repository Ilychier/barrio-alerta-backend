package com.alertabarrio.ingsoft.models.dtos;

import java.io.Serializable;

public record BarrioResponseDTO(
    Long id,
    String nombre,
    CuadranteResponseDTO cuadrante
) implements Serializable {}