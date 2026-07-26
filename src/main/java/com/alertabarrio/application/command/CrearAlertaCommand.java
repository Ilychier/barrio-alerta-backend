package com.alertabarrio.application.command;

public record CrearAlertaCommand(String descripcion, boolean esSos, Long usuarioId, Long categoriaId) {
}
