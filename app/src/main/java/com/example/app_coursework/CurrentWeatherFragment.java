package com.example.app_coursework;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
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

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

            // Set temperature display and toolbar colour based on heat

            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
            Window window = getActivity().getWindow();
            View fragment = this.getView();

            String theme = "light";
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    theme = "dark";
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                    theme = "light";
                    break;
            }

            Integer temperatureValue = Integer.parseInt(currentTemperature.split("°")[0]);
            if (temperatureValue > 15 && temperatureValue < 20) {
                currentTemperatureTextView.setTextColor(Color.parseColor("#f5d63d"));
                if (theme.equals("light")) {
                    window.setStatusBarColor(Color.parseColor("#f5d63d"));
                    toolbar.setBackgroundColor(Color.parseColor("#f5d63d"));
                    fragment.setBackgroundResource(R.drawable.background_gradient_warm_mild);
                } else {
                    window.setStatusBarColor(Color.parseColor("#4DF5D63D"));
                    toolbar.setBackgroundColor(Color.parseColor("#4DF5D63D"));
                    fragment.setBackgroundResource(R.drawable.background_dark_gradient_warm_mild);
                }
            } else if (temperatureValue >= 20 && temperatureValue < 25) {
                currentTemperatureTextView.setTextColor(Color.parseColor("#f5a142"));
                if (theme.equals("light")) {
                    toolbar.setBackgroundColor(Color.parseColor("#f5a142"));
                    window.setStatusBarColor(Color.parseColor("#f5a142"));
                    fragment.setBackgroundResource(R.drawable.background_gradient_warm);
                } else {
                    toolbar.setBackgroundColor(Color.parseColor("#4DF5A142"));
                    window.setStatusBarColor(Color.parseColor("#4DF5A142"));
                    fragment.setBackgroundResource(R.drawable.background_dark_gradient_warm);
                }
            } else if (temperatureValue >= 25) {
                currentTemperatureTextView.setTextColor(Color.parseColor("#f55142"));
                if (theme.equals("light")) {
                    toolbar.setBackgroundColor(Color.parseColor("#f55142"));
                    window.setStatusBarColor(Color.parseColor("#f55142"));
                    fragment.setBackgroundResource(R.drawable.background_gradient_hot);
                } else {
                    toolbar.setBackgroundColor(Color.parseColor("#4DF55142"));
                    window.setStatusBarColor(Color.parseColor("#4DF55142"));
                    fragment.setBackgroundResource(R.drawable.background_dark_gradient_hot);
                }
            } else if (temperatureValue < 10) {
                currentTemperatureTextView.setTextColor(Color.parseColor("#3dd6f5"));
                if (theme.equals("light")) {
                    toolbar.setBackgroundColor(Color.parseColor("#3dd6f5"));
                    window.setStatusBarColor(Color.parseColor("#3dd6f5"));
                    fragment.setBackgroundResource(R.drawable.background_gradient_cold);
                } else {
                    toolbar.setBackgroundColor(Color.parseColor("#4D3DD6F5"));
                    window.setStatusBarColor(Color.parseColor("#4D3DD6F5"));
                    fragment.setBackgroundResource(R.drawable.background_dark_gradient_cold);
                }
            } else {
                if (theme.equals("light")) {
                    currentTemperatureTextView.setTextColor(Color.parseColor("#808080"));
                    window.setStatusBarColor(Color.parseColor("#FFFFFF"));
                    toolbar.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    fragment.setBackgroundColor(Color.parseColor("#FFFFFF"));
                } else {
                    currentTemperatureTextView.setTextColor(Color.parseColor("#b7bcbf"));
                    window.setStatusBarColor(Color.parseColor("#151F28"));
                    toolbar.setBackgroundResource(R.color.weather_list_dark);
                    fragment.setBackgroundResource(R.color.weather_list_dark);
                }
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
                "\n" + "Feels like " + String.valueOf((int) arr1m.getJSONObject(0).getJSONObject("values").getDouble("temperatureApparent")) + "°" +
                "\n" + String.valueOf((int) arr1m.getJSONObject(0).getJSONObject("values").getDouble("precipitationProbability")) + "%" + " chance of rain");

            TextView currentWeatherTextView = (TextView) getActivity().findViewById(R.id.current_weather);
            currentWeatherTextView.setText(currentWeather);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}