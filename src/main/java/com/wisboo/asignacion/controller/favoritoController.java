package com.wisboo.asignacion.controller;

import java.util.List;

import com.google.gson.Gson;
import com.wisboo.asignacion.model.Favorita;
import com.wisboo.asignacion.model.Respuesta;
import com.wisboo.asignacion.repository.FavoritaRepository;
import com.wisboo.asignacion.service.IFavoritaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "")
public class favoritoController {

    @Autowired
    IFavoritaService favoritaService;

    @Autowired
    FavoritaRepository favoritaRespository;

    @Autowired
    private RestTemplate clienteRest;

    @Value("${api.key}")
    String APIKEY;

    @GetMapping(path = "/fav")
    public List<Favorita> getfav() {
        return favoritaService.findAll();
    }

    @GetMapping(path = "/favdir")
    public List<Favorita> getfavDir() {
        return (List<Favorita>) favoritaRespository.findAll();
    }

    @GetMapping(value = "/images/search")
    public Respuesta dummyget(@RequestParam String query, @RequestParam String page, @RequestParam String size) {
        String token = APIKEY;
        String params = query + "&per_page= " + size + "&page=" + page + "&client_id=" + token;
        String url = "https://api.unsplash.com/search/photos?query=" + params;

        Gson gson = new Gson();

        String salida = clienteRest.getForObject(url, String.class);
        Respuesta respuesta = gson.fromJson(salida, Respuesta.class);

        // System.out.println(APIKEY);

        return respuesta;
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
