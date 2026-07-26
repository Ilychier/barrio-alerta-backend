package com.alertabarrio.domain.model.valueobject;

public record CategoriaDescripcionId(Long value) {
    public CategoriaDescripcionId {
        if (value == null) throw new IllegalArgumentException("CategoriaDescripcionId must not be null");
    }
}
