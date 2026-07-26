package com.alertabarrio.application.command;

public record ActualizarConfiguracionCommand(Long id, Long usuarioId, boolean recibirNotificaciones, boolean modoSilencioso) {
}
