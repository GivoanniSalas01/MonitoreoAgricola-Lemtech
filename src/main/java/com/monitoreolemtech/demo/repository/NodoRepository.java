package com.monitoreolemtech.demo.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.repository.Repository;
import com.monitoreolemtech.demo.model.Nodo;

public interface NodoRepository extends Repository<Nodo, Long> {

    List<Nodo> findByTipoNodoIn(Collection<Integer> tipos);
}
