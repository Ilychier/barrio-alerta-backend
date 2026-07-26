package com.alertabarrio.adapters.rest.dto;

public record AuthResponseDTO(
        String token,
        UsuarioResponseDTO user
) {
}
