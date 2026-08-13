package com.alertabarrio.domain.model.mascotas;

/**
 * Tipo de reporte de mascota.
 * <p>
 * {@code LOST}: se perdió mi mascota.
 * {@code FOUND}: encontré o vi una mascota.
 * <p>
 * Enum puro del dominio. La persistencia (VARCHAR + CHECK) es
 * responsabilidad del adapter JPA, no del dominio.
 */
public enum TipoReporte {
    LOST,
    FOUND;

    public static TipoReporte fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("TipoReporte no puede ser nulo");
        }
        try {
            return TipoReporte.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "TipoReporte inválido: " + value + ". Valores permitidos: LOST, FOUND");
        }
    }
}
