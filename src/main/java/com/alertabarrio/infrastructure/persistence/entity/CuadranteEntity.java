package com.alertabarrio.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cuadrantes")
public class CuadranteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_unidad", nullable = false, unique = true, length = 100)
    private String nombreUnidad;

    @Column(name = "telefono_emergencia", nullable = false, length = 100)
    private String telefonoEmergencia;

    public CuadranteEntity() {}

    public CuadranteEntity(String nombreUnidad, String telefonoEmergencia) {
        this.nombreUnidad = nombreUnidad;
        this.telefonoEmergencia = telefonoEmergencia;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombreUnidad() { return nombreUnidad; }
    public void setNombreUnidad(String nombreUnidad) { this.nombreUnidad = nombreUnidad; }
    public String getTelefonoEmergencia() { return telefonoEmergencia; }
    public void setTelefonoEmergencia(String telefonoEmergencia) { this.telefonoEmergencia = telefonoEmergencia; }
}
