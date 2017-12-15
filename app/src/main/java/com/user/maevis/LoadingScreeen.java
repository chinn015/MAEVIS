package com.user.maevis;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.user.maevis.models.FirebaseDatabaseManager;

import java.util.Collections;
import java.util.Comparator;

public class LoadingScreeen extends AppCompatActivity {
    private static final String TAG = LoadingScreeen.class.getSimpleName();
    private final int SPLASH_DISPLAY_LENGTH = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        ActionBar actionBar = getSupportActionBar();
        if(null != actionBar){
            actionBar.hide();
        }
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent startActivityIntent = new Intent(LoadingScreeen.this, Login.class);
                startActivity(startActivityIntent);
                LoadingScreeen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);


        //initialize Firebase Database Manager
        FirebaseDatabaseManager.initializeFirebaseDatabaseManager();

        //listener to store all users from the Firebase Database to a List
        FirebaseDatabaseManager.FirebaseUsers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserItem user = new UserItem(dataSnapshot.getKey().toString(),
                        dataSnapshot.child("address").getValue().toString(),
                        dataSnapshot.child("birthdate").getValue().toString(),
                        dataSnapshot.child("email").getValue().toString(),
                        dataSnapshot.child("firstName").getValue().toString(),
                        dataSnapshot.child("lastName").getValue().toString(),
                        dataSnapshot.child("userType").getValue().toString(),
                        dataSnapshot.child("username").getValue().toString());

                FirebaseDatabaseManager.getUserItems().add(user);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //listener to store all reports from the Firebase Database to a List
        FirebaseDatabaseManager.FirebaseReports.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String reportDateTime = dataSnapshot.child("dateTime").getValue().toString();

                String year  = reportDateTime.substring(0,4);
                String monthNum = reportDateTime.substring(5,7);
                String day = reportDateTime.substring(8,10);

                String month="";
                switch (monthNum) {
                    case "01": month = "JAN"; break;
                    case "02": month = "FEB"; break;
                    case "03": month = "MAR"; break;
                    case "04": month = "APR"; break;
                    case "05": month = "MAY"; break;
                    case "06": month = "JUN"; break;
                    case "07": month = "JUL"; break;
                    case "08": month = "AUG"; break;
                    case "09": month = "SEP"; break;
                    case "10": month = "OCT"; break;
                    case "11": month = "NOV"; break;
                    case "12": month = "DEC"; break;
                }

                String time = reportDateTime.substring(11, reportDateTime.length());
                String hour = time.substring(0, 2);
                int hr = Integer.parseInt(hour);
                if(hr > 9) {
                    hour = time.substring(0, 2);
                } else {
                    hour = time.substring(1, 2);
                }
                String min = time.substring(3,5);
                String period = time.substring(9, time.length());

                String formatDateTime = hour+":"+min+" "+period+" - "+month+" "+day+" "+year;

                //parse Long to Double for Latitude and Longitude values
                double locationLatitude = 0.0000;
                Object locLat = dataSnapshot.child("locationLatitude").getValue();
                if (locLat instanceof Long) {
                    locationLatitude = ((Long) locLat).doubleValue();
                } else {
                    locationLatitude = (double) dataSnapshot.child("locationLatitude").getValue();
                }

                double locationLongitude = 0.0000;
                Object locLong = dataSnapshot.child("locationLongitude").getValue();
                if (locLong instanceof Long) {
                    locationLongitude = ((Long) locLong).doubleValue();
                } else {
                    locationLongitude = (double) dataSnapshot.child("locationLongitude").getValue();
                }

                ListItem item = new ListItem(dataSnapshot.getKey().toString(),
                        dataSnapshot.child("reportedBy").getValue().toString() + " reported a " +
                                dataSnapshot.child("reportType").getValue().toString() + " at " +
                                dataSnapshot.child("location").getValue().toString(),
                        dataSnapshot.child("dateTime").getValue().toString(),
                        dataSnapshot.child("description").getValue().toString(),
                        dataSnapshot.child("imageURL").getValue().toString(),
                        dataSnapshot.child("location").getValue().toString(),
                        locationLatitude,
                        locationLongitude,
                        dataSnapshot.child("reportStatus").getValue().toString(),
                        dataSnapshot.child("reportType").getValue().toString(),
                        dataSnapshot.child("reportedBy").getValue().toString(),
                        formatDateTime);

                FirebaseDatabaseManager.getListItems().add(item);
                Collections.sort(FirebaseDatabaseManager.getListItems(), new Comparator<ListItem>() {
                    @Override
                    public int compare(ListItem o1, ListItem o2) {
                        if (o1.getDateTime() == null || o2.getDateTime() == null) {
                            return 0;
                        }
                        return o1.getDateTime().compareTo(o2.getDateTime());
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}