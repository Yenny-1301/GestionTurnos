package com.example.gestionturnos;

import java.io.Serializable;
public class Servicio implements Serializable{
    private String nombreServicio;
    private String minutos;
    private String precio;

    public Servicio( String nombreServicio, String minutos, String precio) {
        this.nombreServicio = nombreServicio;
        this.minutos = minutos;
        this.precio = precio;
    }

    public String getNombreServicio() { return nombreServicio; }
    public String getMinutos() { return minutos; }
    public String getPrecio() { return precio; }
}
