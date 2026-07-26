package com.alertabarrio.application.command;

public record ActualizarAlertaCommand(Long id, String descripcion, boolean esSos, Long usuarioId, Long categoriaId) {
}
