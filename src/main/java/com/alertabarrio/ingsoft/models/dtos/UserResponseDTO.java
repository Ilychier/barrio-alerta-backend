package com.alertabarrio.ingsoft.models.dtos;

import java.io.Serializable;

public record UserResponseDTO(
    Long id,
    String name,
    String email,
    String phone,
    String address,
    Long barrioId
) implements Serializable {}
