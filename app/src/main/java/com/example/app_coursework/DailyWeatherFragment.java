package com.example.app_coursework;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app_coursework.adapter.DailyWeatherAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class DailyWeatherFragment extends Fragment implements AdapterView.OnItemClickListener {

    ListView listView;
    ArrayList<String> weatherList = new ArrayList<>();
    private static DailyWeatherFragment instance = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_daily, container, false);
        instance = this;

        listView = (ListView) v.findViewById(R.id.daily_weather_list);

        return v;
    }

    public static DailyWeatherFragment getInstance() {
        return instance;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String day = parent.getItemAtPosition(position).toString();
        Toast.makeText(getActivity(), day, Toast.LENGTH_SHORT).show();
    }

    protected void parseJSON(JSONObject response) {
        weatherList.clear();
        try {
            JSONArray arr = response.getJSONObject("data").getJSONArray("timelines").getJSONObject(1).getJSONArray("intervals");
            for (int i = 0; i < arr.length(); i++) {
                String[] dateFormatted = arr.getJSONObject(i).getString("startTime").split("T");
                weatherList.add(dateFormatted[0] + "," +
                        String.valueOf(arr.getJSONObject(i).getJSONObject("values").getDouble("temperature")) + "    " +
                        String.valueOf(arr.getJSONObject(i).getJSONObject("values").getDouble("weatherCode")));  // TODO requires translation
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Update listView
        DailyWeatherAdapter adapter = new DailyWeatherAdapter (getActivity(), weatherList);
        listView.setAdapter(adapter);
    }

}
