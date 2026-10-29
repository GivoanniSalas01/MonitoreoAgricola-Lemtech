package com.monitoreolemtech.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;
import com.monitoreolemtech.demo.model.SectorCampo;

public interface SectorCampoRepository extends Repository<SectorCampo, Integer> {

    List<SectorCampo> findAll();
    Optional<SectorCampo> findById(Integer idSector);
}