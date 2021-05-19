package com.wisboo.asignacion.controller;

import java.util.ArrayList;

import com.wisboo.asignacion.model.Favorita;
import com.wisboo.asignacion.service.IFavoritaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class favoritoController {

    @Autowired
    IFavoritaService favoritaService;

    @GetMapping(path = "/fav")
    public ArrayList<Favorita> getfav() {
        return (ArrayList<Favorita>) favoritaService.findAll();
    }

    // GET images/search?query&page&size
    // Endpoint para listar las imágenes según el query indicado, este endpoint debe
    // hacer uso del endpoint de búsqueda de Unsplash
    // https://unsplash.com/documentation#search-photos.

    // POST {"url":""}
    // guardar en local url de imagen

    // GET images?page&size
    // Endpoint para listar las imágenes guardadas como favoritas. Estas imágenes
    // provienen de la DB local.

}
