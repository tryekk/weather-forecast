package com.example.app_coursework;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class CurrentWeatherFragment extends Fragment {

    ListView listView;
    ArrayList<String> weatherList = new ArrayList<>();
    private static CurrentWeatherFragment instance = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_current, container, false);
        instance = this;

        listView = (ListView) v.findViewById(R.id.current_weather);

        return v;
    }

    public static CurrentWeatherFragment getInstance() {
        return instance;
    }
}