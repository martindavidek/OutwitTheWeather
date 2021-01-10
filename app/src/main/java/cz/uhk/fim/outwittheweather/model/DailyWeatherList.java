package cz.uhk.fim.outwittheweather.model;

import java.util.ArrayList;
import java.util.List;

public class DailyWeatherList
{
    private List<DailyWeather> dailyWeatherList = new ArrayList<>();

    public List<DailyWeather> getDailyWeatherList() {
        return dailyWeatherList;
    }

    public void setDailyWeatherList(List<DailyWeather> daily) {
        this.dailyWeatherList = daily;
    }
}
