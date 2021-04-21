package com.example.app_coursework.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;

import java.util.List;

@Dao
public interface WeatherLocationDAO {

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT name, coordinates FROM WeatherLocation WHERE isChosen = false")
    List<WeatherLocation> getUnselectedWeatherLocations();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT name, coordinates FROM WeatherLocation WHERE isChosen = true")
    List<WeatherLocation> getSelectedWeatherLocations();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT name, coordinates FROM WeatherLocation WHERE name = :inputName")
    WeatherLocation getChosenWeatherLocation(String inputName);

    @Query("UPDATE weatherlocation SET isChosen=:status WHERE name = :inputName")
    void setWeatherLocationAsSelected(String inputName, Boolean status);

    @Insert
    void addWeatherLocation(WeatherLocation... weatherLocation);

    @Insert
    void insertAll(WeatherLocation... weatherLocations);

    @Query("DELETE FROM WeatherLocation")
    void deleteAllWeatherLocations();
}
