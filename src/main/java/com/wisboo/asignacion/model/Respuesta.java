package com.wisboo.asignacion.model;

import java.util.List;

public class Respuesta {
    private Integer total;
    private Integer total_pages;
    private List<Resultado> results;

    public Integer getTotal() {
        return total;
    }

    public Integer getTotal_pages() {
        return total_pages;
    }


    public List<Resultado> getResultados() {
        return results;
    }


}
