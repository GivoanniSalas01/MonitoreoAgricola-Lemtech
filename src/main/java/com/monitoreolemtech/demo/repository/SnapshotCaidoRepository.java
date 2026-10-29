package com.monitoreolemtech.demo.repository;

import java.util.List;
import org.springframework.data.repository.Repository;

import com.monitoreolemtech.demo.model.SnapshotCaido;

public interface SnapshotCaidoRepository extends Repository<SnapshotCaido, Long> {

    List<SnapshotCaido> findAll();

    List<SnapshotCaido> findByOperativa(Integer operativa);
}