package com.example.app_coursework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AddLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        setTitle("Select a location");

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

        if (id == R.id.create_location) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), CreateLocationActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}