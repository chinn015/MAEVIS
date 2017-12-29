package com.user.maevis.controllers;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.user.maevis.NotificationView;
import com.user.maevis.R;
import com.user.maevis.UploadReport;
import com.user.maevis.models.FirebaseDatabaseManager;


public class Notification {

    //partial notification code
    public static void alertNotification(Context context){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setLights(Color.BLUE, 500, 500)
                .setSmallIcon(R.drawable.ic_notif_maevis_logo)
                .setContentTitle("MAEVIS")
                .setContentText("maevis notification")
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setAutoCancel(true)
                .setOngoing(true);

        Intent i = new Intent(context, NotificationView.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NotificationView.class);
        stackBuilder.addNextIntent(i);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0, builder.build());

    }

    //notification with data
    public static void showNotification(Application app){

        String fullName = FirebaseDatabaseManager.getFullName(UploadReport.reportModel.getReportedBy());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(app);
        builder.setSmallIcon(R.drawable.ic_notif_maevis_logo)
                .setColor(ContextCompat.getColor(app, R.color.colorPrimary))
                .setContentTitle(UploadReport.reportModel.getReportType() + " Report")
                .setLights(0xff00ff00, 500, 500)
                .setAutoCancel(true)
                .setContentText(fullName + " reported a " +  UploadReport.reportModel.getReportType()
                + " Report" + " at " + UploadReport.reportModel.getLocation());

        Intent i = new Intent(app, NotificationView.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(app);
        stackBuilder.addParentStack(NotificationView.class);
        stackBuilder.addNextIntent(i);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager) app.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0, builder.build());

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

    //partial code
    public static void vibrateNotif(Context context){
        // Get instance of Vibrator from current Context
        Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        // Vibrate for 400 milliseconds
        //vib.vibrate(1000);

        // Start without a delay
        // Each element then alternates between vibrate, sleep, vibrate, sleep...
        long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};

        // The '-1' here means to vibrate once, as '-1' is out of bounds in the pattern array
        vib.vibrate(pattern, -1);

    }
}
