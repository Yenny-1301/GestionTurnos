package com.example.gestionturnos.data.repository;

import android.content.Context;

import com.example.gestionturnos.DatabaseClient;
import com.example.gestionturnos.Servicio;
import com.example.gestionturnos.data.entities.ServicioEntity;

import java.util.ArrayList;
import java.util.List;

public class ServicioRepository {
    public void insertarServicio(Context context, int usuarioId, Servicio servicio) {
        ServicioEntity entity = new ServicioEntity();
        entity.usuarioId = usuarioId;
        entity.nombre = servicio.getNombreServicio();

        try {
            entity.duracion = Integer.parseInt(servicio.getMinutos());
        } catch (NumberFormatException e) {
            entity.duracion = 0;
        }

        try {
            entity.precio = Double.parseDouble(servicio.getPrecio());
        } catch (NumberFormatException e) {
            entity.precio = 0.0;
        }

        DatabaseClient.getInstance(context)
                .getAppDatabase()
                .servicioDao()
                .insert(entity);
    }
    public List<Servicio> obtenerTodos(Context context) {
        List<ServicioEntity> entities = DatabaseClient.getInstance(context)
                .getAppDatabase()
                .servicioDao()
                .getAll();

        return mapToModelList(entities);
    }
    public List<Servicio> obtenerPorUsuario(Context context, int usuarioId) {
        List<ServicioEntity> entities = DatabaseClient.getInstance(context)
                .getAppDatabase()
                .servicioDao()
                .getServiciosByUsuario(usuarioId);

        return mapToModelList(entities);
    }
    public void actualizarServicio(Context context, int servicioId, Servicio servicio) {
        ServicioEntity entity = DatabaseClient.getInstance(context)
                .getAppDatabase()
                .servicioDao()
                .findById(servicioId);

        if (entity != null) {
            entity.nombre = servicio.getNombreServicio();
            entity.duracion = Integer.parseInt(servicio.getMinutos());
            entity.precio = Double.parseDouble(servicio.getPrecio());

            DatabaseClient.getInstance(context)
                    .getAppDatabase()
                    .servicioDao()
                    .update(entity);
        }
    }

    public void eliminarServicio(Context context, int servicioId) {
        ServicioEntity entity = DatabaseClient.getInstance(context)
                .getAppDatabase()
                .servicioDao()
                .findById(servicioId);

        if (entity != null) {
            DatabaseClient.getInstance(context)
                    .getAppDatabase()
                    .servicioDao()
                    .delete(entity);
        }
    }

    private List<Servicio> mapToModelList(List<ServicioEntity> entities) {
        List<Servicio> servicios = new ArrayList<>();
        for (ServicioEntity entity : entities) {
            Servicio s = new Servicio();
            s.setNombreServicio(entity.nombre);
            s.setMinutos(String.valueOf(entity.duracion));
            s.setPrecio(String.valueOf(entity.precio));
            servicios.add(s);
        }
        return servicios;
    }
}
