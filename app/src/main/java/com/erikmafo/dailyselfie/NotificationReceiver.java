package com.erikmafo.dailyselfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by erikmafo on 17.11.15.
 */
public class NotificationReceiver extends BroadcastReceiver {

    private static final int MY_NOTIFICATION_ID = 1;
    private static final String TAG = NotificationReceiver.class.getSimpleName();

    // Notification Text Elements
    private final CharSequence contentTitle = "Daily Selfie";
    private final CharSequence contentText = "Time for another selfie";

    private final long[] mVibratePattern = { 0, 200, 200, 300 };

    private Intent mNotificationIntent;
    private PendingIntent mContentIntent;


    @Override
    public void onReceive(Context context, Intent intent) {

        // The Intent to be used when the user clicks on the Notification View
        mNotificationIntent = new Intent(context, MainActivity.class);

        // The PendingIntent that wraps the underlying Intent
        mContentIntent = PendingIntent.getActivity(context, 0,
                mNotificationIntent, 0);


        // Build the Notification
        Notification.Builder notificationBuilder = new Notification.Builder(
                context).setAutoCancel(true).setContentTitle(contentTitle)
                .setContentText(contentText).setContentIntent(mContentIntent)
                .setVibrate(mVibratePattern)
                .setSmallIcon(R.drawable.ic_camera_alt_black_24dp);

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        // Pass the Notification to the NotificationManager:
        notificationManager.notify(MY_NOTIFICATION_ID,
                notificationBuilder.build());


        // Log occurence of notify() call
        Log.i(TAG, "Sending notification at:"
                + DateFormat.getDateTimeInstance().format(new Date()));
    }

}
