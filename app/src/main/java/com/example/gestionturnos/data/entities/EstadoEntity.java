package com.example.gestionturnos.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "estados")
public class EstadoEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombre;
}

