package com.example.app_coursework.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class WeatherLocation {

    @PrimaryKey
    @NonNull
    private String name;
    @ColumnInfo
    private String coordinates;
    @ColumnInfo(defaultValue = "false")
    private Boolean isChosen;

    public WeatherLocation(String name, String coordinates, Boolean isChosen) {
        this.name = name;
        this.coordinates = coordinates;
        this.isChosen = isChosen;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isChosen() {
        return isChosen;
    }

    public void setChosen(Boolean chosen) {
        isChosen = chosen;
    }

    public static WeatherLocation[] populateData() {
        return new WeatherLocation[] {
                new WeatherLocation("Cardiff", "51.4816,-3.1791", false),
                new WeatherLocation("London", "51.5074,0.1278", false),
                new WeatherLocation("New York", "40.7128,74.0060", false),
                new WeatherLocation("San Francisco", "37.7749,122.4194", false),
                new WeatherLocation("Stockholm", "59.3293,18.0686", false),
                new WeatherLocation("Moscow", "55.7558,37.6173", false),
                new WeatherLocation("Kuala Lumpur", "3.1390,101.6869", false),
                new WeatherLocation("Tokyo", "35.6762,139.6503", false),
                new WeatherLocation("Cologne", "50.9375,6.9603", false),
                new WeatherLocation("Madrid", "40.4168,3.7038", false)
        };
    }
}
