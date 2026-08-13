package com.alertabarrio.domain.port.in.mascotas;

import com.alertabarrio.application.command.mascotas.RegistrarReporteRapidoCommand;
import com.alertabarrio.application.dto.mascotas.ReporteRapidoResultDTO;

/**
 * Puerto de entrada del registro rápido de emergencia (BC Mascotas).
 * <p>
 * Facade transaccional: registra (o reutiliza) el usuario por su celular y
 * crea el reporte del animalito en una sola transacción. Si el usuario es
 * nuevo o tiene clave temporal, devuelve un JWT para auto-login.
 */
public interface RegistrarReporteRapidoUseCase {
    ReporteRapidoResultDTO execute(RegistrarReporteRapidoCommand command);
}
