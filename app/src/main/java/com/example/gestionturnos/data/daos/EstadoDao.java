package com.example.gestionturnos.data.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.gestionturnos.data.entities.EstadoEntity;

import java.util.List;

@Dao
public interface EstadoDao {
    @Query("SELECT * FROM estados")
    List<EstadoEntity> getAll();

    @Query("SELECT * FROM estados WHERE id = :id LIMIT 1")
    EstadoEntity findById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(EstadoEntity estado);
}
