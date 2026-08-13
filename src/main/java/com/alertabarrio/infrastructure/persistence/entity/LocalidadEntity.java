package com.alertabarrio.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "localidades")
public class LocalidadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipio_id", nullable = false)
    private CiudadInfoEntity municipio;

    public LocalidadEntity() {}

    public LocalidadEntity(String nombre, CiudadInfoEntity municipio) {
        this.nombre = nombre;
        this.municipio = municipio;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public CiudadInfoEntity getMunicipio() { return municipio; }
    public void setMunicipio(CiudadInfoEntity municipio) { this.municipio = municipio; }
}
