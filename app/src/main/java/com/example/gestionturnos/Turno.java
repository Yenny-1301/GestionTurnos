package com.example.gestionturnos;

import java.io.Serializable;
public class Turno implements Serializable {
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
        this.fecha = fecha;
        this.hora = hora;
        this.contacto = contacto;
        this.servicio = servicio;
        this.apellidoCliente = apellidoCliente;
        this.comentarios = comentarios;
        this.estado = "Pendiente";
    }

    public String getNombreCliente() { return nombreCliente; }
    public String getApellidoCliente(){ return apellidoCliente;}
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public String getContacto() { return contacto; }
    public String getServicio() { return servicio; }
    public String getComentarios(){return comentarios;}
    public String getEstado(){return estado;}

}
