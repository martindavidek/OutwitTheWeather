package cz.uhk.fim.outwittheweather.jobservice;

import android.app.AlarmManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.util.Log;

import cz.uhk.fim.outwittheweather.data.DataHandler;

public class AlarmJobService extends JobService {
    private static final String TAG = "AlarmJobService";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Alarm job service started");

        checkAlarmOnBackground(params);

        return true;
    }

    private void checkAlarmOnBackground(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                if (alarmManager.getNextAlarmClock() != null) {
                    Log.d(TAG, "ALARM NEXT: " + alarmManager.getNextAlarmClock().getTriggerTime());
                    DataHandler dataHandler = new DataHandler(null);
                    dataHandler.saveAlarm(alarmManager.getNextAlarmClock().getTriggerTime());
                } else {
                    Log.d(TAG, "ALARM NEXT not set");
                }
                jobFinished(params, false);
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Alarm job service stopped before completion");

        return false;
    }
}
