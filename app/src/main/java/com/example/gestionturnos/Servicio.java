package com.example.gestionturnos;

import android.content.Context;

import androidx.core.content.ContextCompat;
import java.io.Serializable;
import java.util.Objects;
public class Servicio implements Serializable{
    private int id;
    private String nombreServicio;
    private String minutos;
    private String precio;

    public Servicio( String nombreServicio, String minutos, String precio) {
        this.nombreServicio = nombreServicio;
        this.minutos = minutos;
        this.precio = precio;
    }

    public Servicio() { this.precio = "0"; }

    public int getId() { return id; }
    public String getNombreServicio() { return nombreServicio; }
    public String getMinutos() { return minutos; }
    public String getPrecio() { return precio; }

    public void setId(int id) { this.id = id; }
    public void setNombreServicio(String nombreServicio) { this.nombreServicio = nombreServicio; }
    public void setMinutos(String minutos) { this.minutos = minutos; }
    public void setPrecio(String precio) { this.precio = precio; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Servicio servicio = (Servicio) o;
        return Objects.equals(nombreServicio, servicio.nombreServicio);
    }

    @Override
    public int hashCode() {return Objects.hash(nombreServicio);}
}
