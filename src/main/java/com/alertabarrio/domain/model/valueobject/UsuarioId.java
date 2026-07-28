package com.alertabarrio.domain.model.valueobject;

public record UsuarioId(Long value) {
    public UsuarioId {
        if (value == null) throw new IllegalArgumentException("UsuarioId must not be null");
    }
}
