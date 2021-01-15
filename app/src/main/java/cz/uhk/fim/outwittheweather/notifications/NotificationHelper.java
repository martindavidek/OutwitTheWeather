package cz.uhk.fim.outwittheweather.notifications;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import cz.uhk.fim.outwittheweather.R;

public class NotificationHelper extends ContextWrapper {
    public static String CHANNEL_ID = "channelId1";
    public static String CHANNEL_NAME = "channel1";

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createChannels() {
        NotificationChannel notificationChannel1 = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel1.enableLights(true);
        notificationChannel1.enableVibration(true);
        notificationChannel1.setLightColor(R.color.colorPrimary);
        notificationChannel1.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        getManager().createNotificationChannel( notificationChannel1);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
             mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannel1Notification(String title, String notificationText, String notificationBigText){
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setColor(Color.RED)
                .setSmallIcon(R.drawable.ic_otw)
                .setContentTitle(title)
                .setContentText(notificationText)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationBigText));
    }

}
