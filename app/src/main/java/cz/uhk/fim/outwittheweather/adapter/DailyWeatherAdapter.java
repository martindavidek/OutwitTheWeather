package cz.uhk.fim.outwittheweather.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Locale;

import cz.uhk.fim.outwittheweather.R;
import cz.uhk.fim.outwittheweather.model.DailyWeather;
import cz.uhk.fim.outwittheweather.utils.DateTimeConverter;

public class DailyWeatherAdapter extends ArrayAdapter<DailyWeather> {

    Context context;

    public DailyWeatherAdapter(@NonNull Context context, ArrayList<DailyWeather> weatherArrayList) {
        super(context, 0, weatherArrayList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DailyWeather dailyWeather = getItem(position);

        DateTimeConverter converter = new DateTimeConverter();

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_day_weather, parent, false);
        }

        TextView dayView = convertView.findViewById(R.id.item_day);
        TextView weatherView = convertView.findViewById(R.id.item_weather);
        TextView weatherDescriptionView = convertView.findViewById(R.id.item_weather_desc);
        TextView minimumView = convertView.findViewById(R.id.item_min_value);
        TextView maximumView = convertView.findViewById(R.id.item_max_value);
        TextView feelsLikeView = convertView.findViewById(R.id.item_feels_like_value);
        TextView windSpeedView = convertView.findViewById(R.id.item_wind_value);
        TextView pressureView = convertView.findViewById(R.id.item_pressure_value);

        dayView.setText(converter.getDate(dailyWeather.getDt()));

        String icon = dailyWeather.getWeather().getIcon();

        if (icon != null) {
            ImageView iconView = convertView.findViewById(R.id.item_icon);
            Glide.with(context)
                    .load("https://openweathermap.org/img/wn/" + icon + "@2x.png")
                    .apply(new RequestOptions().override(250, 250))
                    .into(iconView);
        }

        weatherView.setText(dailyWeather.getWeather().getMain());
        weatherDescriptionView.setText(dailyWeather.getWeather().getDescription());
        minimumView.setText(String.format(Locale.ENGLISH, "%.1f%s", dailyWeather.getNormalTemperature().getMin(), context.getString(R.string.celsius_degree)));
        maximumView.setText(String.format(Locale.ENGLISH, "%.1f%s", dailyWeather.getNormalTemperature().getMax(), context.getString(R.string.celsius_degree)));
        feelsLikeView.setText(String.format(Locale.ENGLISH, "%.1f%s", dailyWeather.getFeelsLikeTemperature().getDay(), context.getString(R.string.celsius_degree)));
        windSpeedView.setText(String.format(Locale.ENGLISH, "%.1f%s", dailyWeather.getWindSpeed(), context.getString(R.string.speed_units)));
        pressureView.setText(String.format(Locale.ENGLISH, "%d %s", dailyWeather.getPressure(), context.getString(R.string.pressure_units)));

        return convertView;
    }
}
