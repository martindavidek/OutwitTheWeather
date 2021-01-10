package cz.uhk.fim.outwittheweather.app;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import cz.uhk.fim.outwittheweather.model.CurrentWeather;
import cz.uhk.fim.outwittheweather.parser.WeatherJsonParser;
import cz.uhk.fim.outwittheweather.utils.DateTimeConverter;
import cz.uhk.fim.outwittheweather.volley.IVolleyCallback;
import cz.uhk.fim.outwittheweather.volley.VolleyClient;
import cz.uhk.fim.outwittheweather.R;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final String TAG = "MainActivity";

    private VolleyClient volleyClient = new VolleyClient();
    private WeatherJsonParser parser = new WeatherJsonParser();
    private LocationManager locationManager;

    private double latitude;
    private double longitude;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }

        if (checkSelfPermission(ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
            Toast.makeText(getBaseContext(), "Fine location access was not granted!", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Permission error: ACCESS_FINE_LOCATION permission was not granted!");
        }

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

        volleyClient.getWeatherObject(this, "CURRENT", latitude, longitude, new IVolleyCallback() {
            @Override
            public void onSuccess(Context context, JSONObject response) {
                try {
                    CurrentWeather currentWeather = parser.parseCurrentWeather(response);
                    DateTimeConverter converter = new DateTimeConverter();

                    String icon = currentWeather.getWeather().getIcon();

                    if (icon != null) {
                        ImageView imageView = (ImageView) findViewById(R.id.main_icon);
                        Glide.with(context)
                                .load("https://openweathermap.org/img/wn/" + icon + "@2x.png")
                                .apply(new RequestOptions().override(400, 400))
                                .into(imageView);
                    }

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

                } catch (JSONException e) {
                    Log.e(TAG, "Current weather parsing error: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(getBaseContext(), "An error occurred. Please try again later.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
    }

    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }


    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
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
}
