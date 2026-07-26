package com.alertabarrio.application.command;

public record ParchearCategoriaDescripcionCommand(Long id, String descripcion, Long categoriaId, String imagenUrl) {
}
