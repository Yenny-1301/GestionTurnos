package com.example.gestionturnos.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

import java.util.Date;
@Entity(tableName = "usuarios")
public class UsuarioEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "correo_electronico")
    public String correoElectronico;

    @ColumnInfo(name = "contrasena")
    public String contrasena;

    @ColumnInfo(name = "fecha_creacion")
    public Date fechaCreacion;
}
