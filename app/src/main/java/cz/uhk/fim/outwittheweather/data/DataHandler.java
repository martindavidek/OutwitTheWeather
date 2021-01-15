package cz.uhk.fim.outwittheweather.data;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.util.Log;

public class DataHandler extends ContextWrapper {
    private static final String TAG = "DataHandler";
    private static final String ALARM_PREFERENCES = "AlarmPreferences";
    private static final String TIME_NOTIFICATION = "NotificationTime";
    private static final String TIME_GET_DATA = "GetDataTime";

    public DataHandler(Context base) {
        super(base);
    }

    public void saveAlarm(long alarm) {
        //Set time to get weather data to notification trigger time minus 5 minutes
        long getDataTime = alarm - 300000;

        SharedPreferences sharedPreferences = getSharedPreferences(ALARM_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putLong(TIME_NOTIFICATION, alarm);
        editor.putLong(TIME_GET_DATA, getDataTime);

        editor.apply();

        Log.i(TAG, "Alarm preferences saved");
        Log.d(TAG, "Saved notification time: " + alarm);
        Log.d(TAG, "Saved get data time: " + getDataTime);
    }

    public long loadNotificationTime() {
        SharedPreferences sharedPreferences = getSharedPreferences(ALARM_PREFERENCES, MODE_PRIVATE);

        return sharedPreferences.getLong(TIME_NOTIFICATION, 0);
    }

    public long loadGetDataTime() {
        SharedPreferences sharedPreferences = getSharedPreferences(ALARM_PREFERENCES, MODE_PRIVATE);

        return sharedPreferences.getLong(TIME_GET_DATA, 0);
    }
}
