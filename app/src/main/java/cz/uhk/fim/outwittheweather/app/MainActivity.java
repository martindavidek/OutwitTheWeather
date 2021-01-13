package cz.uhk.fim.outwittheweather.app;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import cz.uhk.fim.outwittheweather.mock.CurrentWeatherDataMock;
import cz.uhk.fim.outwittheweather.model.CurrentWeather;
import cz.uhk.fim.outwittheweather.parser.WeatherJsonParser;
import cz.uhk.fim.outwittheweather.utils.DateTimeConverter;
import cz.uhk.fim.outwittheweather.volley.IVolleyCallback;
import cz.uhk.fim.outwittheweather.volley.VolleyClient;
import cz.uhk.fim.outwittheweather.R;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final String TAG = "MainActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 0xA1;

    private VolleyClient volleyClient = new VolleyClient();
    private WeatherJsonParser parser = new WeatherJsonParser();
    private LocationManager locationManager;

    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Location permission already granted");

            locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                Log.d(TAG, location.toString());
                this.onLocationChanged(location);
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            Button button1 = (Button) findViewById(R.id.main_button_7_days_gps);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,
                            SecondActivity.class);
                    intent.putExtra("Latitude", latitude);
                    intent.putExtra("Longitude", longitude);
                    startActivity(intent);
                }
            });

            Button button2 = (Button) findViewById(R.id.main_button_city_forecast);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,
                            ThirdActivity.class);
                    startActivity(intent);
                }
            });

            CurrentWeather currentWeather = CurrentWeatherDataMock.getCurrentWeather();

            showWeatherData(this, currentWeather);

            /*volleyClient.getWeatherObject(this, "CURRENT", latitude, longitude, new IVolleyCallback() {
                @Override
                public void onSuccess(Context context, JSONObject response) {
                    try {
                        CurrentWeather currentWeather = parser.parseCurrentWeather(response);
                        //CurrentWeather currentWeather = CurrentWeatherDataMock.getCurrentWeather();

                        showWeatherData(context, currentWeather);
                    } catch (JSONException e) {
                        Log.e(TAG, "Current weather parsing error: " + e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    Toast.makeText(getBaseContext(), "An error occurred. Please try again later.", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Volley error: " + error.getLocalizedMessage());
                }
            });*/
        } else {
            requestLocationPermission();
        }

    }

    private void showWeatherData(Context context, CurrentWeather currentWeather) {
        DateTimeConverter converter = new DateTimeConverter();

        TextView weatherView = (TextView) findViewById(R.id.main_weather);
        weatherView.setText(currentWeather.getWeather().getMain());

        TextView weatherDescriptionView = (TextView) findViewById(R.id.main_weather_desc);
        weatherDescriptionView.setText(currentWeather.getWeather().getDescription());

        TextView tempView = (TextView) findViewById(R.id.main_temp);
        tempView.setText(String.format(Locale.ENGLISH, "%.1f%s", currentWeather.getTemp(), getString(R.string.celsius_degree)));

        TextView feelsLikeView = (TextView) findViewById(R.id.main_feels_like);
        feelsLikeView.setText(String.format(Locale.ENGLISH, "%s%.1f%s", getString(R.string.feels_like), currentWeather.getFeelsLike(), getString(R.string.celsius_degree)));

        TextView dtView = (TextView) findViewById(R.id.main_dt_value);
        dtView.setText(converter.getTimeStamp(currentWeather.getDt()));

        TextView sunriseView = (TextView) findViewById(R.id.main_sunrise_value);
        sunriseView.setText(converter.getTimeStamp(currentWeather.getSunrise()));

        TextView sunsetView = (TextView) findViewById(R.id.main_sunset_value);
        sunsetView.setText(converter.getTimeStamp(currentWeather.getSunset()));

        TextView windSpeedView = (TextView) findViewById(R.id.main_wind_speed_value);
        windSpeedView.setText(String.format(Locale.ENGLISH, "%.1f%s", currentWeather.getWindSpeed(), getString(R.string.speed_units)));

        TextView pressureView = (TextView) findViewById(R.id.main_pressure_value);
        pressureView.setText(String.format(Locale.ENGLISH, "%d %s", currentWeather.getPressure(), getString(R.string.pressure_units)));

        TextView humidityView = (TextView) findViewById(R.id.main_humidity_value);
        humidityView.setText(String.format(Locale.ENGLISH, "%d %s", currentWeather.getHumidity(), getString(R.string.percent_sign)));

        String icon = currentWeather.getWeather().getIcon();

        if (icon != null) {
            ImageView imageView = (ImageView) findViewById(R.id.main_icon);
            Glide.with(context)
                    .load("https://openweathermap.org/img/wn/" + icon + "@2x.png")
                    .apply(new RequestOptions().override(400, 400))
                    .into(imageView);
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.forecast_loading);
            progressBar.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
        }
    }

    protected void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Location permission already granted");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
        } else {
            requestLocationPermission();
        }
    }

    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Location permission already granted");
            locationManager.removeUpdates(this);
        } else {
            requestLocationPermission();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this)
                    .setTitle("Location permission needed")
                    .setMessage("Location permission is needed in order to get weather data for your location.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission GRANTED", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "User granted the permission");
            } else {
                Toast.makeText(this, "Location permission DENIED", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "User denied the permission");
            }
        }
    }
}
