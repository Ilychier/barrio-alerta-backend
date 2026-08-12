package com.alertabarrio.domain.model.mascotas.valueobject;

public record TipoMascotaId(Long value) {
    public TipoMascotaId {
        if (value == null) throw new IllegalArgumentException("TipoMascotaId must not be null");
    }
}
