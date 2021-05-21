package com.wisboo.asignacion.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.wisboo.asignacion.model.Favorita;
import com.wisboo.asignacion.repository.FavoritaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoritaServiceImpl implements IFavoritaService {
    @Autowired
    private FavoritaRepository favoritaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Favorita> findAll() {
        return (List<Favorita>) favoritaRepository.findAll();
    }

}
