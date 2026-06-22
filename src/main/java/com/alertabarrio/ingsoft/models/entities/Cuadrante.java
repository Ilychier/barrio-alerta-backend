package com.alertabarrio.ingsoft.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "cuadrantes")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Cuadrante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_unidad", nullable = false, unique = true, length = 100)
    private String nombreUnidad;

    @Column(name = "telefono_emergencia", nullable = false, length = 15)
    private String telefonoEmergencia;

}