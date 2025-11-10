package com.example.gestionturnos.data.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

import java.util.Date;

@Entity(
    tableName = "turnos",
    foreignKeys = {
        @ForeignKey(
                entity = UsuarioEntity.class,
                parentColumns = "id",
                childColumns = "usuario_id",
                onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
                entity = ServicioEntity.class,
                parentColumns = "id",
                childColumns = "servicio_id",
                onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
                entity = EstadoEntity.class,
                parentColumns = "id",
                childColumns = "estado_id",
                onDelete = ForeignKey.SET_NULL
        )
    }
)
public class TurnoEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "usuario_id")
    public int usuarioId;

    @ColumnInfo(name = "servicio_id")
    public int servicioId;

    @ColumnInfo(name = "nombre_cliente")
    public String nombreCliente;

    @ColumnInfo(name = "apellido_cliente")
    public String apellidoCliente;

    public String contacto;

    public String comentarios;

    @ColumnInfo(name = "estado_id")
    public Integer estadoId;

    @ColumnInfo(name = "fecha_turno")
    public Date fechaTurno;

    @ColumnInfo(name = "fecha_creacion")
    public Date fechaCreacion;

    @ColumnInfo(name = "fecha_actualizacion")
    public Date fechaActualizacion;
}

