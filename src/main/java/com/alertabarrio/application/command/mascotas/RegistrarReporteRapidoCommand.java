package com.alertabarrio.application.command.mascotas;

/**
 * Command del registro rápido de emergencia (BC Mascotas).
 * <p>
 * Un solo formulario UX: el usuario aporta su celular personal (llave de
 * identidad) + los datos del animalito. El teléfono de contacto de la mascota
 * puede ser igual o distinto al personal (checkbox "usar mi mismo número").
 */
public record RegistrarReporteRapidoCommand(
        String phonePersonal,        // llave del usuario (login + idempotencia)
        String telefonoContacto,      // contacto del reporte (puede diferir)
        String tipoReporte,           // LOST | FOUND
        Long tipoMascotaId,
        String otroTipoMascota,       // opcional
        Long ciudadId,
        String ubicacion,
        String descripcion,           // opcional
        String fotoUrl                // opcional (resuelta por el controller)
) {
}
