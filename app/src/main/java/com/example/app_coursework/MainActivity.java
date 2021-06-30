package com.example.app_coursework;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.app_coursework.database.WeatherLocation;
import com.example.app_coursework.database.WeatherLocationDatabase;

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
import static android.Manifest.permission.SYSTEM_ALERT_WINDOW;
import static android.app.usage.UsageEvents.Event.NONE;

public class MainActivity extends AppCompatActivity {

    private WeatherLocationDatabase weatherLocationDatabase;
    private ExecutorService executorService;
    private RequestQueue requestQueue;
    private LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Current Location");

        // Make fullscreen
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        // Initialize room database
        weatherLocationDatabase = WeatherLocationDatabase.getInstance(getApplicationContext());

        // Add toolbar to the appbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Get location
        requestPermission();

        // Get a fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_current_container, new CurrentWeatherFragment())
                .commit();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_hourly_container, new HourlyWeatherFragment())
                .commit();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_daily_container, new DailyWeatherFragment())
                .commit();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_extra_container, new ExtraInformationFragment())
                .commit();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        getLastKnownLocationAndWeather();
    }

    // Method is modified code from - user:TharakaNirmana @ https://stackoverflow.com/questions/20438627/getlastknownlocation-returns-null
    private void getLastKnownLocationAndWeather() {
        // Inform user the information is being retrieved
//        TextView textViewCurrent = (TextView) this.findViewById(R.id.current_date);
        ListView listViewHourly = (ListView) this.findViewById(R.id.hourly_weather_list);
        ListView listViewDaily = (ListView) this.findViewById(R.id.daily_weather_list);
        ListView listViewExtra = (ListView) this.findViewById(R.id.extra_list);
        ArrayList<String> statusList = new ArrayList<>();
        statusList.add("Retrieving weather information...");
        listViewHourly.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, statusList));
        listViewDaily.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, statusList));
        listViewExtra.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, statusList));

        // Get location
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermission();
            } else {
                Location l = locationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
                // Get weather using API
                ArrayList<Double> currentPosition = new ArrayList<>();
                currentPosition.add(bestLocation.getLatitude());
                currentPosition.add(bestLocation.getLongitude());
                getWeatherData(currentPosition);
            }
        }
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
        String[] fields = {"temperature", "weatherCode", "precipitationProbability", "sunriseTime",
                "sunsetTime", "humidity", "windSpeed", "windDirection", "temperatureApparent",
                "grassIndex", "treeIndex"};
        String units = "metric";
        String[] timesteps = {"1m", "1h", "1d"};
        String timezone = "Europe/London";

        String url = "https://api.tomorrow.io/v4/timelines" + "?apikey=" + apikey + "&location=" +
                currentPosition.get(0) + "," + currentPosition.get(1) + "&fields=" + fields[0]
                + "&fields=" + fields[1] + "&fields=" + fields[2] + "&fields=" + fields[3] +
                "&fields=" + fields[4] + "&fields=" + fields[5] + "&fields=" + fields[6] +
                "&fields=" + fields[7] + "&fields=" + fields[8] + "&fields=" + fields[9] +
                "&fields=" + fields[10] + "&units=" + units + "&timesteps=" + timesteps[0] +
                "&timesteps=" + timesteps[1] + "&timesteps=" + timesteps[2] +
                "&endTime=" + endTime + "&timezone=" + timezone;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.GET, //The type of request, e.g., GET, POST, DELETE, PUT, etc.
            url, //as defined above
            null, //data to send with the request, none in this case
            new Response.Listener<JSONObject>() { //onsuccess
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(JSONObject response) {
//                            Log.d("RESPONSE", response.toString(2)); //prints the response to LogCat
                        // Process data
                        CurrentWeatherFragment currentWeatherFragment = CurrentWeatherFragment.getInstance();
                        currentWeatherFragment.parseJSON(response);
                        HourlyWeatherFragment hourlyWeatherFragment = HourlyWeatherFragment.getInstance();
                        hourlyWeatherFragment.parseJSON(response);
                        DailyWeatherFragment dailyWeatherFragment = DailyWeatherFragment.getInstance();
                        dailyWeatherFragment.parseJSON(response);
                        ExtraInformationFragment extraInformationFragment = ExtraInformationFragment.getInstance();
                        extraInformationFragment.parseJSON(response);
                }
            },
            new Response.ErrorListener() { //on error
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.getMessage() != null) {
                        Log.d("ERROR", error.getMessage()); //prints the error message to LogCat
                        // Display error to user
                        ListView listView = (ListView) MainActivity.this.findViewById(R.id.hourly_weather_list);
                        ArrayList<String> errorList = new ArrayList<>();
                        errorList.add("Error retrieving weather information");
                        ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, errorList);
                        listView.setAdapter(adapter);
                    }
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
                            menu.add(NONE, i+1, NONE, locationsList.get(i));
                        }
                        menu.add(NONE, R.id.add_location, NONE, "Add Location")
                                .setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_baseline_add_24)).setShowAsAction(1);
                        menu.add(NONE, 0, NONE, "Current Location")
                                .setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_baseline_location_on_24)).setShowAsAction(1);
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
                    if (item.getTitle().toString().equals("Current Location")) {
//                        getCurrentLocationAndGetWeather();
                        getLastKnownLocationAndWeather();
                    } else {
                        String coordinates = weatherLocationDatabase.weatherLocationDAO()
                                .getChosenWeatherLocation(String.valueOf(item.getTitle()))
                                .getCoordinates();

                        String[] formattedCoordinates = coordinates.split(",");
                        // Update weather with selected coordinates
                        ArrayList<Double> currentPosition = new ArrayList<>();
                        currentPosition.add(Double.valueOf(formattedCoordinates[0]));
                        currentPosition.add(Double.valueOf(formattedCoordinates[1]));
                        getWeatherData(currentPosition);
                    }
                }
            });
            setTitle(item.getTitle());
            return true;
        }
    }
}