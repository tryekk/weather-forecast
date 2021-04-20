package com.example.app_coursework.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

        String[] display = items.get(position).split(",");
        time.setText(display[0]);
        temperature.setText(display[1]);


        return rowView;
    }

}