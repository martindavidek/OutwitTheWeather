package cz.uhk.fim.outwittheweather.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder notificationCompatBuilder = notificationHelper.getChannel1Notification("Title", "Text", "Big text");
        notificationHelper.getManager().notify(1, notificationCompatBuilder.build());
    }
}
