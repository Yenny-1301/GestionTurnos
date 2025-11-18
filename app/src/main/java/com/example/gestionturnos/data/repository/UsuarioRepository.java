package com.example.gestionturnos.data.repository;

import android.content.Context;

import com.example.gestionturnos.AppDatabase;
import com.example.gestionturnos.DatabaseClient;
import com.example.gestionturnos.Usuario;
import com.example.gestionturnos.data.entities.UsuarioEntity;
import com.example.gestionturnos.utils.HashUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UsuarioRepository {

    private final AppDatabase db;

    public UsuarioRepository(Context context) {
        this.db = DatabaseClient.getInstance(context).getAppDatabase();
    }
    public long insertarUsuario(Usuario usuario) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String fechaLegible = sdf.format(new Date());

        UsuarioEntity entity = new UsuarioEntity();
        entity.nombre = usuario.getNombre();
        entity.correoElectronico = usuario.getEmail();
        entity.contrasena =  HashUtil.sha256(usuario.getPassword());
        entity.fechaCreacion = fechaLegible;

        return db.usuarioDao().insert(entity);
    }

    public UsuarioEntity obtenerUsuarioPorEmail(String email) {
        return db.usuarioDao().findByCorreo(email);
    }

    public boolean validarLogin(String email, String passwordIngresada) {
        UsuarioEntity usuario = obtenerUsuarioPorEmail(email);

        if (usuario == null) return false;

        String passwordEncriptada = HashUtil.sha256(passwordIngresada);

        return usuario.contrasena.equals(passwordEncriptada);
    }

    public UsuarioEntity obtenerUsuarioPorId(int id) {
        return db.usuarioDao().findById(id);
    }

}
