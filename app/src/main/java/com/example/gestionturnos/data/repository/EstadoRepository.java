package com.example.gestionturnos.data.repository;

import android.content.Context;

import com.example.gestionturnos.AppDatabase;
import com.example.gestionturnos.DatabaseClient;
import com.example.gestionturnos.data.daos.EstadoDao;
import com.example.gestionturnos.data.entities.EstadoEntity;

import java.util.List;

public class EstadoRepository {
    private final EstadoDao estadoDao;

    public EstadoRepository(Context context) {
        AppDatabase db = DatabaseClient.getInstance(context).getAppDatabase();
        estadoDao = db.estadoDao();
    }

    public List<EstadoEntity> getAll() {
        return estadoDao.getAll();
    }

    public EstadoEntity findById(int id) {
        return estadoDao.findById(id);
    }

    public void insertarEstado(EstadoEntity estado) {
        estadoDao.insert(estado);
    }
}
