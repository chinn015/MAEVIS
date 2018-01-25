package com.user.maevis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class NotificationView extends AppCompatActivity implements View.OnClickListener{
    private TextView viewNotifHead;
    private TextView viewNotifDesc;
    private TextView viewNotifDateTime;
    private ImageView viewNotifImage;

    private Button btnVerifyReport;
    private Button btnDeclineReport;

    private DatabaseReference FirebaseReports;
    private Iterator<DataSnapshot> items;
    private List<ListItem> listItems;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewNotifHead = (TextView) findViewById(R.id.viewNotifHead);
        viewNotifDesc = (TextView) findViewById(R.id.viewNotifDesc);
        viewNotifDateTime = (TextView) findViewById(R.id.viewNotifDateTime);
        viewNotifImage = (ImageView) findViewById(R.id.viewNotifImage);

        //display details of clicked item
        viewNotifHead.setText(UploadReport.reportModel.getReportType());
        viewNotifDesc.setText(UploadReport.reportModel.getDescription());
        viewNotifDateTime.setText(UploadReport.reportModel.getDateTime());
        Picasso.with(getApplicationContext()).load(UploadReport.reportModel.getImageURL()).into(viewNotifImage);

        btnVerifyReport = (Button) findViewById(R.id.btnBlockUser);
        btnDeclineReport = (Button) findViewById(R.id.btnProfileUM);

        btnVerifyReport.setOnClickListener(this);
        btnDeclineReport.setOnClickListener(this);

        FirebaseReports = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maevis-ecd17.firebaseio.com/Reports");

        listItems = new ArrayList<>();

        FirebaseReports.addChildEventListener(new ChildEventListener() {
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

//                ListItem item = new ListItem(dataSnapshot.getKey().toString(),
//                        dataSnapshot.child("reportedBy").getValue().toString() + " reported a " +
//                                dataSnapshot.child("reportType").getValue().toString() + " at " +
//                                dataSnapshot.child("location").getValue().toString(),
//                        dataSnapshot.child("dateTime").getValue().toString(),
//                        dataSnapshot.child("description").getValue().toString(),
//                        dataSnapshot.child("imageURL").getValue().toString(),
//                        dataSnapshot.child("location").getValue().toString(),
//                        locationLatitude,
//                        locationLongitude,
//                        dataSnapshot.child("mergedTo").getValue().toString(),
//                        dataSnapshot.child("reportStatus").getValue().toString(),
//                        dataSnapshot.child("reportType").getValue().toString(),
//                        dataSnapshot.child("reportedBy").getValue().toString(),
//                        formatDateTime);
//
//                if(item.getReportStatus().equals("Pending")) {
//                    listItems.add(item);
//                }
//
//                Collections.sort(listItems, new Comparator<ListItem>() {
//                    @Override
//                    public int compare(ListItem o1, ListItem o2) {
//                        if (o1.getDateTime() == null || o2.getDateTime() == null) {
//                            return 0;
//                        }
//                        return o1.getDateTime().compareTo(o2.getDateTime());
//                    }
//                });
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

    @Override
    public void onClick(View v) {
        if(v==btnVerifyReport) {
            Toast.makeText(NotificationView.this, "Report verified.", Toast.LENGTH_SHORT).show();
            FirebaseReports.child(UploadReport.newReport.getKey()).child("reportStatus").setValue("Verified");
            finish();
            startActivity(new Intent(NotificationView.this, Sidebar_HomePage.class));
            return;
        }

        if(v==btnDeclineReport) {
            Toast.makeText(NotificationView.this, "Report declined.", Toast.LENGTH_SHORT).show();
            FirebaseReports.child(UploadReport.newReport.getKey()).child("reportStatus").setValue("Declined");
            finish();
            startActivity(new Intent(NotificationView.this, Sidebar_HomePage.class));
            return;
        }
    }
}
