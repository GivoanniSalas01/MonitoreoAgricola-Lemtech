package com.monitoreolemtech.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monitoreolemtech.demo.dto.CampoResumenDto;
import com.monitoreolemtech.demo.dto.NodoDetalleDto;
import com.monitoreolemtech.demo.service.CampoService;

@RestController
@RequestMapping("/api/campos")
public class CampoController {

    private final CampoService campoService;

    public CampoController(CampoService campoService) {
        this.campoService = campoService;
    }

    @GetMapping
    public List<CampoResumenDto> listarResumen() {
        return campoService.obtenerResumenCampos();
    }

    @GetMapping("/{idEmpresa}/nodos")
    public List<NodoDetalleDto> listarNodos(@PathVariable Long idEmpresa) {
        return campoService.obtenerNodosDeEmpresa(idEmpresa);
    }
}
