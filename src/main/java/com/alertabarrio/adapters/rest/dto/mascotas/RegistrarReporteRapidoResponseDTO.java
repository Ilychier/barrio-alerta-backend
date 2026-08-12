package com.alertabarrio.adapters.rest.dto.mascotas;

/**
 * Response del registro rápido de emergencia.
 * <p>
 * - {@code token}: JWT para auto-login (nuevo o temporal). null si el usuario
 *   existente tiene clave real (debe loguear manualmente).
 * - {@code passwordTemporal}: true si debe cambiar la clave en el primer acceso.
 * - {@code esNuevo}: true si el usuario se creó en esta llamada.
 */
public record RegistrarReporteRapidoResponseDTO(
        ReporteMascotaPublicoResponseDTO reporte,
        String token,
        boolean passwordTemporal,
        boolean esNuevo
) {
}
