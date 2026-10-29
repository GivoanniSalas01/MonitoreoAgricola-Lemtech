package com.monitoreolemtech.demo.dto;

import java.time.LocalDateTime;

public class NodoDetalleDto {

    private Long idNodo;
    private Integer idSector;
    private String codigoSector;
    private String nombreSector;
    private String nombre;
    private Integer tipoNodo;
    private String categoria;
    private String conectividad;
    private LocalDateTime ultimaTransmision;
    private Long minutosSinSenal;
    private String estado; // ONLINE / OFFLINE

    public NodoDetalleDto(Long idNodo, Integer idSector, String codigoSector, String nombreSector,
                          String nombre, Integer tipoNodo, String categoria, String conectividad,
                          LocalDateTime ultimaTransmision, Long minutosSinSenal, String estado) {
        this.idNodo = idNodo;
        this.idSector = idSector;
        this.codigoSector = codigoSector;
        this.nombreSector = nombreSector;
        this.nombre = nombre;
        this.tipoNodo = tipoNodo;
        this.categoria = categoria;
        this.conectividad = conectividad;
        this.ultimaTransmision = ultimaTransmision;
        this.minutosSinSenal = minutosSinSenal;
        this.estado = estado;
    }

    public Long getIdNodo() { return idNodo; }
    public Integer getIdSector() { return idSector; }
    public String getCodigoSector() { return codigoSector; }
    public String getNombreSector() { return nombreSector; }
    public String getNombre() { return nombre; }
    public Integer getTipoNodo() { return tipoNodo; }
    public String getCategoria() { return categoria; }
    public String getConectividad() { return conectividad; }
    public LocalDateTime getUltimaTransmision() { return ultimaTransmision; }
    public Long getMinutosSinSenal() { return minutosSinSenal; }
    public String getEstado() { return estado; }
}
