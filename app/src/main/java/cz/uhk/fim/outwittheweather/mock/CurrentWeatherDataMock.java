package cz.uhk.fim.outwittheweather.mock;

import cz.uhk.fim.outwittheweather.model.CurrentWeather;
import cz.uhk.fim.outwittheweather.model.Weather;

public class CurrentWeatherDataMock {


    public static CurrentWeather getCurrentWeather() {
        CurrentWeather currentWeather = new CurrentWeather();

        currentWeather.setClouds(90);
        currentWeather.setDewPoint(-1.05);
        currentWeather.setDt(Long.valueOf("1610398910"));
        currentWeather.setFeelsLike(-6.02);
        currentWeather.setHumidity(80);
        currentWeather.setPressure(1006);
        currentWeather.setSunrise(Long.valueOf("1610351580"));
        currentWeather.setSunset(Long.valueOf("1610382000"));
        currentWeather.setTemp(1.58);
        currentWeather.setUvi(0.0);
        currentWeather.setVisibility(10000);
        currentWeather.setWeather(getWeather());
        currentWeather.setWindDeg(290);
        currentWeather.setWindSpeed(7.72);

        return currentWeather;
    }

    private static Weather getWeather() {
        Weather weather = new Weather();

        weather.setDescription("overcast clouds");
        weather.setIcon("04n");
        weather.setId(804);
        weather.setMain("Clouds");

        return weather;
    }
}
