package com.example.gestionturnos;

public class Turno {
    private String nombreCliente;
    private String fecha;
    private String hora;
    private String contacto;
    private String servicio;

    public Turno(String nombreCliente, String fecha, String hora, String contacto, String servicio) {
        this.nombreCliente = nombreCliente;
        this.fecha = fecha;
        this.hora = hora;
        this.contacto = contacto;
        this.servicio = servicio;
    }

    public String getNombreCliente() { return nombreCliente; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public String getContacto() { return contacto; }
    public String getServicio() { return servicio; }

}
