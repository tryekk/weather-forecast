package com.example.app_coursework.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.app_coursework.R;
import com.example.app_coursework.database.WeatherLocation;

import java.util.List;

public class ExtraInformationAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final List<String> items;

    public ExtraInformationAdapter (Activity context, List<String> items) {
        super(context, R.layout.location_list_layout, items);
        this.context = context;
        this.items = items;

    }

    // Modified code from https://stackoverflow.com/questions/25501460/android-custom-arrayadapter
    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.extra_information_layout, null, true);

        TextView entry = (TextView) rowView.findViewById(R.id.entry);
        entry.setText(items.get(position));

        return rowView;
    }

}
