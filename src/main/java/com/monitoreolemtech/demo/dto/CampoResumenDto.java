package com.monitoreolemtech.demo.dto;

public class CampoResumenDto {

    private Long idEmpresa;
    private String nombre;          // nombre de la empresa
    private String codigos;         // ej. "147-a · 147-b"
    private String sectores;        // ej. "Cholqui, Las Gaviotas"
    private Integer cantidadSectores;
    private String region;
    private String comuna;
    private Long total;
    private Long caidos;
    private Double porcentajeCaidos;
    private String color;

    public CampoResumenDto(Long idEmpresa, String nombre, String codigos, String sectores,
                           Integer cantidadSectores, String region, String comuna, Long total,
                           Long caidos, Double porcentajeCaidos, String color) {
        this.idEmpresa = idEmpresa;
        this.nombre = nombre;
        this.codigos = codigos;
        this.sectores = sectores;
        this.cantidadSectores = cantidadSectores;
        this.region = region;
        this.comuna = comuna;
        this.total = total;
        this.caidos = caidos;
        this.porcentajeCaidos = porcentajeCaidos;
        this.color = color;
    }

    public Long getIdEmpresa() { return idEmpresa; }
    public String getNombre() { return nombre; }
    public String getCodigos() { return codigos; }
    public String getSectores() { return sectores; }
    public Integer getCantidadSectores() { return cantidadSectores; }
    public String getRegion() { return region; }
    public String getComuna() { return comuna; }
    public Long getTotal() { return total; }
    public Long getCaidos() { return caidos; }
    public Double getPorcentajeCaidos() { return porcentajeCaidos; }
    public String getColor() { return color; }
}
