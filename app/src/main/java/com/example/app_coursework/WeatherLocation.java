package com.example.app_coursework;

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
                new WeatherLocation("San Francisco", "37.7749,122.4194", false)
        };
    }
}
