package com.example.app_coursework;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app_coursework.adapter.ExtraInformationAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExtraInformationFragment extends Fragment {

    ListView listView;
    ArrayList<String> weatherList = new ArrayList<>();
    private static ExtraInformationFragment instance = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_extra_information, container, false);
        instance = this;

        listView = (ListView) v.findViewById(R.id.extra_list);

        return v;
    }

    public static ExtraInformationFragment getInstance() {
        return instance;
    }

    protected void parseJSON(JSONObject response) {
        weatherList.clear();
        try {
            JSONArray arr = response.getJSONObject("data").getJSONArray("timelines").getJSONObject(1).getJSONArray("intervals");

            String[] sunrise = arr.getJSONObject(0).getJSONObject("values").getString("sunriseTime").split("T|:00\\+")[1].split(":");
            String[] sunset = arr.getJSONObject(0).getJSONObject("values").getString("sunsetTime").split("T|:00\\+")[1].split(":");

            Integer humidity = (int) arr.getJSONObject(0).getJSONObject("values").getDouble("humidity");

            Integer windSpeed = (int) arr.getJSONObject(0).getJSONObject("values").getDouble("windSpeed");
            Integer windDirection = (int) arr.getJSONObject(0).getJSONObject("values").getDouble("windDirection");

            Integer grassIndex = (int) arr.getJSONObject(0).getJSONObject("values").getDouble("grassIndex");
            Integer treeIndex = (int) arr.getJSONObject(0).getJSONObject("values").getDouble("treeIndex");

            weatherList.add("Sunrise" + "," + sunrise[0] + ":" + sunrise[1]);
            weatherList.add("Sunset" + "," + sunset[0] + ":" + sunset[1]);
            weatherList.add("Humidity" + "," + humidity + "%");
            weatherList.add("Wind Speed" + "," + windSpeed + " mph");
            weatherList.add("Wind Direction" + "," + windDirection + "??");
            weatherList.add("Grass Pollen" + "," + grassIndex + "/5");
            weatherList.add("Tree Pollen" + "," + treeIndex + "/5");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        listView.setAdapter(new ExtraInformationAdapter(getActivity(), weatherList));

    }
}
