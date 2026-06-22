package com.alertabarrio.ingsoft.models.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "configuraciones")
public class Configuracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean recibirNotificaciones;
    private Boolean modoSilencioso;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Boolean getRecibirNotificaciones() { return recibirNotificaciones; }
    public void setRecibirNotificaciones(Boolean recibirNotificaciones) { this.recibirNotificaciones = recibirNotificaciones; }
    public Boolean getModoSilencioso() { return modoSilencioso; }
    public void setModoSilencioso(Boolean modoSilencioso) { this.modoSilencioso = modoSilencioso; }
}
