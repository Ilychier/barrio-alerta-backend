package com.alertabarrio.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evidencias")
public class EvidenciaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "archivo_url", nullable = false)
    private String archivoUrl;

    @Column(name = "fecha_subida", nullable = false)
    private LocalDateTime fechaSubida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alerta_id", nullable = false)
    private AlertaEntity alerta;

    public EvidenciaEntity() {}

    public EvidenciaEntity(String archivoUrl, LocalDateTime fechaSubida, AlertaEntity alerta) {
        this.archivoUrl = archivoUrl;
        this.fechaSubida = fechaSubida;
        this.alerta = alerta;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getArchivoUrl() { return archivoUrl; }
    public void setArchivoUrl(String archivoUrl) { this.archivoUrl = archivoUrl; }
    public LocalDateTime getFechaSubida() { return fechaSubida; }
    public void setFechaSubida(LocalDateTime fechaSubida) { this.fechaSubida = fechaSubida; }
    public AlertaEntity getAlerta() { return alerta; }
    public void setAlerta(AlertaEntity alerta) { this.alerta = alerta; }
}
