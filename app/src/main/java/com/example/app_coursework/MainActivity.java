package com.example.app_coursework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.app.usage.UsageEvents.Event.NONE;

public class MainActivity extends AppCompatActivity {

//    private WeatherLocationDatabase weatherLocationDatabase;
    // Get database
    private WeatherLocationDatabase weatherLocationDatabase;
    private ExecutorService executorService;

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherLocationDatabase = WeatherLocationDatabase.getInstance(getApplicationContext());

        // Add toolbar to the appbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get a fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_hourly_container, new HourlyWeatherFragment())
                .commit();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_daily_container, new DailyWeatherFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        menu.clear();
        ArrayList<String> locationsList = new ArrayList<>();

        // Create background thread to get selected weather locations
        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<WeatherLocation> weatherLocationList = weatherLocationDatabase.weatherLocationDAO().getSelectedWeatherLocations();
                for (int i = 0; i<weatherLocationList.size(); i++) {
                    locationsList.add(weatherLocationList.get(i).getName());
                }
                // Update listView
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Update menu
                        for (int i = 0; i<locationsList.size(); i++) {
                            menu.add(NONE, i, NONE, locationsList.get(i));
                        }
                        menu.add(NONE, R.id.add_location, NONE, "Add Location")
                                .setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_baseline_add_24)).setShowAsAction(1);
                    }
                });
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    // Options menu items
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_location) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), AddLocation.class);
            startActivity(intent);
            return true;
        } else {
            Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}