package com.alertabarrio.ingsoft.models.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;

public record AlertaResponseDTO(
    Long id,
    String descripcion,
    Boolean esSos,
    LocalDateTime fechaHora,
    Long usuarioId,
    CategoriaResponseDTO categoria
) implements Serializable {}
