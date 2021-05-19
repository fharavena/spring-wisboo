package com.wisboo.asignacion.repository;

import com.wisboo.asignacion.model.Favorita;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoritaRepository extends CrudRepository<Favorita, Long> {

}
