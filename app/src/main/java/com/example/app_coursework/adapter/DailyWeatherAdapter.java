package com.example.app_coursework.adapter;

import android.app.Activity;
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
        TextView temperature = (TextView) rowView.findViewById(R.id.daily_temperature);
        ImageView weatherIcon = (ImageView) rowView.findViewById(R.id.weather_icon);
        TextView precipitation = (TextView) rowView.findViewById(R.id.daily_precipitation);

        String[] display = items.get(position).split(",");
        time.setText(display[0]);
        temperature.setText(display[1]);
        switch (display[2]) {
            case "1000.0":
                weatherIcon.setBackgroundResource(R.drawable.sunny);
                break;
            case "1100.0":
                weatherIcon.setImageResource(R.drawable.ic_mostly_clear_day);
                break;
        }

        System.out.println(display[2]);

        precipitation.setText(display[3]);

        return rowView;
    }

}
