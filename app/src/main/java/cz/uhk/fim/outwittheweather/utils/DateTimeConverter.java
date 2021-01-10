package cz.uhk.fim.outwittheweather.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeConverter {

    //Convert Unix epoch time to human-readable date format
    @SuppressLint("SimpleDateFormat")
    public String getDate(long epoch) {
        Date date = new Date();
        date.setTime(epoch*1000);
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    //Convert Unix epoch time to human-readable time stamp format
    @SuppressLint("SimpleDateFormat")
    public String getTimeStamp(long epoch) {
        Date date = new Date();
        date.setTime(epoch*1000);
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);
    }
}
