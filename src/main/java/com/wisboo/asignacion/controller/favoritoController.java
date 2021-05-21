package com.wisboo.asignacion.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.wisboo.asignacion.model.Favorita;
import com.wisboo.asignacion.model.Respuesta;
import com.wisboo.asignacion.repository.FavoritaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "")
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
            response.put("mensaje", "Error al crear en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El cliente ha sido creado con éxito");
        response.put("cliente", FavoritaNew);

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

}
