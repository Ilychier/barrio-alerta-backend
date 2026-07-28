package com.alertabarrio.application.command;

public record ActualizarCategoriaDescripcionCommand(Long id, String descripcion, Long categoriaId, String imagenUrl) {
}
