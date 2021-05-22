package com.wisboo.asignacion.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.wisboo.asignacion.model.Favorita;
import com.wisboo.asignacion.model.Respuesta;
import com.wisboo.asignacion.repository.FavoritaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin(origins = "*")
public class favoritoController {

    @Autowired
    FavoritaRepository favoritaRespository;

    @Autowired
    private RestTemplate clienteRest;

    @Value("${api.key}")
    String APIKEY;

    // GET images/search?query&page&size
    // Endpoint para listar las imágenes según el query indicado, este endpoint debe
    // hacer uso del endpoint de búsqueda de Unsplash
    // https://unsplash.com/documentation#search-photos.
    @GetMapping(value = "/images/search")
    public ResponseEntity<?> dummyget(@RequestParam String query, @RequestParam String page,
            @RequestParam String size) {
        String token = APIKEY;
        String params = query + "&per_page= " + size + "&page=" + page + "&client_id=" + token;
        String url = "https://api.unsplash.com/search/photos?query=" + params;

        Gson gson = new Gson();

        String salida = clienteRest.getForObject(url, String.class);
        Respuesta respuesta = gson.fromJson(salida, Respuesta.class);

        return new ResponseEntity<Respuesta>(respuesta, HttpStatus.OK);
    }

    // POST {"url":""}
    // guardar en local url de imagen
    @PostMapping(path = "/insert")
    public ResponseEntity<?> create(@RequestBody(required = false) Favorita favorita, BindingResult result) {
        Favorita FavoritaNew = null;
        Map<String, Object> response = new HashMap<>();

        if (favorita == null) {
            return new ResponseEntity<>("body vacio", HttpStatus.BAD_REQUEST);
        }

        if (favorita.getUrl() == null || favorita.getUrl() == "") {
            return new ResponseEntity<>("El campo url no se encuentra o no tiene informacion", HttpStatus.BAD_REQUEST);
        }

        try {
            FavoritaNew = favoritaRespository.save(favorita);
        } catch (DataAccessException e) {
            response.put("mensaje", "La url ya existe.");
            // response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La imagen se ha agregado exitosamente");
        response.put("imagen", FavoritaNew);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    // GET images?page&size
    // Endpoint para listar las imágenes guardadas como favoritas. Estas imágenes
    // provienen de la DB local.
    @GetMapping(path = "/fav")
    public ResponseEntity<?> getfavDir() {

        List<Favorita> favorita = (List<Favorita>) favoritaRespository.findAll();

        return new ResponseEntity<List<Favorita>>(favorita, HttpStatus.OK);
    }

    @GetMapping(path = "/favpage", params = { "page", "size" })
    public ResponseEntity<?> findPaginated(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        if (page < 1) {
            return new ResponseEntity<>("El campo page debe ser mayor o igual a 1", HttpStatus.BAD_REQUEST);
        }
        page--;

        Pageable elements = PageRequest.of(page, size);
        Page<Favorita> resultPage = favoritaRespository.findAll(elements);
        List<Favorita> response = resultPage.getContent();

        return new ResponseEntity<List<Favorita>>(response, HttpStatus.OK);
    }

}
