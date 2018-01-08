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
import com.user.maevis.models.ReportModel;
import com.user.maevis.models.ReportVerifiedModel;
import com.user.maevis.session.SessionManager;

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
    private List<String> imageList;
    private List<String> mergedReportsID;
    ListItem clickedReportBasis;

    public static ReportVerifiedModel reportVerifiedModel;
    public static DatabaseReference newReportVerified;

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
        viewNotifHead.setText(TabNotifAdapter.getClickedItem().getHead());
        viewNotifDesc.setText(TabNotifAdapter.getClickedItem().getDescription());
        viewNotifDateTime.setText(TabNotifAdapter.getClickedItem().getDisplayDateTime());
        Picasso.with(getApplicationContext())
                .load(TabNotifAdapter.getClickedItem().getImageURL())
                .fit()
                .into(viewNotifImage);

        Picasso.with(getApplicationContext())
                .load(ListItem.getReportTypeImage((TabNotifAdapter.getClickedItem().getReportType())))
                .into(viewNotifReportType);


        btnVerifyReport = (Button) findViewById(R.id.btnVerifyReport);
        btnDeclineReport = (Button) findViewById(R.id.btnDeclineReport);

        btnVerifyReport.setOnClickListener(this);
        btnDeclineReport.setOnClickListener(this);

        FirebaseReports = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maevis-ecd17.firebaseio.com/Reports");

        listItems = new ArrayList<>();
        imageList = new ArrayList<>();
        mergedReportsID = new ArrayList<>();

        FirebaseReports.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String reportDateTime = dataSnapshot.child("dateTime").getValue().toString();

                //format date from (yyyy-mm-dd hh:mm:ss A) to (hh:mm A - MMM-dd-yyyy)
                String formatDateTime = FirebaseDatabaseManager.formatDate(reportDateTime);

                //parse Long to Double for Latitude and Longitude values
                double locationLatitude = 0.000;
                double locationLongitude = 0.0000;
                locationLatitude = FirebaseDatabaseManager.parseLongToDouble(dataSnapshot.child("locationLatitude").getValue());
                locationLongitude = FirebaseDatabaseManager.parseLongToDouble(dataSnapshot.child("locationLongitude").getValue());

                //retrieve full name
                String fullName = FirebaseDatabaseManager.getFullName(dataSnapshot.child("reportedBy").getValue().toString());

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
                        dataSnapshot.child("mergedTo").getValue().toString(),
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
            //Toast.makeText(VerifyReport.this, "Report verified.", Toast.LENGTH_SHORT).show();
            clickedReportBasis = TabNotifAdapter.getClickedItem();
            //FirebaseReports.child(TabNotifAdapter.getClickedItem().getReportID()).child("reportStatus").setValue("Verified");

            for(int x = 0; x < FirebaseDatabaseManager.getVerifiedReports().size(); x++) {
                ListItem verifiedReport = FirebaseDatabaseManager.getVerifiedReports().get(x);
                double vLatitude = verifiedReport.getLocationLatitude();
                double vLongitude = verifiedReport.getLocationLongitude();
                float distance, limit_distance;

                limit_distance = 300;
                Location report_locations = new Location("1");
                Location verify_location = new Location("2");
                String vTitle = verifiedReport.getReportType()+" "+FirebaseDatabaseManager.getFullName(verifiedReport.getReportedBy());

                report_locations.setLatitude(vLatitude);
                report_locations.setLongitude(vLongitude);

                verify_location.setLatitude(verifiedReport.getLocationLatitude());
                verify_location.setLongitude(verifiedReport.getLocationLongitude());

                //Returns the approximate distance in meters between the current location and the given report location.
                distance = verify_location.distanceTo(report_locations);

                if(distance <= limit_distance){
                    if(FirebaseDatabaseManager.isWithinTimeRange(clickedReportBasis.getDateTime(), verifiedReport.getDateTime())) {
                        if (verifiedReport.getReportType().equals(clickedReportBasis.getReportType())) {
                            imageList.add(verifiedReport.getImageURL());
                            mergedReportsID.add(verifiedReport.getReportID());
                            FirebaseReports.child(verifiedReport.getReportID()).child("reportStatus").setValue("Verified");
                        }
                    }
                }
            }

            verifyReport();
            //FirebaseReports.child(TabNotifAdapter.getClickedItem().getReportID()).child("reportStatus").setValue("Verified");

            finish();
            startActivity(new Intent(VerifyReport.this, Sidebar_HomePage.class));
            return;
        }

        if(v==btnDeclineReport) {
            Toast.makeText(VerifyReport.this, "Report declined.", Toast.LENGTH_SHORT).show();
            FirebaseReports.child(TabNotifAdapter.getClickedItem().getReportID()).child("reportStatus").setValue("Declined");
            finish();
            startActivity(new Intent(VerifyReport.this, Sidebar_HomePage.class));
            return;
        }
    }

    public void verifyReport() {
        String dateTime = clickedReportBasis.getDateTime();
        String description = clickedReportBasis.getDescription();
        String imageThumbnailURL = clickedReportBasis.getImageURL();
        List<String> imageList = this.imageList;
        String location = clickedReportBasis.getLocation();
        double locationLatitude = clickedReportBasis.getLocationLatitude();
        double locationLongitude = clickedReportBasis.getLocationLongitude();
        String reportStatus = "Active";
        String reportType = clickedReportBasis.getReportType();
        String reportedBy = SessionManager.getUserID();
        List<String> mergedReportsID = this.mergedReportsID;

        reportVerifiedModel = new ReportVerifiedModel(dateTime, description, imageList, imageThumbnailURL, location, locationLatitude, locationLongitude, mergedReportsID, reportStatus, reportType, reportedBy);

        newReportVerified = FirebaseDatabaseManager.FirebaseReportsVerified.push();
        newReportVerified.setValue(reportVerifiedModel);

        Toast.makeText(VerifyReport.this, "Report verified!.", Toast.LENGTH_LONG).show();
        finish();
        startActivity(new Intent(VerifyReport.this, Sidebar_HomePage.class));
    }
}
