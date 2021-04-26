package com.example.app_coursework;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app_coursework.adapter.LocationArrayAdapter;
import com.example.app_coursework.database.WeatherLocation;
import com.example.app_coursework.database.WeatherLocationDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CreateLocationFragment extends Fragment {

    private WeatherLocationDatabase weatherLocationDatabase = WeatherLocationDatabase.getInstance(getContext());;
    private ExecutorService executorService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_location, container, false);

        EditText input = (EditText) v.findViewById(R.id.create_location_text);
        EditText inputCoordinates = (EditText) v.findViewById(R.id.create_location_text_coordinates);
        ToggleButton selected = (ToggleButton) v.findViewById(R.id.toggleButton);

        Button submitButton = v.findViewById(R.id.button_save_location);
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // Create background thread
                executorService = Executors.newSingleThreadExecutor();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        // Add user input to database
                        weatherLocationDatabase.weatherLocationDAO().addWeatherLocation(
                                new WeatherLocation(input.getText().toString(), inputCoordinates.getText().toString(), selected.isChecked() ? 1:0));
                        // Back to add location screen
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

        return v;
    }


}
