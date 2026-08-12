package com.alertabarrio.domain.model.mascotas;

import com.alertabarrio.domain.exception.mascotas.CiudadInvalidaException;
import com.alertabarrio.domain.model.mascotas.valueobject.CiudadId;

/**
 * Catálogo geográfico del BC Mascotas.
 * <p>
 * Representa un municipio de Colombia (ej: Cali, Valle del Cauca).
 * Es un catálogo independiente del BC Alertas: no se relaciona con
 * {@code Barrio} (que está acoplado a Cuadrantes/CAIs de seguridad).
 * <p>
 * Entidad pura del dominio: final, inmutable, factory methods.
 */
public final class Ciudad {

    private final CiudadId id;
    private final String nombre;
    private final String departamento;
    private final String pais;

    private Ciudad(CiudadId id, String nombre, String departamento, String pais) {
        this.id = id;
        this.nombre = nombre;
        this.departamento = departamento;
        this.pais = pais;
    }

    public static Ciudad crear(String nombre, String departamento, String pais) {
        validarNombre(nombre);
        validarDepartamento(departamento);
        validarPais(pais);
        return new Ciudad(null, nombre.trim(), departamento.trim(), pais.trim());
    }

    public static Ciudad reconstruir(Long id, String nombre, String departamento, String pais) {
        validarNombre(nombre);
        validarDepartamento(departamento);
        validarPais(pais);
        return new Ciudad(new CiudadId(id), nombre.trim(), departamento.trim(), pais.trim());
    }

    private static void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new CiudadInvalidaException("El nombre de la ciudad no puede estar vacío");
        }
        if (nombre.trim().length() > 100) {
            throw new CiudadInvalidaException("El nombre de la ciudad no puede exceder 100 caracteres");
        }
    }

    private static void validarDepartamento(String departamento) {
        if (departamento == null || departamento.isBlank()) {
            throw new CiudadInvalidaException("El departamento no puede estar vacío");
        }
        if (departamento.trim().length() > 100) {
            throw new CiudadInvalidaException("El departamento no puede exceder 100 caracteres");
        }
    }

    private static void validarPais(String pais) {
        if (pais == null || pais.isBlank()) {
            throw new CiudadInvalidaException("El país no puede estar vacío");
        }
        if (pais.trim().length() > 100) {
            throw new CiudadInvalidaException("El país no puede exceder 100 caracteres");
        }
    }

    public CiudadId getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDepartamento() { return departamento; }
    public String getPais() { return pais; }
}
