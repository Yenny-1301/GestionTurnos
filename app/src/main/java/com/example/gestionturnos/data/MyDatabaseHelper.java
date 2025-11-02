package com.example.gestionturnos.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "TurnManagement.db";
    private static final int DABATASE_VERSION = 1;

    public static final String TABLE_USERS = "users";
    public static final String TABLE_SERVICES = "services";
    public static final String TABLE_TURNS = "turns";


    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DABATASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsers = "CREATE TABLE " + TABLE_USERS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT UNIQUE, " +
                "password TEXT, " +
                "created_date TEXT);";

        String createServices = "CREATE TABLE " + TABLE_SERVICES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "description TEXT, " +
                "user_id INTEGER, " +
                "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE);";

        String createTurns = "CREATE TABLE " + TABLE_TURNS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date TEXT, " +
                "hour TEXT, " +
                "service_id INTEGER, " +
                "user_id INTEGER, " +
                "FOREIGN KEY(service_id) REFERENCES services(id) ON DELETE CASCADE, " +
                "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE);";

        db.execSQL(createUsers);
        db.execSQL(createServices);
        db.execSQL(createTurns);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TURNS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }


}
