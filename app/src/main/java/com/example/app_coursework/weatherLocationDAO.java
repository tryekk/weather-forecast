package com.example.app_coursework;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface weatherLocationDAO {

    @Query("SELECT name FROM weatherLocation WHERE isChosen = 0")
    List<weatherLocation> getUnselectedWeatherLocations();

    @Insert
    void addWeatherLocation(WeatherLocation... weatherLocation);
}
