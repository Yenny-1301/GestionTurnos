package com.example.gestionturnos.data.repository;

import android.content.Context;

import com.example.gestionturnos.AppDatabase;
import com.example.gestionturnos.DatabaseClient;
import com.example.gestionturnos.Servicio;
import com.example.gestionturnos.data.entities.ServicioEntity;

import java.util.ArrayList;
import java.util.List;

public class ServicioRepository {

    private final AppDatabase db;

    public ServicioRepository(Context context) {
        this.db = DatabaseClient.getInstance(context).getAppDatabase();
    }

    public void insertarServicio(int usuarioId, Servicio servicio) {
        ServicioEntity entity = new ServicioEntity();
        entity.usuarioId = usuarioId;
        entity.nombre = servicio.getNombreServicio();

        try {
            entity.duracion = Integer.parseInt(servicio.getMinutos());
        } catch (Exception e) {
            entity.duracion = 0;
        }

        try {
            entity.precio = Double.parseDouble(servicio.getPrecio());
        } catch (Exception e) {
            entity.precio = 0.0;
        }

        db.servicioDao().insert(entity);
    }

    public List<Servicio> obtenerTodos() {
        List<ServicioEntity> entities = db.servicioDao().getAll();
        return mapToModelList(entities);
    }

    public List<Servicio> obtenerPorUsuario(int usuarioId) {
        List<ServicioEntity> entities = db.servicioDao().getServiciosByUsuario(usuarioId);
        return mapToModelList(entities);
    }

    public void actualizarServicio(int servicioId, Servicio servicio) {
        ServicioEntity entity = db.servicioDao().findById(servicioId);

        if (entity != null) {
            entity.nombre = servicio.getNombreServicio();

            try {
                entity.duracion = Integer.parseInt(servicio.getMinutos());
            } catch (Exception e) {
                entity.duracion = 0;
            }

            try {
                entity.precio = Double.parseDouble(servicio.getPrecio());
            } catch (Exception e) {
                entity.precio = 0.0;
            }

            db.servicioDao().update(entity);
        }
    }

    public void eliminarServicio(int servicioId) {
        ServicioEntity entity = db.servicioDao().findById(servicioId);

        if (entity != null) {
            db.servicioDao().delete(entity);
        }
    }
    private List<Servicio> mapToModelList(List<ServicioEntity> entities) {
        List<Servicio> servicios = new ArrayList<>();

        for (ServicioEntity entity : entities) {
            Servicio s = new Servicio();
            s.setId(entity.id);
            s.setNombreServicio(entity.nombre);
            s.setMinutos(String.valueOf(entity.duracion));
            s.setPrecio(String.valueOf(entity.precio));
            servicios.add(s);
        }

        return servicios;
    }
}
