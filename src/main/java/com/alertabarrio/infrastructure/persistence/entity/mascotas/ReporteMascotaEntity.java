package com.alertabarrio.infrastructure.persistence.entity.mascotas;

import com.alertabarrio.infrastructure.persistence.entity.UserEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reportes_mascotas")
public class ReporteMascotaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UserEntity usuario;

    @Column(name = "tipo_reporte", nullable = false, length = 10)
    private String tipoReporte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_mascota_id", nullable = false)
    private TipoMascotaEntity tipoMascota;

    @Column(name = "otro_tipo_mascota", length = 50)
    private String otroTipoMascota;

    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ciudad_id", nullable = false)
    private CiudadEntity ciudad;

    @Column(name = "ubicacion", nullable = false, length = 255)
    private String ubicacion;

    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "estado", nullable = false, length = 10)
    private String estado;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public ReporteMascotaEntity() {}

    public ReporteMascotaEntity(UserEntity usuario, String tipoReporte, TipoMascotaEntity tipoMascota,
                                CiudadEntity ciudad, String ubicacion, String telefono, String descripcion,
                                String estado, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this(usuario, tipoReporte, tipoMascota, null, null, ciudad, ubicacion, telefono, descripcion,
                estado, createdAt, updatedAt);
    }

    public ReporteMascotaEntity(UserEntity usuario, String tipoReporte, TipoMascotaEntity tipoMascota,
                                String otroTipoMascota, String fotoUrl, CiudadEntity ciudad, String ubicacion, String telefono,
                                String descripcion, String estado, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.usuario = usuario;
        this.tipoReporte = tipoReporte;
        this.tipoMascota = tipoMascota;
        this.otroTipoMascota = otroTipoMascota;
        this.fotoUrl = fotoUrl;
        this.ciudad = ciudad;
        this.ubicacion = ubicacion;
        this.telefono = telefono;
        this.descripcion = descripcion;
        this.estado = estado;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public UserEntity getUsuario() { return usuario; }
    public void setUsuario(UserEntity usuario) { this.usuario = usuario; }
    public String getTipoReporte() { return tipoReporte; }
    public void setTipoReporte(String tipoReporte) { this.tipoReporte = tipoReporte; }
    public TipoMascotaEntity getTipoMascota() { return tipoMascota; }
    public void setTipoMascota(TipoMascotaEntity tipoMascota) { this.tipoMascota = tipoMascota; }
    public String getOtroTipoMascota() { return otroTipoMascota; }
    public void setOtroTipoMascota(String otroTipoMascota) { this.otroTipoMascota = otroTipoMascota; }
    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
    public CiudadEntity getCiudad() { return ciudad; }
    public void setCiudad(CiudadEntity ciudad) { this.ciudad = ciudad; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
