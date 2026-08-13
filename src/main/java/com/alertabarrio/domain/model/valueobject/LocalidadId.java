package com.alertabarrio.domain.model.valueobject;

public record LocalidadId(Long value) {
    public LocalidadId {
        if (value == null) throw new IllegalArgumentException("LocalidadId must not be null");
    }
}
