package com.alertabarrio.domain.model.mascotas;

import com.alertabarrio.domain.exception.mascotas.ReporteMascotaInvalidoException;
import com.alertabarrio.domain.model.mascotas.valueobject.CiudadId;
import com.alertabarrio.domain.model.mascotas.valueobject.ReporteMascotaId;
import com.alertabarrio.domain.model.mascotas.valueobject.TipoMascotaId;
import com.alertabarrio.domain.model.valueobject.Telefono;
import com.alertabarrio.domain.model.valueobject.UsuarioId;

import java.time.Clock;
import java.time.LocalDateTime;

/**
 * Entidad principal del BC Mascotas.
 * <p>
 * Representa un reporte de mascota perdida (LOST) o encontrada/vista (FOUND).
 * <p>
 * Inmutable por diseño (consistente con {@code Alerta}, {@code User},
 * {@code Barrio} del BC Alertas): los cambios de estado producen una
 * nueva instancia via {@link #cambiarEstado}.
 * <p>
 * El BC Mascotas comparte identidad con {@code users} únicamente mediante
 * {@link UsuarioId} (FK de referencia). No importa el modelo {@code User}.
 * El {@link Telefono} se reutiliza del dominio existente (D9).
 */
public final class ReporteMascota {

    private final ReporteMascotaId id;
    private final TipoReporte tipoReporte;
    private final TipoMascotaId tipoMascotaId;
    private final CiudadId ciudadId;
    private final String ubicacion;
    private final Telefono telefono;
    private final String descripcion;
    private final EstadoReporte estado;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final UsuarioId usuarioId;

    private ReporteMascota(ReporteMascotaId id, TipoReporte tipoReporte, TipoMascotaId tipoMascotaId,
                           CiudadId ciudadId, String ubicacion, Telefono telefono, String descripcion,
                           EstadoReporte estado, LocalDateTime createdAt, LocalDateTime updatedAt,
                           UsuarioId usuarioId) {
        this.id = id;
        this.tipoReporte = tipoReporte;
        this.tipoMascotaId = tipoMascotaId;
        this.ciudadId = ciudadId;
        this.ubicacion = ubicacion;
        this.telefono = telefono;
        this.descripcion = descripcion;
        this.estado = estado;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.usuarioId = usuarioId;
    }

    /**
     * Factory para un reporte nuevo (antes de persistir).
     * Todo reporte nace con estado {@code ACTIVE}.
     */
    public static ReporteMascota crear(String tipoReporte, Long tipoMascotaId, Long ciudadId,
                                       String ubicacion, String telefono, String descripcion,
                                       Long usuarioId, Clock reloj) {
        validarUbicacion(ubicacion);
        if (usuarioId == null) {
            throw new ReporteMascotaInvalidoException("El ID del usuario no puede ser nulo");
        }
        if (tipoMascotaId == null) {
            throw new ReporteMascotaInvalidoException("El ID del tipo de mascota no puede ser nulo");
        }
        if (ciudadId == null) {
            throw new ReporteMascotaInvalidoException("El ID de la ciudad no puede ser nulo");
        }
        LocalDateTime ahora = LocalDateTime.now(reloj);
        return new ReporteMascota(
                null,
                TipoReporte.fromString(tipoReporte),
                new TipoMascotaId(tipoMascotaId),
                new CiudadId(ciudadId),
                ubicacion.trim(),
                new Telefono(telefono),
                descripcion != null ? descripcion.trim() : null,
                EstadoReporte.ACTIVE,
                ahora,
                ahora,
                new UsuarioId(usuarioId)
        );
    }

    /**
     * Factory para reconstruir un reporte existente (desde persistencia).
     */
    public static ReporteMascota reconstruir(Long id, String tipoReporte, Long tipoMascotaId, Long ciudadId,
                                             String ubicacion, String telefono, String descripcion,
                                             String estado, LocalDateTime createdAt, LocalDateTime updatedAt,
                                             Long usuarioId) {
        if (id == null) {
            throw new ReporteMascotaInvalidoException("El ID del reporte no puede ser nulo al reconstruir");
        }
        validarUbicacion(ubicacion);
        if (usuarioId == null) {
            throw new ReporteMascotaInvalidoException("El ID del usuario no puede ser nulo");
        }
        return new ReporteMascota(
                new ReporteMascotaId(id),
                TipoReporte.fromString(tipoReporte),
                new TipoMascotaId(tipoMascotaId),
                new CiudadId(ciudadId),
                ubicacion.trim(),
                new Telefono(telefono),
                descripcion != null ? descripcion.trim() : null,
                EstadoReporte.fromString(estado),
                createdAt,
                updatedAt,
                new UsuarioId(usuarioId)
        );
    }

    /**
     * Actualiza los campos editables del reporte, retornando una nueva
     * instancia (inmutable). Los campos no editables (tipoReporte,
     * tipoMascotaId, ciudadId, usuarioId, createdAt) se conservan.
     */
    public ReporteMascota actualizar(String ubicacion, String telefono, String descripcion, Clock reloj) {
        validarUbicacion(ubicacion);
        return new ReporteMascota(
                this.id,
                this.tipoReporte,
                this.tipoMascotaId,
                this.ciudadId,
                ubicacion.trim(),
                new Telefono(telefono),
                descripcion != null ? descripcion.trim() : null,
                this.estado,
                this.createdAt,
                LocalDateTime.now(reloj),
                this.usuarioId
        );
    }

    /**
     * Cambia el estado del reporte, retornando una nueva instancia (inmutable).
     * <p>
     * Invariante: un reporte {@code DELETED} no puede cambiar de estado.
     */
    public ReporteMascota cambiarEstado(EstadoReporte nuevoEstado, Clock reloj) {
        if (nuevoEstado == null) {
            throw new ReporteMascotaInvalidoException("El nuevo estado no puede ser nulo");
        }
        if (this.estado == EstadoReporte.DELETED) {
            throw new ReporteMascotaInvalidoException(
                    "No se puede cambiar el estado de un reporte eliminado");
        }
        return new ReporteMascota(
                this.id,
                this.tipoReporte,
                this.tipoMascotaId,
                this.ciudadId,
                this.ubicacion,
                this.telefono,
                this.descripcion,
                nuevoEstado,
                this.createdAt,
                LocalDateTime.now(reloj),
                this.usuarioId
        );
    }

    /**
     * Soft delete: retorna una nueva instancia con estado {@code DELETED}.
     */
    public ReporteMascota eliminar(Clock reloj) {
        return cambiarEstado(EstadoReporte.DELETED, reloj);
    }

    private static void validarUbicacion(String ubicacion) {
        if (ubicacion == null || ubicacion.isBlank()) {
            throw new ReporteMascotaInvalidoException("La ubicación no puede estar vacía");
        }
        if (ubicacion.trim().length() > 255) {
            throw new ReporteMascotaInvalidoException("La ubicación no puede exceder 255 caracteres");
        }
    }

    public ReporteMascotaId getId() { return id; }
    public TipoReporte getTipoReporte() { return tipoReporte; }
    public TipoMascotaId getTipoMascotaId() { return tipoMascotaId; }
    public CiudadId getCiudadId() { return ciudadId; }
    public String getUbicacion() { return ubicacion; }
    public Telefono getTelefono() { return telefono; }
    public String getDescripcion() { return descripcion; }
    public EstadoReporte getEstado() { return estado; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public UsuarioId getUsuarioId() { return usuarioId; }
}
