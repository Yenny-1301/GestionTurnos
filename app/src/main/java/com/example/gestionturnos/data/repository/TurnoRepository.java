package com.example.gestionturnos.data.repository;

import android.content.Context;

import com.example.gestionturnos.AppDatabase;
import com.example.gestionturnos.DatabaseClient;
import com.example.gestionturnos.Turno;
import com.example.gestionturnos.data.entities.TurnoEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TurnoRepository {
    private final AppDatabase db;

    // Constantes de estados
    public static final int ESTADO_PENDIENTE = 1;
    public static final int ESTADO_CONFIRMADO = 2;
    public static final int ESTADO_CANCELADO = 3;

    public TurnoRepository(Context context) {
        this.db = DatabaseClient.getInstance(context).getAppDatabase();
    }

    public void insertarTurno(int usuarioId, Turno turno) {
        TurnoEntity entity = new TurnoEntity();
        entity.usuarioId = usuarioId;
        entity.nombreCliente = turno.getNombreCliente();
        entity.apellidoCliente = turno.getApellidoCliente();
        entity.servicioId = turno.getServicioId();
        entity.contacto = turno.getContacto();
        entity.comentarios = turno.getComentarios();

        // estado es PENDIENTE al crear un nuevo turno
        entity.estadoId = ESTADO_PENDIENTE;

        // concat fecha y hora
        entity.fechaTurno = concatenarFechaHora(turno.getFecha(), turno.getHora());

        // Timestamps
        String fechaActual = obtenerFechaActual();
        entity.fechaCreacion = fechaActual;
        entity.fechaActualizacion = fechaActual;

        db.turnoDao().insert(entity);
    }
    //editar turno existentes
    public void actualizarTurnoCompleto(int turnoId, Turno turno) {
        TurnoEntity entity = obtenerTurnoPorId(turnoId);

        if (entity != null) {
            entity.nombreCliente = turno.getNombreCliente();
            entity.apellidoCliente = turno.getApellidoCliente();
            entity.servicioId = turno.getServicioId();
            entity.contacto = turno.getContacto();
            entity.comentarios = turno.getComentarios();
            entity.fechaTurno = concatenarFechaHora(turno.getFecha(), turno.getHora());

            // mantener el id estado
            if (turno.getEstadoId() != null) {
                entity.estadoId = turno.getEstadoId();
            }

            actualizarTurno(entity);
        }
    }

    /* cambia el estado a confirmado
    public void confirmarTurno(int turnoId) {
        TurnoEntity turno = obtenerTurnoPorId(turnoId);
        if (turno != null) {
            turno.estadoId = ESTADO_CONFIRMADO;
            actualizarTurno(turno);
        }
    }

    // cambia el estado a cancelado
    public void cancelarTurno(int turnoId) {
        TurnoEntity turno = obtenerTurnoPorId(turnoId);
        if (turno != null) {
            turno.estadoId = ESTADO_CANCELADO;
            actualizarTurno(turno);
        }
    }


     * Cambia el estado de un turno a PENDIENTE

    public void marcarComoPendiente(int turnoId) {
        TurnoEntity turno = obtenerTurnoPorId(turnoId);
        if (turno != null) {
            turno.estadoId = ESTADO_PENDIENTE;
            actualizarTurno(turno);
        }
    }*/

    public List<TurnoEntity> obtenerTodosTurnos() {
        return db.turnoDao().getAll();
    }

    public List<TurnoEntity> obtenerTurnosPorUsuario(int usuarioId) {
        return db.turnoDao().getTurnosByUsuario(usuarioId);
    }

    /*public List<TurnoEntity> obtenerTurnosPorServicio(int servicioId) {
        return db.turnoDao().getTurnosByServicio(servicioId);
    }

    public List<TurnoEntity> obtenerTurnosPorFecha(String fecha) {
        return db.turnoDao().getTurnosByFecha(fecha);
    }

    public List<TurnoEntity> obtenerTurnosPorEstado(int estadoId) {
        return db.turnoDao().getTurnosByEstado(estadoId);
    }*/

    public TurnoEntity obtenerTurnoPorId(int id) {
        return db.turnoDao().findById(id);
    }

    public void actualizarTurno(TurnoEntity turno) {
        turno.fechaActualizacion = obtenerFechaActual();
        db.turnoDao().update(turno);
    }

    public void eliminarTurno(TurnoEntity turno) {
        db.turnoDao().delete(turno);
    }

    /*public void eliminarTurnoPorId(int id) {
        TurnoEntity turno = obtenerTurnoPorId(id);
        if (turno != null) {
            eliminarTurno(turno);
        }
    }*/

    /**
     * Concatena fecha y hora en formato "yyyy-MM-dd HH:mm"
     * @param fecha en formato "dd/MM/yyyy" o similar
     * @param hora en formato "HH:mm"
     * @return String con fecha y hora concatenados
     */

    public int obtenerPendientesMes(int usuarioId, String yearMonth) {
        return db.turnoDao().countPendientesMes(usuarioId, yearMonth);
    }

    public int obtenerConfirmadosMes(int usuarioId, String yearMonth) {
        return db.turnoDao().countConfirmadosMes(usuarioId, yearMonth);
    }

    public int obtenerCanceladosMes(int usuarioId, String yearMonth) {
        return db.turnoDao().countCanceladosMes(usuarioId, yearMonth);
    }

    public double obtenerRendimientoMensual(int usuarioId, String yearMonth) {
        return db.turnoDao().sumRendimientoMensual(usuarioId, yearMonth);
    }
    private String concatenarFechaHora(String fecha, String hora) {
        if (fecha == null || hora == null) {
            return obtenerFechaActual();
        }

        try {
            String[] partesFecha = fecha.split("/");
            if (partesFecha.length == 3) {
                // dd/MM/yyyy -> yyyy-MM-dd
                return partesFecha[2] + "-" + partesFecha[1] + "-" + partesFecha[0] + " " + hora;
            } else {
                // Si ya viene en otro formato, concatenar directamente
                return fecha + " " + hora;
            }
        } catch (Exception e) {
            // En caso de error, retornar fecha actual
            return obtenerFechaActual();
        }
    }

    private String obtenerFechaActual() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}