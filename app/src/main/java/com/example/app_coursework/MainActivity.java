package com.example.app_coursework;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_coursework.database.WeatherLocation;
import com.example.app_coursework.database.WeatherLocationDatabase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.usage.UsageEvents.Event.NONE;

public class MainActivity extends AppCompatActivity {

    private WeatherLocationDatabase weatherLocationDatabase;
    private ExecutorService executorService;
    private FusedLocationProviderClient locationManager;
    private RequestQueue requestQueue;
    ArrayList<Double> currentPosition = new ArrayList<>();

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Current Location");

        // Initialize room database
        weatherLocationDatabase = WeatherLocationDatabase.getInstance(getApplicationContext());

        // Add toolbar to the appbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Get location
        requestPermission();

        locationManager = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        locationManager.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Log.d("COORDINATES", location.getLatitude() + ", " + location.getLongitude());
                    currentPosition.clear();
                    currentPosition.add(location.getLatitude());
                    currentPosition.add(location.getLongitude());
                    // Make and parse API request
                    getWeatherData(currentPosition);
                } else {
                    Log.d("COORDINATES", "Location is null");
                    currentPosition.clear();
                    currentPosition.add(new Double(51.4816));
                    currentPosition.add(new Double(-3.1791));
                    // Make and parse API request
                    getWeatherData(currentPosition);
                }
            }
        });

        // Get a fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_hourly_container, new HourlyWeatherFragment())
                .commit();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_daily_container, new DailyWeatherFragment())
                .commit();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    private void getWeatherData(ArrayList<Double> currentPosition) {
        //  API
        requestQueue = Volley.newRequestQueue(this);

        // Get time and date
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        // Convert Date to Calendar
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 10);

        String endTime = df.format(c.getTime());
        String apikey = "z6N3a8QW0Cwy80k9sTxvPNHCGqvRFq5f";
//        double[] location = {51.4816, -3.1791};
        String[] fields = {"temperature", "weatherCode", "precipitationProbability"};
        String units = "metric";
        String[] timesteps = {"1h", "1d"};
        String timezone = "Europe/London";

        String url = "https://api.tomorrow.io/v4/timelines" + "?apikey=" + apikey + "&location=" +
                currentPosition.get(0) + "," + currentPosition.get(1) + "&fields=" + fields[0]
                + "&fields=" + fields[1] + "&fields=" + fields[2] + "&units=" + units +
                "&timesteps=" + timesteps[0] + "&timesteps=" + timesteps[1] + "&endTime=" +
                endTime + "&timezone=" + timezone;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, //The type of request, e.g., GET, POST, DELETE, PUT, etc.
                url, //as defined above
                null, //data to send with the request, none in this case
                new Response.Listener<JSONObject>() { //onsuccess
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(JSONObject response) {
                        //access to methods on a JSONObject requires JSONException errors to be
                        //handled in some way - such as surrounding the code in a try-catch block
                        try {
                            Log.d("RESPONSE", response.toString(2)); //prints the response to LogCat
                            // Process data
                            HourlyWeatherFragment hourlyWeatherFragment = HourlyWeatherFragment.getInstance();
                            hourlyWeatherFragment.parseJSON(response);
                            DailyWeatherFragment dailyWeatherFragment = DailyWeatherFragment.getInstance();
                            dailyWeatherFragment.parseJSON(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() { //onerror
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getMessage() != null) {
                            Log.d("ERROR", error.getMessage()); //prints the error message to LogCat
                        }
                            // Dummy data
                            ArrayList<String> array = new ArrayList<>();
                            array.add("hello,1");
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
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
            intent.setClass(getApplicationContext(), AddLocationActivity.class);
            startActivity(intent);
            return true;
        } else {
            // Create background thread
            executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    String coordinates = weatherLocationDatabase.weatherLocationDAO()
                            .getChosenWeatherLocation(String.valueOf(item.getTitle()))
                            .getCoordinates();

                    String[] formattedCoordinates = coordinates.split(",");
                    // Update weather with selected coordinates
                    currentPosition.clear();
                    currentPosition.add(Double.valueOf(formattedCoordinates[0]));
                    currentPosition.add(Double.valueOf(formattedCoordinates[1]));
                    getWeatherData(currentPosition);
                }
            });
            setTitle(item.getTitle());
            return true;
        }
    }
}