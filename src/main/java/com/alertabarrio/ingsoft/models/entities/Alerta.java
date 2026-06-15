package com.alertabarrio.ingsoft.models.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alertas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alerta {

    // PK (Primary Key) - Tipo: int (Long en Java)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    // Tipo: string
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion; 

    // Tipo: boolean (es_sos)
    @Column(name = "es_sos", nullable = false)
    private Boolean esSos; 

    // Tipo: datetime (fecha_hora)
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora; 

    // FK (Foreign Key) - Enlace a usuario_id (int)
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    // FK (Foreign Key) - Enlace a categoria_id (int)
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    // Automatización para la fecha_hora
    @PrePersist
    protected void onCreate() {
        this.fechaHora = LocalDateTime.now();
        if (this.esSos == null) {
            this.esSos = false;
        }
    }
}
