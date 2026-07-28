package com.alertabarrio.adapters.rest.dto;

public record UsuarioRequestDTO(
        String name,
        String email,
        String phone,
        String address,
        String password,
        Long barrioId
) {
}
