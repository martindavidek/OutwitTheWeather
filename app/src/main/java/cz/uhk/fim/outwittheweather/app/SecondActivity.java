package cz.uhk.fim.outwittheweather.app;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.uhk.fim.outwittheweather.adapter.DailyWeatherAdapter;
import cz.uhk.fim.outwittheweather.model.DailyWeather;
import cz.uhk.fim.outwittheweather.parser.WeatherJsonParser;
import cz.uhk.fim.outwittheweather.volley.IVolleyCallback;
import cz.uhk.fim.outwittheweather.volley.VolleyClient;
import cz.uhk.fim.outwittheweather.R;

public class SecondActivity extends AppCompatActivity {

    private final static String TAG = "SecondActivity";

    private VolleyClient volleyClient = new VolleyClient();
    private WeatherJsonParser parser = new WeatherJsonParser();

    private Button button;
    private double latitude;
    private double longitude;
    private ListView listView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        listView = findViewById(R.id.second_idListView);

        latitude = getIntent().getExtras().getDouble("Latitude");
        longitude = getIntent().getExtras().getDouble("Longitude");

        button = (Button) findViewById(R.id.second_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        volleyClient.getWeatherObject(this, "DAILY", latitude, longitude, new IVolleyCallback() {
            @Override
            public void onSuccess(Context context, JSONObject response) {
                try {
                    ArrayList<DailyWeather> dailyWeatherList = new ArrayList<>(parser.parseForecast(response));

                    DailyWeatherAdapter dailyWeatherAdapter = new DailyWeatherAdapter(context, dailyWeatherList);
                    listView.setAdapter(dailyWeatherAdapter);
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
}