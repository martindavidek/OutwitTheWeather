package cz.uhk.fim.outwittheweather.volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class VolleyClient {

    private static final String TAG = "VolleyClient";

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static final String ONE_CALL_API = "onecall?";
    private static final String CURRENT_WEATHER_API = "weather?";
    private static final String DAILY_PARAMS = "&exclude=current,minutely,hourly";
    private static final String NOW_PARAMS = "&exclude=minutely,hourly,daily";
    private static final String METRIC_PARAM = "&units=metric";
    private static final String API_KEY = "&appid=74cd1c375d53a47391f6f893aaaca272";

    RequestQueue requestQueue;

    /*
     * Get city coordinates.
     * Using API for current weather to get coordinates by city name in order to exclude another service integration.
     * One-call API is the only free API providing daily forecast but the only available location input params are latitude and longitude.
     */
    public void getCoordinates(final Context context, String city, final IVolleyCallback callback) {

        requestQueue = Volley.newRequestQueue(context);

        String Url = BASE_URL + CURRENT_WEATHER_API + "q=" + city + METRIC_PARAM + API_KEY;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(context, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
                Log.e(TAG, "Response error: " + error.toString());
            }
        });

        /*requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject responseCoord = response.getJSONObject("coord");
                    position.setLatitude(responseCoord.getDouble("lat"));
                    position.setLongitude(responseCoord.getDouble("lon"));
                    position.setCityName(response.getString("name"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Response error: " + error.toString());
            }
        });*/

        requestQueue.add(request);
    }

    //Handles forecast requests using Volley and returns response JSONObject via Volley callback
    public void getWeatherObject(final Context context, String forecastType, double latitude, double longitude, final IVolleyCallback callback) {
        String lat = "lat=" + String.valueOf(latitude);
        String lon = "&lon=" + String.valueOf(longitude);

        String excludeParams;

        switch(forecastType) {
            case "CURRENT":
                excludeParams = NOW_PARAMS;
                break;
            case "DAILY":
                excludeParams = DAILY_PARAMS;
                break;
            default:
                Log.e(TAG, "Error: Incorrect forecast type getWeatherObject() method input.");
                return;
        }

        String Url = BASE_URL + ONE_CALL_API + lat + lon + excludeParams + METRIC_PARAM + API_KEY;
        requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(context, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
                Log.e(TAG, "Response error: " + error.toString());
            }
        });

        requestQueue.add(request);
    }
}
