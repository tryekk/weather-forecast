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

        TextView textViewDate = (TextView) v.findViewById(R.id.date);
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
            JSONArray arr = response.getJSONObject("data").getJSONArray("timelines").getJSONObject(1).getJSONArray("intervals");

            // Format date
            String[] dateFormatted = arr.getJSONObject(0).getString("startTime").split("T");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date dt1 = df.parse(dateFormatted[0]);
            DateFormat format2 = new SimpleDateFormat("EE");
            String finalDay = format2.format(dt1);
            DateFormat formatDayNumber = new SimpleDateFormat("dd");
            String finalDayNumber = formatDayNumber.format(dt1);

            // Weather code
            String weatherCode = String.valueOf(arr.getJSONObject(0).getJSONObject("values").getDouble("weatherCode"));

            String currentWeatherString = (finalDay + "#" + finalDayNumber + "#" +
                    String.valueOf((int) arr.getJSONObject(0).getJSONObject("values").getDouble("temperature")) + "°C" + "#" +
                    String.valueOf(arr.getJSONObject(0).getJSONObject("values").getDouble("weatherCode")) + "#" +
                    String.valueOf((int) arr.getJSONObject(0).getJSONObject("values").getDouble("precipitationProbability")) + "%");

            // Update display
            switch (weatherCode) {
                case "1000.0":
//                weatherIcon.setBackgroundResource(R.drawable.sunny);
                    currentWeatherIcon.setImageResource(R.drawable.ic_clear_day);
                    break;
                case "1001.0":
                    currentWeatherIcon.setImageResource(R.drawable.ic_cloudy);
                    break;
                case "1100.0":
                    currentWeatherIcon.setImageResource(R.drawable.ic_mostly_clear_day);
                    break;
                case "1101.0":
                    currentWeatherIcon.setImageResource(R.drawable.ic_partly_cloudy_day);
                    break;
                case "1102.0":
                    currentWeatherIcon.setImageResource(R.drawable.ic_mostly_cloudy);
                    break;
                case "4000.0":
                    currentWeatherIcon.setImageResource(R.drawable.ic_drizzle);
                    break;
                case "4001.0":
                    currentWeatherIcon.setImageResource(R.drawable.ic_rain);
                    break;
                case "4200.0":
                    currentWeatherIcon.setImageResource(R.drawable.ic_rain_light);
                    break;
                case "4201.0":
                    currentWeatherIcon.setImageResource(R.drawable.ic_rain_heavy);
                    break;
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }
}