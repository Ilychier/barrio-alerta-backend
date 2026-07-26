package com.alertabarrio.application.command;

public record ParchearAlertaCommand(Long id, String descripcion, Boolean esSos, Long usuarioId, Long categoriaId) {
}
