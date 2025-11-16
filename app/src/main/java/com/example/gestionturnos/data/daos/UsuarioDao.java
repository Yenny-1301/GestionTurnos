package com.example.gestionturnos.data.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.gestionturnos.data.entities.UsuarioEntity;

import java.util.List;

@Dao
public interface UsuarioDao {

    @Insert
    long insert(UsuarioEntity usuario);

    @Query("SELECT * FROM usuarios")
    List<UsuarioEntity> getAll();

    @Query("SELECT * FROM usuarios WHERE correo_electronico = :correo LIMIT 1")
    UsuarioEntity findByCorreo(String correo);

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    UsuarioEntity findById(int id);
}

