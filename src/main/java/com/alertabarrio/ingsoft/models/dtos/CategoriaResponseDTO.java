package com.alertabarrio.ingsoft.models.dtos;

import java.io.Serializable;

public record CategoriaResponseDTO(
    Long id,
    String nombre,
    String iconoReferencia
) implements Serializable {}