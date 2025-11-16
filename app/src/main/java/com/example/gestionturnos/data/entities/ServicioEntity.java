package com.example.gestionturnos.data.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
@Entity(
    tableName = "servicios",
    foreignKeys = @ForeignKey(
        entity = UsuarioEntity.class,
        parentColumns = "id",
        childColumns = "usuario_id",
        onDelete = ForeignKey.CASCADE
    )
)
public class ServicioEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "usuario_id")
    public int usuarioId;

    public String nombre;

    public int duracion; // minutos

    public double precio;
}
