package com.example.app_coursework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.multidex.MultiDex;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

import java.util.ArrayList;

import static android.app.usage.UsageEvents.Event.NONE;

public class MainActivity extends AppCompatActivity {

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        return true;
//        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        ArrayList<String> array = new ArrayList<String>();
        array.add("23123");
        array.add("35443534");
        for (int i = 0; i<array.size(); i++) {
            menu.add(NONE, i, NONE, array.get(i));
        }
        menu.add(NONE, R.id.first_item, NONE, "Add Location")
                .setIcon(ContextCompat.getDrawable(this, R.drawable.ic_baseline_add_24)).setShowAsAction(1);
        return super.onPrepareOptionsMenu(menu);
    }

    // Options menu items
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.first_item) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), AddLocation.class);
            startActivity(intent);
            return true;
        }
//        else if (id == R.id.second_item) {
//            Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
}