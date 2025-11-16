package com.example.gestionturnos.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "estados")
public class EstadoEntity {

    @PrimaryKey
    public int id;

    public String nombre;

    public EstadoEntity(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
}

