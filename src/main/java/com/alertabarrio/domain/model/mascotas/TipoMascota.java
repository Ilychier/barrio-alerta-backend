package com.alertabarrio.domain.model.mascotas;

import com.alertabarrio.domain.exception.mascotas.TipoMascotaInvalidoException;
import com.alertabarrio.domain.model.mascotas.valueobject.TipoMascotaId;

/**
 * Catálogo de tipos de mascota (Perro, Gato, ...).
 * <p>
 * Tabla de catálogo simple (id, nombre, activo) para permitir agregar
 * nuevas especies sin modificar la estructura de {@link ReporteMascota}.
 * <p>
 * Entidad pura del dominio: final, inmutable, factory methods.
 */
public final class TipoMascota {

    private final TipoMascotaId id;
    private final String nombre;
    private final boolean activo;

    private TipoMascota(TipoMascotaId id, String nombre, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.activo = activo;
    }

    public static TipoMascota crear(String nombre) {
        validarNombre(nombre);
        return new TipoMascota(null, nombre.trim(), true);
    }

    public static TipoMascota reconstruir(Long id, String nombre, boolean activo) {
        validarNombre(nombre);
        return new TipoMascota(new TipoMascotaId(id), nombre.trim(), activo);
    }

    private static void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new TipoMascotaInvalidoException("El nombre del tipo de mascota no puede estar vacío");
        }
        if (nombre.trim().length() > 50) {
            throw new TipoMascotaInvalidoException("El nombre del tipo de mascota no puede exceder 50 caracteres");
        }
    }

    public TipoMascotaId getId() { return id; }
    public String getNombre() { return nombre; }
    public boolean isActivo() { return activo; }
}
