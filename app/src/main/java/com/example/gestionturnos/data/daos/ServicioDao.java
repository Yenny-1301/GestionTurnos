package com.example.gestionturnos.data.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gestionturnos.data.entities.ServicioEntity;

import java.util.List;

@Dao
public interface ServicioDao {

    @Insert
    void insert(ServicioEntity servicio);

    @Update
    void update(ServicioEntity servicio);

    @Delete
    void delete(ServicioEntity servicio);

    @Query("SELECT * FROM servicios")
    List<ServicioEntity> getAll();

    @Query("SELECT * FROM servicios WHERE usuario_id = :usuarioId")
    List<ServicioEntity> getServiciosByUsuario(int usuarioId);

    @Query("SELECT * FROM servicios WHERE id = :id LIMIT 1")
    ServicioEntity findById(int id);
}
