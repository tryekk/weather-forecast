package com.example.app_coursework;

public class weatherLocation {
    private String name;
    private boolean isChosen;

    public weatherLocation(String name, boolean isChosen) {
        this.name = name;
        this.isChosen = isChosen;
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
