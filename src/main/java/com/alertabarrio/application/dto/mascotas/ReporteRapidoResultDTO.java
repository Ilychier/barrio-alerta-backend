package com.alertabarrio.application.dto.mascotas;

/**
 * Resultado del registro rápido de emergencia.
 * <p>
 * - {@code esNuevo}: true si se creó el usuario en esta llamada.
 * - {@code passwordTemporal}: true si el usuario debe cambiar su clave.
 * - {@code token}: JWT para auto-login (solo si el usuario puede autenticarse
 *   sin clave: nuevo o temporal). null si el usuario existente tiene clave
 *   real (debe loguear manualmente).
 */
public record ReporteRapidoResultDTO(
        ReporteMascotaDTO reporte,
        String token,
        boolean passwordTemporal,
        boolean esNuevo
) {
}
