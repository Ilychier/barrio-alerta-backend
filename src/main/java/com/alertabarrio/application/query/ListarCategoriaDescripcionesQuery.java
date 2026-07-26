package com.alertabarrio.application.query;

import org.springframework.data.domain.Pageable;

public record ListarCategoriaDescripcionesQuery(Long categoriaId, Pageable pageable) {
}
