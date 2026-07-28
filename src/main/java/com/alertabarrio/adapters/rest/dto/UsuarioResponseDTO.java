package com.alertabarrio.adapters.rest.dto;

public record UsuarioResponseDTO(
        Long id,
        String name,
        String email,
        String phone,
        String address,
        Long barrioId
) {
}
