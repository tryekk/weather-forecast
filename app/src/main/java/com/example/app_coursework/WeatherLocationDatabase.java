package com.example.app_coursework;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {WeatherLocation.class}, version = 1)
public abstract class WeatherLocationDatabase extends RoomDatabase {

    public abstract WeatherLocationDAO weatherLocationDAO();
}
