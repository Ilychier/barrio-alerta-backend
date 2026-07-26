package com.alertabarrio.domain.model.valueobject;

public record CuadranteId(Long value) {

    public CuadranteId {
        if (value == null) {
            throw new IllegalArgumentException("CuadranteId must not be null");
        }
    }
}
