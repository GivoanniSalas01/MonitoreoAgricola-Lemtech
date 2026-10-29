package com.monitoreolemtech.demo.model;

import java.time.LocalDateTime;
import org.hibernate.annotations.Immutable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Immutable
@Table(name = "nodo")
public class Nodo {

    @Id
    @Column(name = "id_nodo")
    private Long idNodo;

    @Column(name = "id_sector")
    private Integer idSector;

    @Column(name = "id_campo_laudus")
    private String idCampoLaudus;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "tipo_nodo")
    private Integer tipoNodo;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "conectividad")
    private String conectividad;

    @Column(name = "operativa")
    private Boolean operativa;

    @Column(name = "frecuencia")
    private Integer frecuencia;

    @Column(name = "fecha_ultimo_m")
    private LocalDateTime fechaUltimoM;

    @Column(name = "id_empresa")
    private Long idEmpresa;

    protected Nodo() { }   // JPA lo necesita

    public Long getIdNodo() { return idNodo; }
    public Integer getIdSector() { return idSector; }
    public String getIdCampoLaudus() { return idCampoLaudus; }
    public String getNombre() { return nombre; }
    public Integer getTipoNodo() { return tipoNodo; }
    public String getCategoria() { return categoria; }
    public String getConectividad() { return conectividad; }
    public Boolean getOperativa() { return operativa; }
    public Integer getFrecuencia() { return frecuencia; }
    public LocalDateTime getFechaUltimoM() { return fechaUltimoM; }
    public Long getIdEmpresa() { return idEmpresa; }
}