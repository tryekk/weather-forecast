package com.example.app_coursework;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddLocationFragment extends Fragment implements AdapterView.OnItemClickListener {

    private WeatherLocationDatabase weatherLocationDatabase;
    private ExecutorService executorService;
    ListView listView;
    ArrayList<WeatherLocation> locationsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_location, container, false);

        listView = (ListView) v.findViewById(R.id.weather_locations_list);

        // Build database
        RoomDatabase.Callback rdc = new RoomDatabase.Callback() {
            public void onCreate(SupportSQLiteDatabase db) {
                weatherLocationDatabase.weatherLocationDAO().addWeatherLocation(
                        new WeatherLocation("Cardiff", "51.4816,-3.1791", false),
                        new WeatherLocation("London", "51.5074,0.1278", false),
                        new WeatherLocation("New York", "40.7128,74.0060", false),
                        new WeatherLocation("San Francisco", "37.7749,122.4194", false)
                );
            }
        };

        weatherLocationDatabase = Room.databaseBuilder(
                getContext(),
                WeatherLocationDatabase.class,
                "WeatherDatabase"
        ).addCallback(rdc).build();

//        Room.databaseBuilder(getContext(), WeatherLocationDatabase.class, "WeatherDatabase")
//                .createFromAsset("database/weather_location_database.db")
//                .build();

        // Create background thread
        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<WeatherLocation> weatherLocationList = weatherLocationDatabase.weatherLocationDAO().getUnselectedWeatherLocations();
                for (int i = 0; i<weatherLocationList.size(); i++) {
                    locationsList.add(weatherLocationList.get(i));
                }

                // Custom array adapter
                LocationArrayAdapter adapter = new LocationArrayAdapter (getActivity(), locationsList);
                listView.setAdapter(adapter);
            }
        });

        listView.setOnItemClickListener(this);

        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        WeatherLocation weatherLocation = (WeatherLocation) parent.getItemAtPosition(position);
        String location = weatherLocation.getCoordinates();
        Toast.makeText(getActivity(), location, Toast.LENGTH_SHORT).show();

        // Create background thread
        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                weatherLocationDatabase.weatherLocationDAO().setWeatherLocationAsSelected(weatherLocation.getName(), true);
                System.out.println(weatherLocationDatabase.weatherLocationDAO().getUnselectedWeatherLocations());
            }
        });
    }
}
