package com.example.gestionturnos.data.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gestionturnos.data.entities.TurnoEntity;

import java.util.List;

@Dao
public interface TurnoDao {

    @Insert
    void insert(TurnoEntity turno);

    @Update
    void update(TurnoEntity turno);

    @Delete
    void delete(TurnoEntity turno);

    @Query("SELECT * FROM turnos")
    List<TurnoEntity> getAll();

    @Query("SELECT * FROM turnos WHERE usuario_id = :usuarioId")
    List<TurnoEntity> getTurnosByUsuario(int usuarioId);

    @Query("SELECT * FROM turnos WHERE servicio_id = :servicioId")
    List<TurnoEntity> getTurnosByServicio(int servicioId);

    @Query("SELECT * FROM turnos WHERE id = :id LIMIT 1")
    TurnoEntity findById(int id);
}
