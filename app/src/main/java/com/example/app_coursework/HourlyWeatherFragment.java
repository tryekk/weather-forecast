package com.example.app_coursework;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HourlyWeatherFragment extends Fragment {

    View v;
    LinearLayout rootView;
    HorizontalScrollView scrollView;
    LayoutInflater inflaterElement;
    private static HourlyWeatherFragment instance = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_hourly, container, false);
        instance = this;

        scrollView = v.findViewById(R.id.hourly_horizontal);

        rootView = new LinearLayout(getContext());
        rootView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rootView.setOrientation(LinearLayout.HORIZONTAL);

        scrollView.addView(rootView);

        // For each element
        inflaterElement = LayoutInflater.from(getContext());

        return v;
    }

    public static HourlyWeatherFragment getInstance() {
        return instance;
    }

    protected void parseJSON(JSONObject response) {
        rootView.removeAllViews();
        try {
            // Read JSON
            JSONArray arr = response.getJSONObject("data").getJSONArray("timelines").getJSONObject(1).getJSONArray("intervals");
            for (int i = 0; i < arr.length(); i++) {
                String[] dateFormatted = arr.getJSONObject(i).getString("startTime").split("T|:00\\+");  // Get time from UTC timestamp string

                // Sunrise and sunset
                String[] sunrise = arr.getJSONObject(i).getJSONObject("values").getString("sunriseTime").split("T|:00\\+");
                String[] sunset = arr.getJSONObject(i).getJSONObject("values").getString("sunsetTime").split("T|:00\\+");

                String weatherConditions = (dateFormatted[1] + "," +
                        String.valueOf((int) arr.getJSONObject(i).getJSONObject("values").getDouble("temperature")) + "°" + "," +
                        String.valueOf(arr.getJSONObject(i).getJSONObject("values").getDouble("weatherCode")) + "," +
                        String.valueOf((int) arr.getJSONObject(i).getJSONObject("values").getDouble("precipitationProbability")) + "%" + "," +
                        sunrise[1] + "," + sunset[1] + "," +
                        String.valueOf((int) arr.getJSONObject(i).getJSONObject("values").getDouble("temperatureApparent")) + "°C");

                setContents(weatherConditions);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setContents(String weatherConditions) {
        View forecast_element = inflaterElement.inflate(R.layout.hourly_forecast_element, rootView, false);

        String[] weatherData = weatherConditions.split(",");

        TextView time = forecast_element.findViewById(R.id.time);
        ImageView weatherIcon = forecast_element.findViewById(R.id.weather_icon);
        TextView precipitation = forecast_element.findViewById(R.id.hourly_rain);
        TextView temperature = forecast_element.findViewById(R.id.hourly_temperature);

        // Time
        time.setText(weatherData[0]);

        // Rain
        precipitation.setText(weatherData[3]);
        if (Integer.parseInt(weatherData[3].split("%")[0]) > 0) {
            precipitation.setTextColor(Color.parseColor("#0fe5f5"));
        }

        // Temperature
        temperature.setText(weatherData[1]);

        // Set temperature display colour based on heat
        Integer temperatureValue = Integer.parseInt(weatherData[1].split("°")[0]);

        if (temperatureValue > 15 && temperatureValue < 20) {
            temperature.setTextColor(Color.parseColor("#f5d63d"));
        } else if (temperatureValue >= 20 && temperatureValue < 25) {
            temperature.setTextColor(Color.parseColor("#f5a142"));
        } else if (temperatureValue >= 25) {
            temperature.setTextColor(Color.parseColor("#f55142"));
        } else if (temperatureValue < 10) {
            temperature.setTextColor(Color.parseColor("#3dd6f5"));
        }

        // Weather conditions
        String[] timeSplit = weatherData[0].split(":");  // For night-time definition
        int sunrise = Integer.parseInt(weatherData[4].split(":")[0]) + 1;
        int sunset = Integer.parseInt(weatherData[5].split(":")[0]);

        switch (weatherData[2]) {
            case "1000.0":
                if (Integer.parseInt(timeSplit[0]) > sunset || Integer.parseInt(timeSplit[0]) < sunrise) {
                    weatherIcon.setImageResource(R.drawable.ic_clear_night);
                } else {
                    weatherIcon.setImageResource(R.drawable.ic_clear_day);
                }
                break;
            case "1001.0":
                weatherIcon.setImageResource(R.drawable.ic_cloudy);
                break;
            case "1100.0":
                if (Integer.parseInt(timeSplit[0]) > sunset || Integer.parseInt(timeSplit[0]) < sunrise) {
                    weatherIcon.setImageResource(R.drawable.ic_mostly_clear_night);
                } else {
                    weatherIcon.setImageResource(R.drawable.ic_mostly_clear_day);
                }
                break;
            case "1101.0":
                if (Integer.parseInt(timeSplit[0]) > sunset || Integer.parseInt(timeSplit[0]) < sunrise) {
                    weatherIcon.setImageResource(R.drawable.ic_partly_cloudy_night);
                } else {
                    weatherIcon.setImageResource(R.drawable.ic_partly_cloudy_day);
                }
                break;
            case "1102.0":
                weatherIcon.setImageResource(R.drawable.ic_mostly_cloudy);
                break;
            case "4000.0":
                weatherIcon.setImageResource(R.drawable.ic_drizzle);
                break;
            case "4001.0":
                weatherIcon.setImageResource(R.drawable.ic_rain);
                break;
            case "4200.0":
                weatherIcon.setImageResource(R.drawable.ic_rain_light);
                break;
            case "4201.0":
                weatherIcon.setImageResource(R.drawable.ic_rain_heavy);
                break;
            case "5000.0":
                weatherIcon.setImageResource(R.drawable.ic_snow);
                break;
        }

        rootView.addView(forecast_element);
    }

}
