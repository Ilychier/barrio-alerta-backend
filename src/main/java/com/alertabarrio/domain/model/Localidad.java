package com.alertabarrio.domain.model;

import com.alertabarrio.domain.exception.BarrioInvalidoException;
import com.alertabarrio.domain.model.valueobject.LocalidadId;

/**
 * Localidad/comuna/corregimiento dentro de un municipio (ej: Castilla en Medellín).
 * Nivel intermedio entre municipio y barrio en la jerarquía geográfica.
 */
public final class Localidad {

    private final LocalidadId id;
    private final String nombre;
    private final Long municipioId;

    private Localidad(LocalidadId id, String nombre, Long municipioId) {
        this.id = id;
        this.nombre = nombre;
        this.municipioId = municipioId;
    }

    public static Localidad crear(String nombre, Long municipioId) {
        validarNombre(nombre);
        if (municipioId == null) throw new BarrioInvalidoException("El ID del municipio no puede ser nulo");
        return new Localidad(null, nombre.trim(), municipioId);
    }

    public static Localidad reconstruir(Long id, String nombre, Long municipioId) {
        validarNombre(nombre);
        if (municipioId == null) throw new BarrioInvalidoException("El ID del municipio no puede ser nulo");
        return new Localidad(new LocalidadId(id), nombre.trim(), municipioId);
    }

    private static void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new BarrioInvalidoException("El nombre de la localidad no puede estar vacío");
        }
        if (nombre.trim().length() > 150) {
            throw new BarrioInvalidoException("El nombre de la localidad no puede exceder 150 caracteres");
        }
    }

    public LocalidadId getId() { return id; }
    public String getNombre() { return nombre; }
    public Long getMunicipioId() { return municipioId; }
}
