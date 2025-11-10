package com.example.gestionturnos.data.repository;

import android.content.Context;

import com.example.gestionturnos.DatabaseClient;
import com.example.gestionturnos.Usuario;
import com.example.gestionturnos.data.entities.UsuarioEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UsuarioRepository {
    public void insertarUsuario(Context context, Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.correoElectronico = usuario.getEmail();
        entity.contrasena = usuario.getPassword();
        entity.fechaCreacion = new Date();

        DatabaseClient.getInstance(context)
                .getAppDatabase()
                .usuarioDao()
                .insert(entity);
    }

}
