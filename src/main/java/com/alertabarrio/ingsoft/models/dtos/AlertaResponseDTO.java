package com.alertabarrio.ingsoft.models.dtos;

import java.io.Serializable;

public record AlertaResponseDTO(
    Long id,
    String tipo,
    String descripcion,
    String ubicacion,
    java.time.LocalDateTime fechaHora,
    Long usuarioId
) implements Serializable {}
