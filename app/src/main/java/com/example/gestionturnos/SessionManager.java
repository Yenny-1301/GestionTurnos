package com.example.gestionturnos;

import android.content.Context;

public class SessionManager {
    private static final String PREF_NAME = "usuario_prefs";
    private static final String KEY_USER_ID = "usuario_id";

    public static void guardarUsuarioActivo(Context context, int usuarioId) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putInt(KEY_USER_ID, usuarioId)
                .apply();
    }

    public static int obtenerUsuarioActivo(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getInt(KEY_USER_ID, -1);
    }

    public static void cerrarSesion(Context context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .remove(KEY_USER_ID)
                .apply();
    }
}
