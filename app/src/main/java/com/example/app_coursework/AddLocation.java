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

import com.example.app_coursework.database.WeatherLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import static android.app.usage.UsageEvents.Event.NONE;

public class AddLocation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        setTitle("Add a location");

        // Add toolbar to the appbar
        Toolbar toolbar = findViewById(R.id.toolbar_add_location);
        setSupportActionBar(toolbar);

        // Load fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_add_location, new AddLocationFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_add_location, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_location) {
            // TODO Direct to create location activity
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), AddLocation.class);
            startActivity(intent);
            return true;
        } else {

            return true;
        }
    }
}