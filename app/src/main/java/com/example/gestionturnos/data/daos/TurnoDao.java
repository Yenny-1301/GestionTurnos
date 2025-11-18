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
    @Query("SELECT * FROM turnos WHERE fecha_turno = :fecha")
    List<TurnoEntity> getTurnosByFecha(String fecha);
    // AGREGADOS - Para filtrar por estado
    @Query("SELECT * FROM turnos WHERE estado_id = :estadoId")
    List<TurnoEntity> getTurnosByEstado(int estadoId);

    @Query("SELECT * FROM turnos WHERE usuario_id = :usuarioId AND estado_id = :estadoId")
    List<TurnoEntity> getTurnosByUsuarioYEstado(int usuarioId, int estadoId);

    @Query(
            "SELECT COUNT(*) " +
                    "FROM turnos t " +
                    "INNER JOIN estados e ON t.estado_id = e.id " +
                    "WHERE t.usuario_id = :usuarioId " +
                    "AND e.nombre = 'Pendiente' " +
                    "AND strftime('%Y-%m', t.fecha_turno) = :yearMonth"
    )
    int countPendientesMes(int usuarioId, String yearMonth);

    @Query(
            "SELECT COUNT(*) " +
                    "FROM turnos t " +
                    "INNER JOIN estados e ON t.estado_id = e.id " +
                    "WHERE t.usuario_id = :usuarioId " +
                    "AND e.nombre = 'Confirmado' " +
                    "AND strftime('%Y-%m', t.fecha_turno) = :yearMonth"
    )
    int countConfirmadosMes(int usuarioId, String yearMonth);

    @Query(
            "SELECT COUNT(*) " +
                    "FROM turnos t " +
                    "INNER JOIN estados e ON t.estado_id = e.id " +
                    "WHERE t.usuario_id = :usuarioId " +
                    "AND e.nombre = 'Cancelado' " +
                    "AND strftime('%Y-%m', t.fecha_turno) = :yearMonth"
    )
    int countCanceladosMes(int usuarioId, String yearMonth);

    @Query(
            "SELECT IFNULL(SUM(s.precio), 0) " +
                    "FROM turnos t " +
                    "INNER JOIN estados e ON t.estado_id = e.id " +
                    "INNER JOIN servicios s ON t.servicio_id = s.id " +
                    "WHERE t.usuario_id = :usuarioId " +
                    "AND e.nombre = 'Confirmado' " +
                    "AND strftime('%Y-%m', t.fecha_turno) = :yearMonth"
    )
    double sumRendimientoMensual(int usuarioId, String yearMonth);
}
