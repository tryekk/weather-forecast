package com.example.app_coursework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add toolbar to the appbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get a fragment manager
        // Start a fragment transaction
        // Replace the container with MyFragment
        // (Can be done on one line)

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
        return true;
//        return super.onCreateOptionsMenu(menu);
    }

    // Options menu items
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.first_item) {
            Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
            return true;
        }
//        else if (id == R.id.second_item) {
//            Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
}