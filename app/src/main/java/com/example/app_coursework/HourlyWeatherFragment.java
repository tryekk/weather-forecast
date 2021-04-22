package com.example.app_coursework;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app_coursework.adapter.HourlyWeatherAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HourlyWeatherFragment extends Fragment {

    ListView listView;
    ArrayList<String> weatherList = new ArrayList<>();
    private static HourlyWeatherFragment instance = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hourly, container, false);
        instance = this;

        listView = (ListView) v.findViewById(R.id.hourly_weather_list);

        return v;
    }

    public static HourlyWeatherFragment getInstance() {
        return instance;
    }

    protected void parseJSON(JSONObject response) {
        weatherList.clear();
        try {
            JSONArray arr = response.getJSONObject("data").getJSONArray("timelines").getJSONObject(0).getJSONArray("intervals");
            for (int i = 0; i < arr.length(); i++) {
                String[] dateFormatted = arr.getJSONObject(i).getString("startTime").split("T|:0");  // Get time from UTC timestamp string
                weatherList.add(dateFormatted[1] + "," +
                        String.valueOf(arr.getJSONObject(i).getJSONObject("values").getDouble("temperature")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HourlyWeatherAdapter adapter = new HourlyWeatherAdapter (getActivity(), weatherList);
        listView.setAdapter(adapter);

    }

}
