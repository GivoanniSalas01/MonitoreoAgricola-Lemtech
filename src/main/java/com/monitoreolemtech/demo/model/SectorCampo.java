package com.monitoreolemtech.demo.model;

import org.hibernate.annotations.Immutable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Immutable
@Table(name = "sector_campo")
public class SectorCampo {

    @Id
    @Column(name = "id_sector")
    private Integer idSector;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "region")
    private String region;

    @Column(name = "comuna")
    private String comuna;

    @Column(name = "id_campo_laudus")
    private String idCampoLaudus;
@Column(name = "id_empresa")
private Long idEmpresa;


    protected SectorCampo() { }   // JPA lo necesita

    public Integer getIdSector() { return idSector; }
    public String getNombre() { return nombre; }
    public String getRegion() { return region; }
    public String getComuna() { return comuna; }
    public String getIdCampoLaudus() { return idCampoLaudus; }
    public Long getIdEmpresa() { return idEmpresa; }
}