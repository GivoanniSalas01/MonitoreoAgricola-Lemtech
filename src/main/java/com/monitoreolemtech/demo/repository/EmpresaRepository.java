package com.monitoreolemtech.demo.repository;

import java.util.List;
import org.springframework.data.repository.Repository;
import com.monitoreolemtech.demo.model.Empresa;

public interface EmpresaRepository extends Repository<Empresa, Long> {

    List<Empresa> findByMorosoIgnoreCase(String moroso);
    List<Empresa> findAll();
}