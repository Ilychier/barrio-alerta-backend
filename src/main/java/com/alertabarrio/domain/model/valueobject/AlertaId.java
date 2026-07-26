package com.alertabarrio.domain.model.valueobject;

public record AlertaId(Long value) {
    public AlertaId {
        if (value == null) throw new IllegalArgumentException("AlertaId must not be null");
    }
}
