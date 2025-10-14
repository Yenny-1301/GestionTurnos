package com.example.gestionturnos;

import android.content.Context;

import androidx.core.content.ContextCompat;

import java.io.Serializable;
import java.util.Objects;

public class Turno implements Serializable {

    public static final String ESTADO_PENDIENTE = "Pendiente";
    public static final String ESTADO_CONFIRMADO = "Confirmado";
    public static final String ESTADO_CANCELADO = "Cancelado";
    private String nombreCliente;
    private String apellidoCliente;
    private String fecha;
    private String hora;
    private String contacto;
    private String servicio;
    private String comentarios;
    private String estado;
    public Turno(String nombreCliente, String apellidoCliente,String fecha, String hora, String contacto, String servicio, String comentarios) {
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.contacto = contacto;
        this.servicio = servicio;
        this.fecha = fecha;
        this.hora = hora;
        this.comentarios = comentarios;
        this.estado = ESTADO_PENDIENTE;
    }

    public Turno() {
        this.estado = ESTADO_PENDIENTE;
    }

    public String getNombreCliente() { return nombreCliente; }
    public String getApellidoCliente(){ return apellidoCliente;}
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public String getContacto() { return contacto; }

    public String getServicio() { return servicio; }
    public String getComentarios(){return comentarios;}
    public String getEstado(){return estado;}

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public void setApellidoCliente(String apellidoCliente) {
        this.apellidoCliente = apellidoCliente;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Turno turno = (Turno) o;
        return Objects.equals(contacto, turno.contacto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contacto);
    }

    public int getEstadoColorResource(Context context) {

        int colorResId;
        switch (estado) {
            case ESTADO_CONFIRMADO:
                colorResId = R.color.green;
                break;
            case ESTADO_CANCELADO:
                colorResId = R.color.rojo;
                break;
            case ESTADO_PENDIENTE:
            default:
                colorResId = R.color.azul;
                break;
        }
        return ContextCompat.getColor(context, colorResId);
    }

}
