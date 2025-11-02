package com.example.gestionturnos.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gestionturnos.Usuario;
import com.example.gestionturnos.data.MyDatabaseHelper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserDAO {
    private MyDatabaseHelper dbHelper;

    public UserDAO(Context context) {
        dbHelper = new MyDatabaseHelper(context);
    }

    public long addUser(Usuario user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        Date createdDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String formattedDate = sdf.format(createdDate);
        String hashedPassword = hashPassword(user.getPassword());

        cv.put("email", user.getEmail());
        cv.put("password", hashedPassword);
        cv.put("created_date", formattedDate);

        long id = db.insert(MyDatabaseHelper.TABLE_USERS, null, cv);
        db.close();
        return id;
    }

    public Usuario getUserByEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
        Usuario user = null;
        if (cursor.moveToFirst()) {
            user = new Usuario(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2)
            );
        }
        cursor.close();
        db.close();
        return user;
    }
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
