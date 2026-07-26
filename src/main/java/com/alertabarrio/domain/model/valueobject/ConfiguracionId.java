package com.alertabarrio.domain.model.valueobject;

public record ConfiguracionId(Long value) {
    public ConfiguracionId {
        if (value == null) throw new IllegalArgumentException("ConfiguracionId must not be null");
    }
}
