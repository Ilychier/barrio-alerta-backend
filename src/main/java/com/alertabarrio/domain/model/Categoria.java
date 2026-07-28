package com.alertabarrio.domain.model;

import com.alertabarrio.domain.exception.CategoriaInvalidaException;
import com.alertabarrio.domain.model.valueobject.CategoriaId;

/**
 * Pure domain entity for Categoria.
 * No JPA annotations, no framework dependencies.
 */
public final class Categoria {

    private final CategoriaId id;
    private final String nombre;
    private final String iconoReferencia;

    private Categoria(CategoriaId id, String nombre, String iconoReferencia) {
        this.id = id;
        this.nombre = nombre;
        this.iconoReferencia = iconoReferencia;
    }

    /**
     * Factory method to create a new Categoria (before persistence).
     */
    public static Categoria crear(String nombre, String iconoReferencia) {
        validarNombre(nombre);
        validarIcono(iconoReferencia);
        return new Categoria(null, nombre.trim(), iconoReferencia.trim());
    }

    /**
     * Factory method to reconstruct an existing Categoria (from persistence).
     */
    public static Categoria reconstruir(Long id, String nombre, String iconoReferencia) {
        validarNombre(nombre);
        validarIcono(iconoReferencia);
        return new Categoria(new CategoriaId(id), nombre.trim(), iconoReferencia.trim());
    }

    private static void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new CategoriaInvalidaException("El nombre de la categoría no puede estar vacío");
        }
        if (nombre.trim().length() > 100) {
            throw new CategoriaInvalidaException("El nombre de la categoría no puede exceder 100 caracteres");
        }
    }

    private static void validarIcono(String icono) {
        if (icono == null || icono.isBlank()) {
            throw new CategoriaInvalidaException("El icono de referencia no puede estar vacío");
        }
        if (icono.trim().length() > 255) {
            throw new CategoriaInvalidaException("El icono de referencia no puede exceder 255 caracteres");
        }
    }

    public CategoriaId getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIconoReferencia() {
        return iconoReferencia;
    }
}
