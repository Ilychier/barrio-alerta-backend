package com.alertabarrio.domain.model.mascotas.valueobject;

public record ReporteMascotaId(Long value) {
    public ReporteMascotaId {
        if (value == null) throw new IllegalArgumentException("ReporteMascotaId must not be null");
    }
}
