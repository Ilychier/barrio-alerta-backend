package com.alertabarrio.infrastructure.persistence.entity.mascotas;

import jakarta.persistence.*;

@Entity
@Table(name = "ciudades")
public class CiudadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "departamento", nullable = false, length = 100)
    private String departamento;

    @Column(name = "pais", nullable = false, length = 100)
    private String pais;

    @Column(name = "es_capital", nullable = false)
    private boolean esCapital = false;

    public CiudadEntity() {}

    public CiudadEntity(String nombre, String departamento, String pais) {
        this.nombre = nombre;
        this.departamento = departamento;
        this.pais = pais;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }
    public boolean isEsCapital() { return esCapital; }
    public void setEsCapital(boolean esCapital) { this.esCapital = esCapital; }
}
