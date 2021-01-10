package cz.uhk.fim.outwittheweather.model;

public class DailyWeather {

    private Integer dt;

    private Integer sunrise;

    private Integer sunset;

    private NormalTemperature normalTemperature;

    private FeelsLikeTemperature feelsLikeTemperature;

    private Integer pressure;

    private Integer humidity;

    private Double dewPoint;

    private Double windSpeed;

    private Integer windDeg;

    private Weather weather;

    private Integer clouds;

    private Integer pop;

    private Double uvi;

    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public Integer getSunrise() {
        return sunrise;
    }

    public void setSunrise(Integer sunrise) {
        this.sunrise = sunrise;
    }

    public Integer getSunset() {
        return sunset;
    }

    public void setSunset(Integer sunset) {
        this.sunset = sunset;
    }

    public NormalTemperature getNormalTemperature() {
        return normalTemperature;
    }

    public void setNormalTemperature(NormalTemperature normalTemperature) {
        this.normalTemperature = normalTemperature;
    }

    public FeelsLikeTemperature getFeelsLikeTemperature() {
        return feelsLikeTemperature;
    }

    public void setFeelsLikeTemperature(FeelsLikeTemperature feelsLikeTemperature) {
        this.feelsLikeTemperature = feelsLikeTemperature;
    }

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Double getDewPoint() {
        return dewPoint;
    }

    public void setDewPoint(Double dewPoint) {
        this.dewPoint = dewPoint;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Integer getWindDeg() {
        return windDeg;
    }

    public void setWindDeg(Integer windDeg) {
        this.windDeg = windDeg;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public Integer getClouds() {
        return clouds;
    }

    public void setClouds(Integer clouds) {
        this.clouds = clouds;
    }

    public Integer getPop() {
        return pop;
    }

    public void setPop(Integer pop) {
        this.pop = pop;
    }

    public Double getUvi() {
        return uvi;
    }

    public void setUvi(Double uvi) {
        this.uvi = uvi;
    }
}
