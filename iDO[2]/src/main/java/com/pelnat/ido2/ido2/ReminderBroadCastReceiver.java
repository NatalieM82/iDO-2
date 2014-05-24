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


        Intent myIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, intent.getIntExtra("taskMessage", 0), myIntent, 0);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new Notification(R.drawable.ic_launcher, "iDO[2]", System.currentTimeMillis());
        notification.setLatestEventInfo(context,"iDO[2] Reminder", intent.getStringExtra("taskMessage"), pendingIntent);

        notificationManager.notify(intent.getIntExtra("taskMessage",0), notification);



    }
}
