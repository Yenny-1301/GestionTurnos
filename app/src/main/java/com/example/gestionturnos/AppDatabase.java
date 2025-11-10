package com.example.gestionturnos;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.gestionturnos.data.daos.*;
import com.example.gestionturnos.data.entities.*;
import com.example.gestionturnos.data.converters.DateConverter;

@Database(
        entities = {
                UsuarioEntity.class,
                ServicioEntity.class,
                TurnoEntity.class,
                EstadoEntity.class
        },
        version = 1,
        exportSchema = false
)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UsuarioDao usuarioDao();
    public abstract ServicioDao servicioDao();
    public abstract TurnoDao turnoDao();
    public abstract EstadoDao estadoDao();
}
