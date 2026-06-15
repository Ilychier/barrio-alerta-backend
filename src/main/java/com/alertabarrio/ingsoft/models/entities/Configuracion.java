package com.alertabarrio.ingsoft.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "configuraciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Configuracion {

    // PK - Tipo: int (Long en Java)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    // FK - Tipo: int (Conexión directa a usuario_id)
    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    // Tipo: boolean (recibir_notificaciones)
    @Column(name = "recibir_notificaciones", nullable = false)
    private Boolean recibirNotificaciones; 

    // Tipo: boolean (modo_silencioso)
    @Column(name = "modo_silencioso", nullable = false)
    private Boolean modoSilencioso;

    // Valores por defecto para cuando el usuario sea nuevo
    @PrePersist
    protected void onCreate() {
        if (this.recibirNotificaciones == null) this.recibirNotificaciones = true;
        if (this.modoSilencioso == null) this.modoSilencioso = false;
    }
}