package com.example.gestionturnos;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.gestionturnos.data.daos.*;
import com.example.gestionturnos.data.entities.*;

import java.util.concurrent.Executors;

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

public abstract class AppDatabase extends RoomDatabase {
    public abstract UsuarioDao usuarioDao();
    public abstract ServicioDao servicioDao();
    public abstract TurnoDao turnoDao();
    public abstract EstadoDao estadoDao();
    private static volatile AppDatabase INSTANCE;
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "gestion_turnos.db")
                            .addCallback(roomCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // Insertar los estados en un hilo aparte
            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase database = INSTANCE;
                EstadoDao estadoDao = database.estadoDao();

                estadoDao.insert(new EstadoEntity(1, "Pendiente"));
                estadoDao.insert(new EstadoEntity(2, "Confirmado"));
                estadoDao.insert(new EstadoEntity(3, "Cancelado"));
            });
        }
    };
}
