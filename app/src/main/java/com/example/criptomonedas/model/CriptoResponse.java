package com.example.criptomonedas.model;

import java.util.List;

public class CriptoResponse {
    private List<Item> criptomonedas;

    public List<Item> getCriptomonedas() {
        return criptomonedas;
    }

    public void setCriptomonedas(List<Item> criptomonedas) {
        this.criptomonedas = criptomonedas;
    }
}
