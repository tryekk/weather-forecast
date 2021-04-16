package com.example.app_coursework;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class weatherLocation {

    @PrimaryKey
    private String name;
    @ColumnInfo
    private double[] coordinates;
    @ColumnInfo(defaultValue = "false")
    private boolean isChosen;

    public weatherLocation(String name, boolean isChosen) {
        this.name = name;
        this.isChosen = isChosen;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }
}
