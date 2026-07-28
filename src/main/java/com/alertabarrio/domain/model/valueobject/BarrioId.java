package com.alertabarrio.domain.model.valueobject;

public record BarrioId(Long value) {
    public BarrioId {
        if (value == null) throw new IllegalArgumentException("BarrioId must not be null");
    }
}
