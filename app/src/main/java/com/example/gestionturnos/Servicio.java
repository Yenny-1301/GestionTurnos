package com.example.gestionturnos;

public class Servicio {
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
