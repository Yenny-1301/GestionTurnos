package com.example.gestionturnos.data.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.gestionturnos.data.entities.EstadoEntity;

import java.util.List;

@Dao
public interface EstadoDao {

    @Insert
    void insert(EstadoEntity estado);

    @Query("SELECT * FROM estados")
    List<EstadoEntity> getAll();

    @Query("SELECT * FROM estados WHERE id = :id LIMIT 1")
    EstadoEntity findById(int id);
}
