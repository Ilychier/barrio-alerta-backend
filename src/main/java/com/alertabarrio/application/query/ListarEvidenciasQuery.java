package com.alertabarrio.application.query;

import com.alertabarrio.domain.model.valueobject.Paginacion;

public record ListarEvidenciasQuery(Long alertaId, Paginacion paginacion) {
}
