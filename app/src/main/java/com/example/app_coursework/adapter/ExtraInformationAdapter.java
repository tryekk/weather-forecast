package com.example.app_coursework.adapter;

import android.app.Activity;
import android.graphics.Color;
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

        String[] display = items.get(position).split(",");

        TextView entry = (TextView) rowView.findViewById(R.id.entry_name);
        entry.setText(display[0]);
        TextView entry_information = (TextView) rowView.findViewById(R.id.entry_information);
        entry_information.setText(display[1]);

        if (display[0].equals("Grass Pollen") || display[0].equals("Tree Pollen")) {
            switch (display[1].split("/")[0]) {
                case "1":
                    entry_information.setTextColor(Color.parseColor("#60e04c"));
                case "2":
                    entry_information.setTextColor(Color.parseColor("#e0de4c"));
                case "3":
                    entry_information.setTextColor(Color.parseColor("#e0b14c"));
                case "4":
                    entry_information.setTextColor(Color.parseColor("#e0874c"));
                case "5":
                    entry_information.setTextColor(Color.parseColor("#e05d4c"));
            }
        }

        return rowView;
    }

}
