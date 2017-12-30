package com.user.maevis.controllers;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.user.maevis.ListItem;
import com.user.maevis.NotificationView;
import com.user.maevis.R;
import com.user.maevis.ReportPage;
import com.user.maevis.UploadReport;
import com.user.maevis.models.FirebaseDatabaseManager;

import java.util.UUID;

import static android.content.Context.NOTIFICATION_SERVICE;


public class cNotification {

    //notification for verification with data
    public static void showVerifyReportNotification(Application app){

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

        NotificationManager nm = (NotificationManager) app.getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0, builder.build());

    }

    //notification for view report with data
    public static void showViewReportNotification(Context context, ListItem nearbyReport){

        String fullName = FirebaseDatabaseManager.getFullName(nearbyReport.getReportedBy());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_notif_maevis_logo)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setContentTitle(nearbyReport.getReportType() + " Report")
                .setLights(0xff00ff00, 500, 500)
                .setAutoCancel(true)
                .setContentText(fullName + " reported a " +  nearbyReport.getReportType()
                        + " near your location.");

        Intent i = new Intent(context, ReportPage.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NotificationView.class);
        stackBuilder.addNextIntent(i);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        //nm.notify(0, builder.build());

        SharedPreferences prefs = context.getSharedPreferences(Activity.class.getSimpleName(), Context.MODE_PRIVATE);
        int notificationNumber = prefs.getInt("notificationNumber", 0);

        nm.notify(notificationNumber ,  builder.build());
        SharedPreferences.Editor editor = prefs.edit();
        notificationNumber++;
        editor.putInt("notificationNumber", notificationNumber);
        editor.commit();

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

    //partial notification code
    public static void alertNotification(Context context){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setLights(Color.BLUE, 500, 500)
                .setSmallIcon(R.drawable.ic_notif_maevis_logo)
                .setContentTitle("MAEVIS")
                .setContentText("maevis notification")
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setAutoCancel(true);

        Intent i = new Intent(context, NotificationView.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NotificationView.class);
        stackBuilder.addNextIntent(i);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //nm.notify(0, builder.build());

        SharedPreferences prefs = context.getSharedPreferences(Activity.class.getSimpleName(), Context.MODE_PRIVATE);
        int notificationNumber = prefs.getInt("notificationNumber", 0);

        nm.notify(notificationNumber ,  builder.build());
        SharedPreferences.Editor editor = prefs.edit();
        notificationNumber++;
        editor.putInt("notificationNumber", notificationNumber);
        editor.commit();


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
