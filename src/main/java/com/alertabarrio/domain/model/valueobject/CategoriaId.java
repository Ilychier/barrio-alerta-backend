package com.alertabarrio.domain.model.valueobject;

public record CategoriaId(Long value) {

    public CategoriaId {
        if (value == null) {
            throw new IllegalArgumentException("CategoriaId must not be null");
        }
    }
}
