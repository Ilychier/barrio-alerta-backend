package com.alertabarrio.application.query;

import com.alertabarrio.domain.model.valueobject.Paginacion;

public record ListarBarriosQuery(Paginacion paginacion, Long localidadId) {
    public ListarBarriosQuery(Paginacion paginacion) {
        this(paginacion, null);
    }
}
