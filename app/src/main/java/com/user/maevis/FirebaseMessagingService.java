package com.user.maevis;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.google.firebase.messaging.RemoteMessage;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);

        String notif_title = remoteMessage.getNotification().getTitle();
        String notif_message = remoteMessage.getNotification().getBody();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notif_maevis_logo)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setContentTitle(notif_title)
                .setContentText(notif_message)
                .setAutoCancel(true);

        int mNotificationId = (int) System.currentTimeMillis();

        NotificationManager mNotifiyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        vibrateNotification(getApplication());

        mNotifiyMgr.notify(mNotificationId, mBuilder.build());

    }

    public static void vibrateNotification(Application app){
        // Get instance of Vibrator from current Context
        Vibrator vib = (Vibrator) app.getSystemService(Context.VIBRATOR_SERVICE);

        // Vibrate for 400 milliseconds
        //vib.vibrate(1000);

        // Start without a delay
        // Each element then alternates between vibrate, sleep, vibrate, sleep...
        long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};

        // The '-1' here means to vibrate once, as '-1' is out of bounds in the pattern array
        vib.vibrate(pattern, -1);

    }
}
