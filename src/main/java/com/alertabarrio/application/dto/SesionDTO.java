package com.alertabarrio.application.dto;

public record SesionDTO(
        String token,
        UsuarioDTO user,
        BarrioDTO barrio,
        CuadranteDTO cuadrante,
        ConfiguracionDTO configuracion,
        String ciudadNombre,
        String paisNombre
) {
}
