package com.alertabarrio.adapters.rest.dto;

public record SesionResponseDTO(
        String token,
        UsuarioResponseDTO user,
        BarrioResponseDTO barrio,
        CuadranteResponseDTO cuadrante,
        ConfiguracionResponseDTO configuracion
) {
}
