package com.example.app_coursework;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app_coursework.adapter.HourlyWeatherAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExtraInformationFragment extends Fragment {

    ListView listView;
    ArrayList<String> weatherList = new ArrayList<>();
    private static ExtraInformationFragment instance = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_extra_information, container, false);
        instance = this;

        listView = (ListView) v.findViewById(R.id.extra_list);

        return v;
    }

    public static ExtraInformationFragment getInstance() {
        return instance;
    }

    protected void parseJSON(JSONObject response) {
        weatherList.clear();
        try {
            JSONArray arr = response.getJSONObject("data").getJSONArray("timelines").getJSONObject(0).getJSONArray("intervals");

            // Sunrise and sunset
            String[] sunrise = arr.getJSONObject(0).getJSONObject("values").getString("sunriseTime").split("T|:00\\+")[1].split(":");
            String[] sunset = arr.getJSONObject(0).getJSONObject("values").getString("sunsetTime").split("T|:00\\+")[1].split(":");

            weatherList.add(sunrise[0] + ":" + sunrise[1] + ", " + sunset[0] + ":" + sunset[1]);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        listView.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, weatherList));

    }
}
