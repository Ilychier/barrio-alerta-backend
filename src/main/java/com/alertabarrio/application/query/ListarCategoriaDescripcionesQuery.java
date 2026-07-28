package com.alertabarrio.application.query;

import com.alertabarrio.domain.model.valueobject.Paginacion;

public record ListarCategoriaDescripcionesQuery(Long categoriaId, Paginacion paginacion) {
}
