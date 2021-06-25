package com.example.app_coursework;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app_coursework.adapter.DailyWeatherAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class CurrentWeatherFragment extends Fragment {

    private static CurrentWeatherFragment instance = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_current, container, false);
        instance = this;

        TextView textViewDate = (TextView) v.findViewById(R.id.current_date);
        String timeStamp = new SimpleDateFormat("EEEE, MMM d'th'").format(Calendar.getInstance().getTime());
        textViewDate.setText(timeStamp);

        return v;
    }

    public static CurrentWeatherFragment getInstance() {
        return instance;
    }

    protected void parseJSON(JSONObject response) {
        ImageView currentWeatherIcon = (ImageView) getActivity().findViewById(R.id.current_weather_icon);


        try {
            JSONArray arr = response.getJSONObject("data").getJSONArray("timelines").getJSONObject(0).getJSONArray("intervals");
            String[] dateFormatted = arr.getJSONObject(0).getString("startTime").split("T|:00\\+");  // Get time from UTC timestamp string

            // Sunrise and sunset
            String[] sunrise = arr.getJSONObject(0).getJSONObject("values").getString("sunriseTime").split("T|:00\\+");
            String[] sunset = arr.getJSONObject(0).getJSONObject("values").getString("sunsetTime").split("T|:00\\+");

            Integer weatherCode = (int) arr.getJSONObject(0).getJSONObject("values").getDouble("weatherCode");

            String currentWeather = (
                    String.valueOf((int) arr.getJSONObject(0).getJSONObject("values").getDouble("temperature")) + "°C" + "," +
                    String.valueOf((int) arr.getJSONObject(0).getJSONObject("values").getDouble("precipitationProbability")) + "%" + "," +
//                    sunrise[1] + "," + sunset[1] + "," +
                    String.valueOf((int) arr.getJSONObject(0).getJSONObject("values").getDouble("temperatureApparent")) + "°C");

            TextView currentWeatherTextView = (TextView) getActivity().findViewById(R.id.current_weather);
            currentWeatherTextView.setText(currentWeather);

            // Update display
            switch (weatherCode) {
                case 1000:
//                weatherIcon.setBackgroundResource(R.drawable.sunny);
                    currentWeatherIcon.setImageResource(R.drawable.ic_clear_day);
                    break;
                case 1001:
                    currentWeatherIcon.setImageResource(R.drawable.ic_cloudy);
                    break;
                case 1100:
                    currentWeatherIcon.setImageResource(R.drawable.ic_mostly_clear_day);
                    break;
                case 1101:
                    currentWeatherIcon.setImageResource(R.drawable.ic_partly_cloudy_day);
                    break;
                case 1102:
                    currentWeatherIcon.setImageResource(R.drawable.ic_mostly_cloudy);
                    break;
                case 4000:
                    currentWeatherIcon.setImageResource(R.drawable.ic_drizzle);
                    break;
                case 4001:
                    currentWeatherIcon.setImageResource(R.drawable.ic_rain);
                    break;
                case 4200:
                    currentWeatherIcon.setImageResource(R.drawable.ic_rain_light);
                    break;
                case 4201:
                    currentWeatherIcon.setImageResource(R.drawable.ic_rain_heavy);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}