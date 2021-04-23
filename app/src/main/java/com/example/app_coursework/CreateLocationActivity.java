package com.example.app_coursework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class CreateLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_location);
        setTitle("Create a new location");

        // Add toolbar to the appbar
        Toolbar toolbar = findViewById(R.id.toolbar_create_location);
        setSupportActionBar(toolbar);

        // Load fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_create_location, new CreateLocationFragment())
                .commit();
    }
}