package com.example.app_coursework;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class LocationArrayAdapter extends ArrayAdapter<WeatherLocation> {
    private final Activity context;
    private final List<WeatherLocation> items;

    public LocationArrayAdapter (Activity context, List<WeatherLocation> items) {
        super(context, R.layout.location_list_layout, items);
        this.context = context;
        this.items = items;

    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.location_list_layout, null, true);

        TextView locationName = (TextView) rowView.findViewById(R.id.location_name);
//        TextView locationCoordinates = (TextView) rowView.findViewById(R.id.location_coordinates);


        locationName.setText(items.get(position).getName());
//        locationCoordinates.setText(items.get(position).getCoordinates());

        return rowView;
    }

}
