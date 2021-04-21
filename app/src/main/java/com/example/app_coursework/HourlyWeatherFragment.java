package com.example.app_coursework;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_coursework.adapter.HourlyWeatherAdapter;
import com.example.app_coursework.adapter.LocationArrayAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class HourlyWeatherFragment extends Fragment {

    ListView listView;
    ArrayList<String> weatherList = new ArrayList<>();
    private static HourlyWeatherFragment instance = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hourly, container, false);
        instance = this;

        // Update listView
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                System.out.println(preferences.getString("response", null));

//                listView = (ListView) v.findViewById(R.id.weather_locations_list);
//                // Custom array adapter
//                LocationArrayAdapter adapter = new LocationArrayAdapter (getActivity(), locationsList);
//                listView.setAdapter(adapter);
//
//                listView.setOnItemClickListener(AddLocationFragment.this::onItemClick);
            }
        });

        return v;
    }

    public static HourlyWeatherFragment getInstance() {
        return instance;
    }

    protected void parseJSON(JSONObject response) {
        try {
            JSONArray arr = response.getJSONObject("data").getJSONArray("timelines").getJSONObject(0).getJSONArray("intervals");
            for (int i = 0; i < arr.length(); i++) {
                String[] dateFormatted = arr.getJSONObject(i).getString("startTime").split("T");
                weatherList.add(dateFormatted[1] + "," +
                        String.valueOf(arr.getJSONObject(i).getJSONObject("values").getDouble("temperature")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(weatherList);
    }

}
