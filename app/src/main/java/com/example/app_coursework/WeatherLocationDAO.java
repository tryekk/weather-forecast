package com.example.app_coursework;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;

import java.util.List;

@Dao
public interface WeatherLocationDAO {

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT name FROM WeatherLocation WHERE isChosen = false")
    List<WeatherLocation> getUnselectedWeatherLocations();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT name, coordinates FROM WeatherLocation WHERE name = :inputName")
    WeatherLocation getChosenWeatherLocation(String inputName);

    @Insert
    void addWeatherLocation(WeatherLocation... weatherLocation);

    @Query("DELETE FROM WeatherLocation")
    void deleteAllWeatherLocations();
}
