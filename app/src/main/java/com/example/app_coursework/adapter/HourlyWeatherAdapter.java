package com.example.app_coursework.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app_coursework.R;

import java.util.List;

public class HourlyWeatherAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final List<String> items;

    public HourlyWeatherAdapter (Activity context, List<String> items) {
        super(context, R.layout.hourly_weather_layout, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.hourly_weather_layout, null, true);

        TextView time = (TextView) rowView.findViewById(R.id.hourly_time);
        TextView temperature = (TextView) rowView.findViewById(R.id.hourly_temperature);
        TextView temperatureApparent = (TextView) rowView.findViewById(R.id.hourly_temperature_apparent);
        ImageView weatherIcon = (ImageView) rowView.findViewById(R.id.weather_icon);
        TextView precipitation = (TextView) rowView.findViewById(R.id.hourly_precipitation);

        String[] display = items.get(position).split(",");
        time.setText(display[0]);

        temperature.setText(display[1]);
        temperatureApparent.setText(display[6]);

        // Set temperature display colour based on heat
        Integer temperatureValue = Integer.parseInt(display[1].split("°")[0]);

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
        String[] timeSplit = display[0].split(":");  // For night-time definition
        int sunrise = Integer.parseInt(display[4].split(":")[0]) + 1;
        int sunset = Integer.parseInt(display[5].split(":")[0]);

        switch (display[2]) {
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

        // Set precipitation text colour to match chance
//        Integer rainChanceInt = Integer.parseInt(display[3].split("%")[0]);

        precipitation.setTextColor(Color.parseColor("#0fe5f5"));

//        if (rainChanceInt >= 5 && rainChanceInt < 10) {
//            temperature.setTextColor(Color.parseColor("#f5d63d"));
//        } else if (rainChanceInt >= 10 && rainChanceInt < 25) {
//            temperature.setTextColor(Color.parseColor("#f5a142"));
//        } else if (rainChanceInt >= 25 && rainChanceInt < 50) {
//            temperature.setTextColor(Color.parseColor("#f55142"));
//        } else if (rainChanceInt >= 50 && rainChanceInt < 75) {
//            temperature.setTextColor(Color.parseColor("#3dd6f5"));
//        } else if (rainChanceInt >= 75 && rainChanceInt < 100) {
//            temperature.setTextColor(Color.parseColor("#3dd6f5"));
//        }

        if (Integer.parseInt(display[3].split("%")[0]) > 0) {
            precipitation.setText(display[3]);
        }

        return rowView;
    }

}
