package com.example.app_coursework;

import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class HourlyWeatherFragment extends Fragment implements AdapterView.OnItemClickListener {

    private RequestQueue requestQueue;
    ListView listView;
    ArrayList<String> weatherList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hourly, container, false);

//        API
        requestQueue = Volley.newRequestQueue(getActivity());

        // Get time and date
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        // Convert Date to Calendar
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.HOUR, 24);

        String endTime = df.format(c.getTime());
        String apikey = "z6N3a8QW0Cwy80k9sTxvPNHCGqvRFq5f";
        double[] location = {51.4816, -3.1791};
        String[] fields = {"temperature"};
        String units = "metric";
        String[] timesteps = {"1h"};
        String timezone = "Europe/London";

        String url = "https://api.tomorrow.io/v4/timelines" + "?apikey=" + apikey + "&location=" + location[0] + "," + location[1] +
                "&fields=" + fields[0] + "&units=" + units + "&timesteps=" + timesteps[0] + "&endTime=" + endTime + "&timezone=" + timezone;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, //The type of request, e.g., GET, POST, DELETE, PUT, etc.
                url, //as defined above
                null, //data to send with the request, none in this case
                new Response.Listener<JSONObject>() { //onsuccess
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(JSONObject response) {
                        //access to methods on a JSONObject requires JSONException errors to be
                        //handled in some way - such as surrounding the code in a try-catch block
                        try {
                            Log.d("RESPONSE", response.toString(2)); //prints the response to LogCat
                            parseJSON(response);
                            //       List
                            listView = (ListView) v.findViewById(R.id.hourly_weather_list);

                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_list_item_1,
                                    weatherList);

                            listView.setAdapter(arrayAdapter);
//                        listView.setOnItemClickListener(this);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() { //onerror
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getMessage() != null)
                            Log.d("ERROR", error.getMessage()); //prints the error message to LogCat
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);

        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String day = parent.getItemAtPosition(position).toString();
        Toast.makeText(getActivity(), day, Toast.LENGTH_SHORT).show();
    }

    private void parseJSON(JSONObject response) {
        try {
            JSONArray arr = response.getJSONObject("data").getJSONArray("timelines").getJSONObject(0).getJSONArray("intervals");
            for (int i = 0; i < arr.length(); i++) {
                weatherList.add(String.valueOf(arr.getJSONObject(i).getString("startTime")) + "\n" +
                        String.valueOf(arr.getJSONObject(i).getJSONObject("values").getDouble("temperature")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
