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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddLocationFragment extends Fragment implements AdapterView.OnItemClickListener {

    private WeatherLocationDatabase weatherLocationDatabase;
    private ExecutorService executorService;
    ListView listView;
    ArrayList<String> locationsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_location, container, false);

        listView = (ListView) v.findViewById(R.id.weather_locations_list);

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
                        new WeatherLocation("Cardiff", "51.4816,-3.1791", false),
                        new WeatherLocation("London", "51.5074, 0.1278", false)
                );
                List<WeatherLocation> weatherLocationList = weatherLocationDatabase.weatherLocationDAO().getUnselectedWeatherLocations();
                for (int i = 0; i<weatherLocationList.size(); i++) {
                    locationsList.add(weatherLocationList.get(i).getName());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(requireActivity(),
                        android.R.layout.simple_list_item_1,
                        locationsList);

                listView.setAdapter(arrayAdapter);
            }
        });

//        listView.setClickable(true);

        listView.setOnItemClickListener(this);

        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String day = parent.getItemAtPosition(position).toString();
        Toast.makeText(getActivity(), day, Toast.LENGTH_SHORT).show();
    }
}
