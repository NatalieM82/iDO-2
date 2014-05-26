package com.pelnat.ido2.ido2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Natalie on 23/05/2014.
 */
public class ReminderBroadCastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        //do something QUICK
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Get taskId & message
        int taskId = intent.getIntExtra("taskId", 0);
        String notificationText = intent.getStringExtra("taskMessage");

        Intent myIntent = new Intent(context, MainActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, 0);

        Notification notification = new Notification(R.drawable.ic_launcher, "iDO[2] Reminder!", System.currentTimeMillis());
        notification.setLatestEventInfo(context, "iDO[2]", notificationText, pendingIntent);
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(null, taskId, notification); //0 is id

    }
}
