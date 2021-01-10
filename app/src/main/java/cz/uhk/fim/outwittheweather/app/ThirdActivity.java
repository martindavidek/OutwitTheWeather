package cz.uhk.fim.outwittheweather.app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.uhk.fim.outwittheweather.adapter.DailyWeatherAdapter;
import cz.uhk.fim.outwittheweather.model.DailyWeather;
import cz.uhk.fim.outwittheweather.model.Position;
import cz.uhk.fim.outwittheweather.parser.WeatherJsonParser;
import cz.uhk.fim.outwittheweather.volley.IVolleyCallback;
import cz.uhk.fim.outwittheweather.volley.VolleyClient;
import cz.uhk.fim.outwittheweather.R;

public class ThirdActivity extends AppCompatActivity {

    private final static String TAG = "ThirdActivity";

    private VolleyClient volleyClient = new VolleyClient();
    private WeatherJsonParser parser = new WeatherJsonParser();

    private EditText cityText;
    private ListView listView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        final Context context = this;

        listView = findViewById(R.id.third_idListView);

        Button button2 = (Button) findViewById(R.id.third_button2);
        cityText = (EditText)findViewById(R.id.third_city_input);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = cityText.getText().toString();
                volleyClient.getCoordinates(context, city, new IVolleyCallback() {
                    @Override
                    public void onSuccess(Context context, JSONObject response) {

                        try {
                            Position position = parser.parsePosition(response);

                            TextView cityView = (TextView) findViewById(R.id.third_city);
                            cityView.setText(position.getCityName());

                            volleyClient.getWeatherObject(context, "DAILY", position.getLatitude(), position.getLongitude(), new IVolleyCallback(){
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
        });

        Button button1 = (Button) findViewById(R.id.third_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThirdActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}