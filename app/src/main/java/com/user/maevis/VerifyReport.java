package com.user.maevis;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.user.maevis.controllers.cNotification;
import com.user.maevis.models.FirebaseDatabaseManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class VerifyReport extends AppCompatActivity implements View.OnClickListener {

    private TextView viewNotifHead;
    private TextView viewNotifDesc;
    private TextView viewNotifDateTime;
    private ImageView viewNotifImage;
    private ImageView viewNotifReportType;

    private Button btnVerifyReport;
    private Button btnDeclineReport;

    private DatabaseReference FirebaseReports;
    private Iterator<DataSnapshot> items;
    private List<ListItem> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_report);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewNotifHead = (TextView) findViewById(R.id.viewNotifHead);
        viewNotifDesc = (TextView) findViewById(R.id.viewNotifDesc);
        viewNotifDateTime = (TextView) findViewById(R.id.viewNotifDateTime);
        viewNotifImage = (ImageView) findViewById(R.id.viewNotifImage);
        viewNotifReportType = (ImageView) findViewById(R.id.viewReportType);


        //display details of clicked item
        viewNotifHead.setText(Tab2_Location.pendingReport.getHead());
        viewNotifDesc.setText(Tab2_Location.pendingReport.getDescription());
        viewNotifDateTime.setText(Tab2_Location.pendingReport.getDisplayDateTime());
        Picasso.with(getApplicationContext())
                .load(Tab2_Location.pendingReport.getImageURL())
                .fit()
                .into(viewNotifImage);

        Picasso.with(getApplicationContext())
                .load(ListItem.getReportTypeImage((Tab2_Location.pendingReport.getReportType())))
                .into(viewNotifReportType);


        btnVerifyReport = (Button) findViewById(R.id.btnVerifyReport);
        btnDeclineReport = (Button) findViewById(R.id.btnDeclineReport);

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

                if(item.getReportStatus().equals("Pending")) {
                    listItems.add(item);
                }

                Collections.sort(listItems, new Comparator<ListItem>() {
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

    @Override
    public void onClick(View v) {
        if(v==btnVerifyReport) {
            clickedReportBasis = Tab2_Location.pendingReport;

            for(int x = 0; x < FirebaseDatabaseManager.getPendingReports().size(); x++) {
                ListItem pendingReport = FirebaseDatabaseManager.getPendingReports().get(x);
                double vLatitude = pendingReport.getLocationLatitude();
                double vLongitude = pendingReport.getLocationLongitude();
                float distance, limit_distance;

                limit_distance = 300;
                Location report_locations = new Location("1");
                Location verify_location = new Location("2");
                String vTitle = pendingReport.getReportType()+" "+FirebaseDatabaseManager.getFullName(pendingReport.getReportedBy());

                report_locations.setLatitude(vLatitude);
                report_locations.setLongitude(vLongitude);

                verify_location.setLatitude(pendingReport.getLocationLatitude());
                verify_location.setLongitude(pendingReport.getLocationLongitude());

                //Returns the approximate distance in meters between the current location and the given report location.
                distance = verify_location.distanceTo(report_locations);

                if(distance <= limit_distance){
                    if(FirebaseDatabaseManager.isWithinTimeRange(clickedReportBasis.getDateTime(), pendingReport.getDateTime())) {
                        if (pendingReport.getReportType().equals(clickedReportBasis.getReportType())) {
                            getImageList().add(pendingReport.getImageURL());
                            getMergedReportsID().add(pendingReport.getReportID());
                            FirebaseDatabaseManager.FirebaseReports.child(pendingReport.getReportID()).child("reportStatus").setValue("Verified");
                        }
                    }
                }
            }

            FirebaseReports.child(TabNotifAdapter.getClickedItem().getReportID()).child("reportStatus").setValue("Verified");

            finish();
            startActivity(new Intent(VerifyReport.this, Sidebar_HomePage.class));
            return;
        }

        if(v==btnDeclineReport) {
            Toast.makeText(VerifyReport.this, "Report declined.", Toast.LENGTH_SHORT).show();
            FirebaseReports.child(Tab2_Location.pendingReport.getReportID()).child("reportStatus").setValue("Declined");
            finish();
            startActivity(new Intent(VerifyReport.this, Sidebar_HomePage.class));
            return;
        }
    }
  
    public void verifyReport() {
        String dateTime = clickedReportBasis.getDateTime();
        String description = clickedReportBasis.getDescription();
        String imageThumbnailURL = clickedReportBasis.getImageURL();
        List<String> imageList = getImageList();
        String location = clickedReportBasis.getLocation();
        double locationLatitude = clickedReportBasis.getLocationLatitude();
        double locationLongitude = clickedReportBasis.getLocationLongitude();
        String reportStatus = "Active";
        String reportType = clickedReportBasis.getReportType();
        String reportedBy = SessionManager.getUserID();
        List<String> mergedReportsID = getMergedReportsID();

        reportVerifiedModel = new ReportVerifiedModel(dateTime, description, imageList, imageThumbnailURL, location, locationLatitude, locationLongitude, mergedReportsID, reportStatus, reportType, reportedBy);

        newReportVerified = FirebaseDatabaseManager.FirebaseReportsVerified.push();
        newReportVerified.setValue(reportVerifiedModel);

        Toast.makeText(VerifyReport.this, "Report verified!.", Toast.LENGTH_LONG).show();
        finish();
        startActivity(new Intent(VerifyReport.this, Sidebar_HomePage.class));
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public List<String> getMergedReportsID() {
        return mergedReportsID;
    }

    public void setMergedReportsID(List<String> mergedReportsID) {
        this.mergedReportsID = mergedReportsID;
    }
}
