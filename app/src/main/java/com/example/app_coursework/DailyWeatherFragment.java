package com.example.app_coursework;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_coursework.adapter.DailyWeatherAdapter;
import com.example.app_coursework.adapter.HourlyWeatherAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

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
        try {
            JSONArray arr = response.getJSONObject("data").getJSONArray("timelines").getJSONObject(1).getJSONArray("intervals");
            for (int i = 0; i < arr.length(); i++) {
                String[] dateFormatted = arr.getJSONObject(i).getString("startTime").split("T");
                weatherList.add(dateFormatted[0] + "," +
                        String.valueOf(arr.getJSONObject(i).getJSONObject("values").getDouble("temperature")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Update listView
        DailyWeatherAdapter adapter = new DailyWeatherAdapter (getActivity(), weatherList);
        listView.setAdapter(adapter);
    }

}
