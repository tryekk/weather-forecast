package com.example.app_coursework.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

@Database(entities = {WeatherLocation.class}, version = 1, exportSchema = false)
public abstract class WeatherLocationDatabase extends RoomDatabase {

    private static WeatherLocationDatabase INSTANCE;

    public abstract WeatherLocationDAO weatherLocationDAO();

    public synchronized static WeatherLocationDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = buildDatabase(context);
        }
        return INSTANCE;
    }

    private static WeatherLocationDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context,
            WeatherLocationDatabase.class,
            "weather-database")
            .addCallback(new Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            getInstance(context).weatherLocationDAO().insertAll(WeatherLocation.populateData());
                        }
                    });
                }
            })
            .build();
    }
}
