package com.example.gestionturnos;

import android.content.Context;

public class SessionManager {
    private static final String PREF_NAME = "usuario_prefs";
    private static final String KEY_USER_ID = "usuario_id";
    private static final String KEY_FINGERPRINT_ENABLED = "fingerprint_enabled";
    private static final String KEY_FINGERPRINT_USER_ID = "fingerprint_user_id";
    private static final String FINGERPRINT_PROMPT_SHOWN = "fingerprint_prompt_shown";

    public static void guardarUsuarioActivo(Context context, int usuarioId) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putInt(KEY_USER_ID, usuarioId)
                .putBoolean("isLoggedIn", true)
                .apply();
    }

    public static void habilitarHuella(Context context, int userId) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(KEY_FINGERPRINT_ENABLED, true)
                .putInt(KEY_FINGERPRINT_USER_ID, userId)
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
    public static boolean huellaYaPreguntada(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getBoolean(FINGERPRINT_PROMPT_SHOWN, false);
    }

    public static void marcarHuellaPreguntada(Context context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(FINGERPRINT_PROMPT_SHOWN, true)
                .apply();
    }
    public static boolean huellaHabilitada(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getBoolean(KEY_FINGERPRINT_ENABLED, false);
    }
    public static int obtenerUsuarioHuella(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getInt(KEY_FINGERPRINT_USER_ID, -1);
    }
}
