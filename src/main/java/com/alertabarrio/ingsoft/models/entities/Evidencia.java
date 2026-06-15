package com.alertabarrio.ingsoft.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "evidencias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evidencia {

    // PK - Tipo: int (Long en Java)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    // FK - Tipo: int (Conexión directa a alerta_id)
    @ManyToOne
    @JoinColumn(name = "alerta_id", nullable = false)
    private Alerta alerta;

    // Tipo: string (url_archivo)
    @Column(name = "url_archivo", nullable = false, columnDefinition = "TEXT")
    private String urlArchivo; 

    // Tipo: string (tipo_archivo)
    @Column(name = "tipo_archivo", nullable = false, length = 50)
    private String tipoArchivo; // Aquí el sistema guardará si es "FOTO" o "VIDEO"
}