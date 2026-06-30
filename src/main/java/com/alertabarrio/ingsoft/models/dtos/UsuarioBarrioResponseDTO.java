package com.alertabarrio.ingsoft.models.dtos;

import java.io.Serializable;

public record UsuarioBarrioResponseDTO(
    Long id,
    UserResponseDTO usuario,
    BarrioResponseDTO barrio
) implements Serializable {}
