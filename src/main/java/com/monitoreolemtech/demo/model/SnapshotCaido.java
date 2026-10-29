package com.monitoreolemtech.demo.model;

import java.time.LocalDateTime;
import org.hibernate.annotations.Immutable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Immutable
@Table(name = "snapshot_caidos")
public class SnapshotCaido {

    @Id
    @Column(name = "id_nodo")
    private Long idNodo;

    @Column(name = "id_campo_laudus")
    private String idCampoLaudus;

    @Column(name = "campo")
    private String campo;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "tipo_nodo_label")
    private String tipoNodoLabel;

    @Column(name = "conectividad")
    private String conectividad;

    @Column(name = "bateria")
    private String bateria;

    @Column(name = "lqi")
    private String lqi;

    @Column(name = "ultima_transmision")
    private String ultimaTransmision;

    @Column(name = "tiempo_caida")
    private String tiempoCaida;

    @Column(name = "diff_min")
    private Integer diffMin;

    @Column(name = "operativa")
    private Integer operativa;

    @Column(name = "fecha_efectiva")
    private LocalDateTime fechaEfectiva;

    protected SnapshotCaido() { }   // JPA lo necesita

    public Long getIdNodo() { return idNodo; }
    public String getIdCampoLaudus() { return idCampoLaudus; }
    public String getCampo() { return campo; }
    public String getNombre() { return nombre; }
    public String getTipoNodoLabel() { return tipoNodoLabel; }
    public String getConectividad() { return conectividad; }
    public String getBateria() { return bateria; }
    public String getLqi() { return lqi; }
    public String getUltimaTransmision() { return ultimaTransmision; }
    public String getTiempoCaida() { return tiempoCaida; }
    public Integer getDiffMin() { return diffMin; }
    public Integer getOperativa() { return operativa; }
    public LocalDateTime getFechaEfectiva() { return fechaEfectiva; }
}