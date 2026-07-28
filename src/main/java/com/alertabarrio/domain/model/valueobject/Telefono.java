package com.alertabarrio.domain.model.valueobject;

public record Telefono(String value) {
    public Telefono {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Telefono must not be null or blank");
        }
        if (!value.matches("^\\+?[0-9]{7,15}$")) {
            throw new IllegalArgumentException("Telefono has invalid format: " + value);
        }
    }
}
