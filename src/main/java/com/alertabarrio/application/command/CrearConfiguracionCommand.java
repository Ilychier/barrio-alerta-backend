package com.alertabarrio.application.command;

public record CrearConfiguracionCommand(Long usuarioId, boolean recibirNotificaciones, boolean modoSilencioso) {
}
