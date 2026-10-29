package com.monitoreolemtech.demo.service;

import java.util.List;

import com.monitoreolemtech.demo.dto.CampoResumenDto;
import com.monitoreolemtech.demo.dto.NodoDetalleDto;

public interface CampoService {

    List<CampoResumenDto> obtenerResumenCampos();

    List<NodoDetalleDto> obtenerNodosDeEmpresa(Long idEmpresa);
}
