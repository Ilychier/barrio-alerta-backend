package com.alertabarrio.domain.model.valueobject;

public record EvidenciaId(Long value) {
    public EvidenciaId {
        if (value == null) throw new IllegalArgumentException("EvidenciaId must not be null");
    }
}
