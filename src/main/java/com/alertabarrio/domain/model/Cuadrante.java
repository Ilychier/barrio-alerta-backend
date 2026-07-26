package com.alertabarrio.domain.model;

import com.alertabarrio.domain.exception.CuadranteInvalidaException;
import com.alertabarrio.domain.model.valueobject.CuadranteId;

/**
 * Pure domain entity for Cuadrante.
 * No JPA annotations, no framework dependencies.
 */
public final class Cuadrante {

    private final CuadranteId id;
    private final String nombreUnidad;
    private final String telefonoEmergencia;
    private final String emailEmergencia;

    private Cuadrante(CuadranteId id, String nombreUnidad, String telefonoEmergencia, String emailEmergencia) {
        this.id = id;
        this.nombreUnidad = nombreUnidad;
        this.telefonoEmergencia = telefonoEmergencia;
        this.emailEmergencia = emailEmergencia;
    }

    /**
     * Factory method to create a new Cuadrante (before persistence).
     */
    public static Cuadrante crear(String nombreUnidad, String telefonoEmergencia, String emailEmergencia) {
        validarNombreUnidad(nombreUnidad);
        validarTelefonoEmergencia(telefonoEmergencia);
        return new Cuadrante(null, nombreUnidad.trim(), telefonoEmergencia.trim(), emailEmergencia);
    }

    /**
     * Factory method to reconstruct an existing Cuadrante (from persistence).
     */
    public static Cuadrante reconstruir(Long id, String nombreUnidad, String telefonoEmergencia, String emailEmergencia) {
        validarNombreUnidad(nombreUnidad);
        validarTelefonoEmergencia(telefonoEmergencia);
        return new Cuadrante(new CuadranteId(id), nombreUnidad.trim(), telefonoEmergencia.trim(), emailEmergencia);
    }

    private static void validarNombreUnidad(String nombreUnidad) {
        if (nombreUnidad == null || nombreUnidad.isBlank()) {
            throw new CuadranteInvalidaException("El nombre de la unidad no puede estar vacío");
        }
        if (nombreUnidad.trim().length() > 100) {
            throw new CuadranteInvalidaException("El nombre de la unidad no puede exceder 100 caracteres");
        }
    }

    private static void validarTelefonoEmergencia(String telefono) {
        if (telefono == null || telefono.isBlank()) {
            throw new CuadranteInvalidaException("El teléfono de emergencia no puede estar vacío");
        }
        if (telefono.trim().length() > 100) {
            throw new CuadranteInvalidaException("El teléfono de emergencia no puede exceder 100 caracteres");
        }
    }

    public CuadranteId getId() {
        return id;
    }

    public String getNombreUnidad() {
        return nombreUnidad;
    }

    public String getTelefonoEmergencia() {
        return telefonoEmergencia;
    }

    public String getEmailEmergencia() {
        return emailEmergencia;
    }
}
