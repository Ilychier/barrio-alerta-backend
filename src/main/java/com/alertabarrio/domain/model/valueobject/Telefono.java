package com.alertabarrio.domain.model.valueobject;

public record Telefono(String value) {
    public Telefono {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Telefono must not be null or blank");
        }
        // Máx 14 dígitos: con el prefijo "+" el string completo cabe en
        // varchar(15) de la BD (ej: +573001234567 = 13 chars).
        if (!value.matches("^\\+?[0-9]{7,14}$")) {
            throw new IllegalArgumentException("Telefono has invalid format: " + value);
        }
    }
}
