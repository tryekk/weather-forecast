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
                String[] dateFormatted = arr.getJSONObject(i).getString("startTime").split("T|:00\\+");  // Get time from UTC timestamp string

                // Sunrise and sunset
                String[] sunrise = arr.getJSONObject(i).getJSONObject("values").getString("sunriseTime").split("T|:00\\+");
                String[] sunset = arr.getJSONObject(i).getJSONObject("values").getString("sunsetTime").split("T|:00\\+");

                weatherList.add(dateFormatted[1] + "," +
                        String.valueOf((int) arr.getJSONObject(i).getJSONObject("values").getDouble("temperature")) + "°C" + "," +
                        String.valueOf(arr.getJSONObject(i).getJSONObject("values").getDouble("weatherCode")) + "," +
                        String.valueOf((int) arr.getJSONObject(i).getJSONObject("values").getDouble("precipitationProbability")) + "%" + "," +
                        sunrise[1] + "," + sunset[1]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HourlyWeatherAdapter adapter = new HourlyWeatherAdapter (getActivity(), weatherList);
        listView.setAdapter(adapter);

    }

}
