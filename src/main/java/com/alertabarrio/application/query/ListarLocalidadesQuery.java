package com.alertabarrio.application.query;

import com.alertabarrio.domain.model.valueobject.Paginacion;

public record ListarLocalidadesQuery(Long municipioId, Paginacion paginacion) {
}
