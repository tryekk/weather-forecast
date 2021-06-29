package com.example.app_coursework;

import android.graphics.Color;
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

        return v;
    }

    public static CurrentWeatherFragment getInstance() {
        return instance;
    }

    protected void parseJSON(JSONObject response) {
        ImageView currentWeatherIcon = (ImageView) getActivity().findViewById(R.id.current_weather_icon);

        try {
            // Use hourly forecast to retrieve sunrise and sunset
            JSONArray arr = response.getJSONObject("data").getJSONArray("timelines").getJSONObject(1).getJSONArray("intervals");

            String[] dateFormatted = arr.getJSONObject(0).getString("startTime").split("T|:00\\+");  // Get time from UTC timestamp string

            // Sunrise and sunset
            String[] sunrise = arr.getJSONObject(0).getJSONObject("values").getString("sunriseTime").split("T|:00\\+");
            String[] sunset = arr.getJSONObject(0).getJSONObject("values").getString("sunsetTime").split("T|:00\\+");

            // Switch to 1m forecast from hourly forecast
            JSONArray arr1m = response.getJSONObject("data").getJSONArray("timelines").getJSONObject(0).getJSONArray("intervals");

            String currentTemperature = String.valueOf((int) arr1m.getJSONObject(0).getJSONObject("values").getDouble("temperature")) + "°C";
            Integer weatherCode = (int) arr1m.getJSONObject(0).getJSONObject("values").getDouble("weatherCode");

            // Update display
            TextView textViewDate = (TextView) getActivity().findViewById(R.id.current_date);
            String timeStamp = new SimpleDateFormat("EEEE, d'th' MMM").format(Calendar.getInstance().getTime());
            textViewDate.setText(timeStamp);

            TextView currentTemperatureTextView = (TextView) getActivity().findViewById(R.id.current_temperature);
            currentTemperatureTextView.setText(currentTemperature);

            // Set temperature display colour based on heat
            Integer temperatureValue = Integer.parseInt(currentTemperature.split("°")[0]);
            if (temperatureValue > 15 && temperatureValue < 20) {
                currentTemperatureTextView.setTextColor(Color.parseColor("#f5d63d"));
            } else if (temperatureValue >= 20 && temperatureValue < 25) {
                currentTemperatureTextView.setTextColor(Color.parseColor("#f5a142"));
            } else if (temperatureValue >= 25) {
                currentTemperatureTextView.setTextColor(Color.parseColor("#f55142"));
            } else if (temperatureValue < 10) {
                currentTemperatureTextView.setTextColor(Color.parseColor("#3dd6f5"));
            } else {
                currentTemperatureTextView.setTextColor(Color.parseColor("#808080"));
            }

            // For night-time definition
            int timeSplit = Integer.parseInt(dateFormatted[1].split(":")[0]);
            int sunriseSplit = Integer.parseInt(sunrise[1].split(":")[0]);
            int sunsetSplit = Integer.parseInt(sunset[1].split(":")[0]);

            String weatherCondition = "null";

            switch (weatherCode) {
                case 1000:
                    weatherCondition = "Clear";
                    if (timeSplit > sunsetSplit || timeSplit < sunriseSplit) {
                        currentWeatherIcon.setImageResource(R.drawable.ic_clear_night);
                    } else {
                        currentWeatherIcon.setImageResource(R.drawable.ic_clear_day);
                    }
                    break;
                case 1001:
                    weatherCondition = "Cloudy";
                    currentWeatherIcon.setImageResource(R.drawable.ic_cloudy);
                    break;
                case 1100:
                    weatherCondition = "Mostly Clear";
                    if (timeSplit > sunsetSplit || timeSplit < sunriseSplit) {
                        currentWeatherIcon.setImageResource(R.drawable.ic_mostly_clear_night);
                    } else {
                        currentWeatherIcon.setImageResource(R.drawable.ic_mostly_clear_day);
                    }
                    break;
                case 1101:
                    weatherCondition = "Partly Cloudy";
                    if (timeSplit > sunsetSplit || timeSplit < sunriseSplit) {
                        currentWeatherIcon.setImageResource(R.drawable.ic_partly_cloudy_night);
                    } else {
                        currentWeatherIcon.setImageResource(R.drawable.ic_partly_cloudy_day);
                    }
                    break;
                case 1102:
                    weatherCondition = "Mostly Cloudy";
                    currentWeatherIcon.setImageResource(R.drawable.ic_mostly_cloudy);
                    break;
                case 4000:
                    weatherCondition = "Drizzle";
                    currentWeatherIcon.setImageResource(R.drawable.ic_drizzle);
                    break;
                case 4001:
                    weatherCondition = "Rain";
                    currentWeatherIcon.setImageResource(R.drawable.ic_rain);
                    break;
                case 4200:
                    weatherCondition = "Light Rain";
                    currentWeatherIcon.setImageResource(R.drawable.ic_rain_light);
                    break;
                case 4201:
                    weatherCondition = "Heavy Rain";
                    currentWeatherIcon.setImageResource(R.drawable.ic_rain_heavy);
                    break;
            }

            // Update weather conditions display
            String currentWeather = (
                weatherCondition +
                "\n" + "Feels like " + String.valueOf((int) arr1m.getJSONObject(0).getJSONObject("values").getDouble("temperatureApparent")) + "°C" +
                "\n" + String.valueOf((int) arr1m.getJSONObject(0).getJSONObject("values").getDouble("precipitationProbability")) + "%" + " chance of rain");

            TextView currentWeatherTextView = (TextView) getActivity().findViewById(R.id.current_weather);
            currentWeatherTextView.setText(currentWeather);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}