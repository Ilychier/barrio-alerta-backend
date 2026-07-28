package com.alertabarrio.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "configuraciones")
public class ConfiguracionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UserEntity usuario;

    @Column(name = "recibir_notificaciones", nullable = false)
    private Boolean recibirNotificaciones;

    @Column(name = "modo_silencioso", nullable = false)
    private Boolean modoSilencioso;

    public ConfiguracionEntity() {}

    public ConfiguracionEntity(UserEntity usuario, Boolean recibirNotificaciones, Boolean modoSilencioso) {
        this.usuario = usuario;
        this.recibirNotificaciones = recibirNotificaciones;
        this.modoSilencioso = modoSilencioso;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public UserEntity getUsuario() { return usuario; }
    public void setUsuario(UserEntity usuario) { this.usuario = usuario; }
    public Boolean getRecibirNotificaciones() { return recibirNotificaciones; }
    public void setRecibirNotificaciones(Boolean recibirNotificaciones) { this.recibirNotificaciones = recibirNotificaciones; }
    public Boolean getModoSilencioso() { return modoSilencioso; }
    public void setModoSilencioso(Boolean modoSilencioso) { this.modoSilencioso = modoSilencioso; }
}
