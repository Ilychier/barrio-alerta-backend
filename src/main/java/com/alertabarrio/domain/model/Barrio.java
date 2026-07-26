package com.alertabarrio.domain.model;

import com.alertabarrio.domain.exception.BarrioInvalidoException;
import com.alertabarrio.domain.model.valueobject.BarrioId;
import com.alertabarrio.domain.model.valueobject.CuadranteId;

public final class Barrio {

    private final BarrioId id;
    private final String nombre;
    private final CuadranteId cuadranteId;

    private Barrio(BarrioId id, String nombre, CuadranteId cuadranteId) {
        this.id = id;
        this.nombre = nombre;
        this.cuadranteId = cuadranteId;
    }

    public static Barrio crear(String nombre, Long cuadranteId) {
        validarNombre(nombre);
        if (cuadranteId == null) throw new BarrioInvalidoException("El ID del cuadrante no puede ser nulo");
        return new Barrio(null, nombre.trim(), new CuadranteId(cuadranteId));
    }

    public static Barrio reconstruir(Long id, String nombre, Long cuadranteId) {
        validarNombre(nombre);
        if (cuadranteId == null) throw new BarrioInvalidoException("El ID del cuadrante no puede ser nulo");
        return new Barrio(new BarrioId(id), nombre.trim(), new CuadranteId(cuadranteId));
    }

    private static void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new BarrioInvalidoException("El nombre del barrio no puede estar vacío");
        }
        if (nombre.trim().length() > 100) {
            throw new BarrioInvalidoException("El nombre del barrio no puede exceder 100 caracteres");
        }
    }

    public BarrioId getId() { return id; }
    public String getNombre() { return nombre; }
    public CuadranteId getCuadranteId() { return cuadranteId; }
}
