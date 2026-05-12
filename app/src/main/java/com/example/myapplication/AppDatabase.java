package com.example.myapplication;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Gasto.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract GastoDAO gastoDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "banco_gastos")
                    .allowMainThreadQueries() // Simplifica o uso inicial (não recomendado para apps em produção complexos)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}