package com.alertabarrio.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "barrios", uniqueConstraints = @UniqueConstraint(name = "uq_barrio_localidad", columnNames = {"nombre", "localidad_id"}))
public class BarrioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuadrante_id", nullable = false)
    private CuadranteEntity cuadrante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localidad_id", nullable = false)
    private LocalidadEntity localidad;

    public BarrioEntity() {}

    public BarrioEntity(String nombre, CuadranteEntity cuadrante, LocalidadEntity localidad) {
        this.nombre = nombre;
        this.cuadrante = cuadrante;
        this.localidad = localidad;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public CuadranteEntity getCuadrante() { return cuadrante; }
    public void setCuadrante(CuadranteEntity cuadrante) { this.cuadrante = cuadrante; }
    public LocalidadEntity getLocalidad() { return localidad; }
    public void setLocalidad(LocalidadEntity localidad) { this.localidad = localidad; }
}
