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

public class DailyWeatherAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final List<String> items;

    public DailyWeatherAdapter (Activity context, List<String> items) {
        super(context, R.layout.hourly_weather_layout, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.daily_weather_layout, null, true);

        TextView time = (TextView) rowView.findViewById(R.id.daily_time);
        TextView timeDate = (TextView) rowView.findViewById(R.id.daily_time_date);
        TextView temperature = (TextView) rowView.findViewById(R.id.daily_temperature);
        ImageView weatherIcon = (ImageView) rowView.findViewById(R.id.weather_icon);
        TextView precipitation = (TextView) rowView.findViewById(R.id.daily_precipitation);

        String[] display = items.get(position).split("#");
        time.setText(display[0]);
        timeDate.setText(display[1]);

        // Set temperature display colour based on heat
        Integer temperatureValue = Integer.parseInt(display[2].split("°")[0]);

        if (temperatureValue > 15 && temperatureValue < 20) {
            temperature.setTextColor(Color.parseColor("#f5d63d"));
        } else if (temperatureValue >= 20 && temperatureValue < 25) {
            temperature.setTextColor(Color.parseColor("#f5a142"));
        } else if (temperatureValue >= 25) {
            temperature.setTextColor(Color.parseColor("#f55142"));
        } else if (temperatureValue < 10) {
            temperature.setTextColor(Color.parseColor("#3dd6f5"));
        }

        temperature.setText(display[2]);

        switch (display[3]) {
            case "1000.0":
//                weatherIcon.setBackgroundResource(R.drawable.sunny);
                weatherIcon.setImageResource(R.drawable.ic_clear_day);
                break;
            case "1001.0":
                weatherIcon.setImageResource(R.drawable.ic_cloudy);
                break;
            case "1100.0":
                weatherIcon.setImageResource(R.drawable.ic_mostly_clear_day);
                break;
            case "1101.0":
                weatherIcon.setImageResource(R.drawable.ic_partly_cloudy_day);
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
        }

        precipitation.setText(display[4]);

        return rowView;
    }

}
