package com.alertabarrio.ingsoft.models.dtos;

import java.io.Serializable;

public record CategoriaDescripcionResponseDTO(
    Long id,
    String descripcion,
    Long categoriaId
) implements Serializable {}
