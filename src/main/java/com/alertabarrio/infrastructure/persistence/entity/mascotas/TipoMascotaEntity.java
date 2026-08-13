package com.alertabarrio.infrastructure.persistence.entity.mascotas;

import jakarta.persistence.*;

@Entity
@Table(name = "tipos_mascota")
public class TipoMascotaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private String nombre;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    public TipoMascotaEntity() {}

    public TipoMascotaEntity(String nombre, Boolean activo) {
        this.nombre = nombre;
        this.activo = activo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
