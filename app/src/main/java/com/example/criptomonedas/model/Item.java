package com.example.criptomonedas.model;

import com.google.gson.annotations.SerializedName;

public class Item {
    private String nombre;
    private String simbolo;
    @SerializedName("precioActual")
    private double precio;
    private String imagen;

    // Constructor con imagen personalizada
    public Item(String nombre, String simbolo, double precio, String imagen) {
        this.nombre = nombre;
        this.simbolo = simbolo;
        this.precio = precio;
        this.imagen = imagen;
    }

    // Constructor que asigna automáticamente una imagen basada en el símbolo
    public Item(String nombre, String simbolo, double precio) {
        this.nombre = nombre;
        this.simbolo = simbolo;
        this.precio = precio;
        this.imagen = generarURLImagen(simbolo);
    }

    // Método privado para generar una URL de imagen por símbolo
    private String generarURLImagen(String simbolo) {
        // Puedes personalizar esta lógica con la URL de tu fuente preferida
        return "https://cryptologos.cc/logos/" + simbolo.toLowerCase() + "-logo.png";
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getSimbolo() { return simbolo; }
    public void setSimbolo(String simbolo) { this.simbolo = simbolo; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
}
