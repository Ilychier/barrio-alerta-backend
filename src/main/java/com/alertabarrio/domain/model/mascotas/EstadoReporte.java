package com.alertabarrio.domain.model.mascotas;

/**
 * Estado actual de un reporte de mascota.
 * <p>
 * {@code ACTIVE}: reporte vigente, visible en el feed.
 * {@code RESCUED}: mascota recuperada (historia de rescate).
 * {@code DELETED}: reporte eliminado (soft delete, no visible en feed).
 * <p>
 * Enum puro del dominio. La persistencia (VARCHAR + CHECK) es
 * responsabilidad del adapter JPA, no del dominio.
 */
public enum EstadoReporte {
    ACTIVE,
    RESCUED,
    DELETED;

    public static EstadoReporte fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("EstadoReporte no puede ser nulo");
        }
        try {
            return EstadoReporte.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "EstadoReporte inválido: " + value + ". Valores permitidos: ACTIVE, RESCUED, DELETED");
        }
    }
}
