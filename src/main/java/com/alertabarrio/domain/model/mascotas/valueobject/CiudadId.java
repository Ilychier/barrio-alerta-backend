package com.alertabarrio.domain.model.mascotas.valueobject;

public record CiudadId(Long value) {
    public CiudadId {
        if (value == null) throw new IllegalArgumentException("CiudadId must not be null");
    }
}
