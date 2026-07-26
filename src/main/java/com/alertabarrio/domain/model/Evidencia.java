package com.alertabarrio.domain.model;

import com.alertabarrio.domain.exception.EvidenciaInvalidaException;
import com.alertabarrio.domain.model.valueobject.*;

import java.time.Clock;
import java.time.LocalDateTime;

public final class Evidencia {

    private final EvidenciaId id;
    private final String archivoUrl;
    private final LocalDateTime fechaSubida;
    private final AlertaId alertaId;

    private Evidencia(EvidenciaId id, String archivoUrl, LocalDateTime fechaSubida, AlertaId alertaId) {
        this.id = id;
        this.archivoUrl = archivoUrl;
        this.fechaSubida = fechaSubida;
        this.alertaId = alertaId;
    }

    public static Evidencia crear(String archivoUrl, Long alertaId, Clock reloj) {
        validarArchivoUrl(archivoUrl);
        if (alertaId == null) throw new EvidenciaInvalidaException("El ID de la alerta no puede ser nulo");
        return new Evidencia(null, archivoUrl.trim(), LocalDateTime.now(reloj), new AlertaId(alertaId));
    }

    public static Evidencia reconstruir(Long id, String archivoUrl, LocalDateTime fechaSubida, Long alertaId) {
        validarArchivoUrl(archivoUrl);
        if (alertaId == null) throw new EvidenciaInvalidaException("El ID de la alerta no puede ser nulo");
        return new Evidencia(new EvidenciaId(id), archivoUrl.trim(), fechaSubida, new AlertaId(alertaId));
    }

    private static void validarArchivoUrl(String archivoUrl) {
        if (archivoUrl == null || archivoUrl.isBlank()) {
            throw new EvidenciaInvalidaException("La URL del archivo no puede estar vacía");
        }
    }

    public EvidenciaId getId() { return id; }
    public String getArchivoUrl() { return archivoUrl; }
    public LocalDateTime getFechaSubida() { return fechaSubida; }
    public AlertaId getAlertaId() { return alertaId; }
}
