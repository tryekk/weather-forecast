package com.example.app_coursework;

import android.content.Context;
import android.content.Intent;
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

        // Get database
        weatherLocationDatabase = WeatherLocationDatabase.getInstance(getContext());

        // Create background thread
        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // Get weather locations from DB
                List<WeatherLocation> weatherLocationList = weatherLocationDatabase.weatherLocationDAO().getUnselectedWeatherLocations();
                for (int i = 0; i<weatherLocationList.size(); i++) {
                    locationsList.add(weatherLocationList.get(i));
                }
                // Update listView
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView = (ListView) v.findViewById(R.id.weather_locations_list);
                        // Custom array adapter
                        LocationArrayAdapter adapter = new LocationArrayAdapter (getActivity(), locationsList);
                        listView.setAdapter(adapter);

                        listView.setOnItemClickListener(AddLocationFragment.this::onItemClick);
                    }
                });
            }
        });

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
                // Back to main activity
                Intent intent = new Intent();
                intent.setClass(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
