package cz.uhk.fim.outwittheweather.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.uhk.fim.outwittheweather.model.CurrentWeather;
import cz.uhk.fim.outwittheweather.model.DailyWeather;
import cz.uhk.fim.outwittheweather.model.FeelsLikeTemperature;
import cz.uhk.fim.outwittheweather.model.NormalTemperature;
import cz.uhk.fim.outwittheweather.model.Position;
import cz.uhk.fim.outwittheweather.model.Weather;

public class WeatherJsonParser {

    //Parse current weather from JSONObject
    public CurrentWeather parseCurrentWeather(JSONObject input) throws JSONException {
        CurrentWeather currentWeather = new CurrentWeather();

        JSONObject jsonObject = input.getJSONObject("current");

        currentWeather.setDt(jsonObject.getLong("dt"));
        currentWeather.setSunrise(jsonObject.getLong("sunrise"));
        currentWeather.setSunset(jsonObject.getLong("sunset"));
        currentWeather.setTemp(jsonObject.getDouble("temp"));
        currentWeather.setFeelsLike(jsonObject.getDouble("feels_like"));
        currentWeather.setPressure(jsonObject.getInt("pressure"));
        currentWeather.setHumidity(jsonObject.getInt("humidity"));
        currentWeather.setDewPoint(jsonObject.getDouble("dew_point"));
        currentWeather.setUvi(jsonObject.getDouble("uvi"));
        currentWeather.setClouds(jsonObject.getInt("clouds"));
        currentWeather.setVisibility(jsonObject.getInt("visibility"));
        currentWeather.setWindSpeed(jsonObject.getDouble("wind_speed"));
        currentWeather.setWindDeg(jsonObject.getInt("wind_deg"));
        //Weather JSONArray contains always only one JSONObject
        currentWeather.setWeather(parseWeather(jsonObject.getJSONArray("weather").getJSONObject(0)));

        return currentWeather;
    }

    //Parse daily forecasts JSONArray to array of daily weathers
    public List<DailyWeather> parseForecast(JSONObject input) throws JSONException {

        List<DailyWeather> dailyWeathers = new ArrayList<>();

        JSONArray jsonArray = input.getJSONArray("daily");

        for(int i=0;i<jsonArray.length();i++) {
            DailyWeather dailyWeather = new DailyWeather();
            NormalTemperature normalTemperature = new NormalTemperature();
            FeelsLikeTemperature feelsLikeTemperature = new FeelsLikeTemperature();

            JSONObject responseDay = jsonArray.getJSONObject(i);

            dailyWeather.setDt(responseDay.getInt("dt"));
            dailyWeather.setSunrise(responseDay.getInt("sunrise"));
            dailyWeather.setSunset(responseDay.getInt("sunset"));

            JSONObject responseNormalTemperature = responseDay.getJSONObject("temp");

            normalTemperature.setDay(responseNormalTemperature.getDouble("day"));
            normalTemperature.setMin(responseNormalTemperature.getDouble("min"));
            normalTemperature.setMax(responseNormalTemperature.getDouble("max"));
            normalTemperature.setNight(responseNormalTemperature.getDouble("night"));
            normalTemperature.setEve(responseNormalTemperature.getDouble("eve"));
            normalTemperature.setMorn(responseNormalTemperature.getDouble("morn"));

            dailyWeather.setNormalTemperature(normalTemperature);

            JSONObject responseFeelsLikeTemperature = responseDay.getJSONObject("feels_like");

            feelsLikeTemperature.setDay(responseFeelsLikeTemperature.getDouble("day"));
            feelsLikeTemperature.setNight(responseFeelsLikeTemperature.getDouble("night"));
            feelsLikeTemperature.setEve(responseFeelsLikeTemperature.getDouble("eve"));
            feelsLikeTemperature.setMorn(responseFeelsLikeTemperature.getDouble("morn"));

            dailyWeather.setFeelsLikeTemperature(feelsLikeTemperature);
            dailyWeather.setPressure(responseDay.getInt("pressure"));
            dailyWeather.setHumidity(responseDay.getInt("humidity"));
            dailyWeather.setDewPoint(responseDay.getDouble("dew_point"));
            dailyWeather.setWindSpeed(responseDay.getDouble("wind_speed"));
            dailyWeather.setWindDeg(responseDay.getInt("wind_deg"));

            //Weather JSONArray contains always only one JSONObject
            dailyWeather.setWeather(parseWeather(responseDay.getJSONArray("weather").getJSONObject(0)));

            dailyWeather.setClouds(responseDay.getInt("clouds"));
            dailyWeather.setPop(responseDay.getInt("pop"));
            dailyWeather.setUvi(responseDay.getDouble("uvi"));

            dailyWeathers.add(i, dailyWeather);
        }

        return dailyWeathers;
    }

    //Parse weather additional data from JSONObject (first and the only object from weather array)
    public Weather parseWeather(JSONObject jsonObject) throws JSONException {
        Weather weather = new Weather();

        weather.setId(jsonObject.getInt("id"));
        weather.setMain(jsonObject.getString("main"));
        weather.setDescription(jsonObject.getString("description"));
        weather.setIcon(jsonObject.getString("icon"));

        return weather;
    }

    public Position parsePosition(JSONObject response) throws JSONException {
        Position position = new Position();

        JSONObject coordObject = response.getJSONObject("coord");
        position.setLatitude(coordObject.getDouble("lat"));
        position.setLongitude(coordObject.getDouble("lon"));
        position.setCityName(response.getString("name"));

        return position;
    }
}
