package com.alertabarrio.domain.model;

import com.alertabarrio.domain.exception.AlertaInvalidaException;
import com.alertabarrio.domain.model.valueobject.*;

import java.time.Clock;
import java.time.LocalDateTime;

public final class Alerta {

    private final AlertaId id;
    private final String descripcion;
    private final boolean esSos;
    private final LocalDateTime fechaHora;
    private final UsuarioId usuarioId;
    private final CategoriaId categoriaId;

    private Alerta(AlertaId id, String descripcion, boolean esSos, LocalDateTime fechaHora, UsuarioId usuarioId, CategoriaId categoriaId) {
        this.id = id;
        this.descripcion = descripcion;
        this.esSos = esSos;
        this.fechaHora = fechaHora;
        this.usuarioId = usuarioId;
        this.categoriaId = categoriaId;
    }

    public static Alerta crear(String descripcion, boolean esSos, Long usuarioId, Long categoriaId, Clock reloj) {
        validarDescripcion(descripcion);
        if (usuarioId == null) throw new AlertaInvalidaException("El ID del usuario no puede ser nulo");
        return new Alerta(null, descripcion.trim(), esSos, LocalDateTime.now(reloj),
                new UsuarioId(usuarioId), categoriaId != null ? new CategoriaId(categoriaId) : null);
    }

    public static Alerta reconstruir(Long id, String descripcion, boolean esSos, LocalDateTime fechaHora, Long usuarioId, Long categoriaId) {
        validarDescripcion(descripcion);
        if (usuarioId == null) throw new AlertaInvalidaException("El ID del usuario no puede ser nulo");
        return new Alerta(new AlertaId(id), descripcion.trim(), esSos, fechaHora,
                new UsuarioId(usuarioId), categoriaId != null ? new CategoriaId(categoriaId) : null);
    }

    private static void validarDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            throw new AlertaInvalidaException("La descripción no puede estar vacía");
        }
    }

    public AlertaId getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public boolean isEsSos() { return esSos; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public UsuarioId getUsuarioId() { return usuarioId; }
    public CategoriaId getCategoriaId() { return categoriaId; }
}
