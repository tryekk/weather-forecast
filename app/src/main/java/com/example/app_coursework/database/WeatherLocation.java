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
    private Integer isChosen;

    public WeatherLocation(String name, String coordinates, Integer isChosen) {
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

    public Integer isChosen() {
        return isChosen;
    }

    public void setChosen(Integer chosen) {
        isChosen = chosen;
    }

    public static WeatherLocation[] populateData() {
        return new WeatherLocation[] {
                new WeatherLocation("Cardiff", "51.4816,-3.1791", 0),
                new WeatherLocation("London", "51.5074,0.1278", 0),
                new WeatherLocation("New York", "40.7128,74.0060", 0),
                new WeatherLocation("San Francisco", "37.7749,122.4194", 0),
                new WeatherLocation("Stockholm", "59.3293,18.0686", 0),
                new WeatherLocation("Moscow", "55.7558,37.6173", 0),
                new WeatherLocation("Kuala Lumpur", "3.1390,101.6869", 0),
                new WeatherLocation("Tokyo", "35.6762,139.6503", 0),
                new WeatherLocation("Cologne", "50.9375,6.9603", 0),
                new WeatherLocation("Madrid", "40.4168,3.7038", 0)
        };
    }
}
