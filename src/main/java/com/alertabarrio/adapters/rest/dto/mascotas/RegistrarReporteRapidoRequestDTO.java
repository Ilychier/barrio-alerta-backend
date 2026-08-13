package com.alertabarrio.adapters.rest.dto.mascotas;

/**
 * Datos del registro rápido de emergencia (part "datos" del multipart).
 * <p>
 * El usuario aporta su celular personal (llave de identidad) + los datos del
 * animalito. El teléfono de contacto puede ser igual o distinto al personal
 * (checkbox "usar mi mismo número" en el frontend).
 */
public record RegistrarReporteRapidoRequestDTO(
        String phonePersonal,      // llave del usuario (login + idempotencia)
        String telefonoContacto,    // contacto del reporte (puede diferir)
        String tipoReporte,         // LOST | FOUND
        Long tipoMascotaId,
        String otroTipoMascota,     // opcional
        Long ciudadId,
        String ubicacion,
        String descripcion         // opcional
) {
}
