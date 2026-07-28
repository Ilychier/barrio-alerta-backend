package com.alertabarrio.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categorias")
public class CategoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "icono_referencia", nullable = false, length = 255)
    private String iconoReferencia;

    public CategoriaEntity() {}

    public CategoriaEntity(String nombre, String iconoReferencia) {
        this.nombre = nombre;
        this.iconoReferencia = iconoReferencia;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getIconoReferencia() { return iconoReferencia; }
    public void setIconoReferencia(String iconoReferencia) { this.iconoReferencia = iconoReferencia; }
}
