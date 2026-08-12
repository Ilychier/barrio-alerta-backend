package com.alertabarrio.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "barrios")
public class BarrioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuadrante_id", nullable = false)
    private CuadranteEntity cuadrante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ciudad_id", nullable = false)
    private CiudadInfoEntity ciudad;

    public BarrioEntity() {}

    public BarrioEntity(String nombre, CuadranteEntity cuadrante, CiudadInfoEntity ciudad) {
        this.nombre = nombre;
        this.cuadrante = cuadrante;
        this.ciudad = ciudad;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public CuadranteEntity getCuadrante() { return cuadrante; }
    public void setCuadrante(CuadranteEntity cuadrante) { this.cuadrante = cuadrante; }
    public CiudadInfoEntity getCiudad() { return ciudad; }
    public void setCiudad(CiudadInfoEntity ciudad) { this.ciudad = ciudad; }
}
