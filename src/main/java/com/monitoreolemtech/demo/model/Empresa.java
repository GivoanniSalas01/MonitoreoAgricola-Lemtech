package com.monitoreolemtech.demo.model;

import org.hibernate.annotations.Immutable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Immutable
@Table(name = "empresa")
public class Empresa {

    @Id
    @Column(name = "id_empresa")
    private Long idEmpresa;

    @Column(name = "moroso")
    private String moroso;
    @Column(name = "nombre")
    private String nombre;

    
    protected Empresa() { }   // JPA lo necesita

    public Long getIdEmpresa() { return idEmpresa; }
    public String getMoroso() { return moroso; }
    public String getNombre() { return nombre; }
}