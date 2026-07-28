package com.alertabarrio.application.command;

public record ActualizarEvidenciaCommand(Long id, String archivoUrl, Long alertaId) {
}
