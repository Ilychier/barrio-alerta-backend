package com.alertabarrio.ingsoft.models.dtos;

import java.io.Serializable;

public record AuthResponseDTO(
    String token,
    UserResponseDTO user
) implements Serializable {}
