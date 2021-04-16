package com.example.app_coursework;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddLocationFragment extends Fragment {

    private WeatherLocationDatabase weatherLocationDatabase;
    private ExecutorService executorService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_location, container, false);

        // Build database
        weatherLocationDatabase = Room.databaseBuilder(
                getContext(),
                WeatherLocationDatabase.class,
                "WeatherDatabase"
        ).build();

        // Create background thread
        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                weatherLocationDatabase.weatherLocationDAO().deleteAllWeatherLocations();
                weatherLocationDatabase.weatherLocationDAO().addWeatherLocation(
                        new WeatherLocation("Cardiff", "51.4816,-3.1791", false)
                );
                List<WeatherLocation> weatherLocationList = weatherLocationDatabase.weatherLocationDAO().getUnselectedWeatherLocations();
                System.out.println(weatherLocationList.get(0).getName());
            }
        });

        return v;
    }
}
