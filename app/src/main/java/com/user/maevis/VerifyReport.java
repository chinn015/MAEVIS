package com.user.maevis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.user.maevis.models.FirebaseDatabaseManager;
import com.user.maevis.models.PageNavigationManager;
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
    private ImageView imgViewProfilePic;

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
        imgViewProfilePic = (ImageView) findViewById(R.id.imgViewProfilePic);

        imgViewProfilePic.setOnClickListener(this);

        //display details of clicked item
        if(PageNavigationManager.getClickedTabLocListItemPending() != null) {
            viewNotifHead.setText(PageNavigationManager.getClickedTabLocListItemPending().getHead());
            viewNotifDesc.setText(PageNavigationManager.getClickedTabLocListItemPending().getDescription());
            viewNotifDateTime.setText(PageNavigationManager.getClickedTabLocListItemPending().getDisplayDateTime());
            Picasso.with(getApplicationContext())
                    .load(PageNavigationManager.getClickedTabLocListItemPending().getImageURL())
                    .fit()
                    .into(viewNotifImage);

            Picasso.with(getApplicationContext())
                    .load(ListItem.getReportTypeImage((PageNavigationManager.getClickedTabLocListItemPending().getReportType())))
                    .into(viewNotifReportType);
        } else if (PageNavigationManager.getClickedTabNotifListItem() != null) {
            viewNotifHead.setText(PageNavigationManager.getClickedTabNotifListItem().getHead());
            viewNotifDesc.setText(PageNavigationManager.getClickedTabNotifListItem().getDescription());
            viewNotifDateTime.setText(PageNavigationManager.getClickedTabNotifListItem().getDisplayDateTime());
            Picasso.with(getApplicationContext())
                    .load(PageNavigationManager.getClickedTabNotifListItem().getImageURL())
                    .fit()
                    .into(viewNotifImage);

            Picasso.with(getApplicationContext())
                    .load(ListItem.getReportTypeImage((PageNavigationManager.getClickedTabNotifListItem().getReportType())))
                    .into(viewNotifReportType);
        }

        btnVerifyReport = (Button) findViewById(R.id.btnBlockUser);
        btnDeclineReport = (Button) findViewById(R.id.btnProfileUM);

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
            showDialogVerify();
            return;
        }

        if(v==btnDeclineReport) {
            showDialogDecline();
            return;
        }

        if(v==imgViewProfilePic) {
            if(SessionManager.getUserType().equals("Admin")) {
                if (PageNavigationManager.getClickedTabLocListItemPending() != null) {
                    PageNavigationManager.markTab(PageNavigationManager.getClickedTabLocListItemPending().getReportedBy(),
                            PageNavigationManager.KEY_TABLOCPENDING,
                            PageNavigationManager.getClickedTabLocListItemPending());

                    Toast.makeText(this, "[TabLoc] User ID: "+PageNavigationManager.getClickedUserID(), Toast.LENGTH_LONG).show();
                }

                if (PageNavigationManager.getClickedTabNotifListItem() != null) {
                    PageNavigationManager.markTab(PageNavigationManager.getClickedTabNotifListItem().getReportedBy(),
                            PageNavigationManager.KEY_TABNOTIF,
                            PageNavigationManager.getClickedTabNotifListItem());

                    Toast.makeText(this, "[TabNotif] User ID: "+PageNavigationManager.getClickedUserID(), Toast.LENGTH_LONG).show();
                }

                PageNavigationManager.clickVerifyReportUserItem(FirebaseDatabaseManager.getUserItem(PageNavigationManager.getClickedUserID()));
                if(PageNavigationManager.getClickedVerifyReportUserItem() != null) {
                    Intent i = new Intent(this, UserManagement.class);
                    startActivity(i);
                }
            }
        }
    }

    public void verifyReport() {
        clickedReportBasis = PageNavigationManager.getClickedTabLocListItemPending();
        newReportVerified = FirebaseDatabaseManager.FirebaseReportsVerified.push();

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
                        FirebaseDatabaseManager.FirebaseReports.child(pendingReport.getReportID()).child("mergedTo").setValue(newReportVerified.getKey());
                    }
                }
            }
        }

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
        newReportVerified.setValue(reportVerifiedModel);

        Toast.makeText(VerifyReport.this, "Report verified!.", Toast.LENGTH_LONG).show();
        finish();
        startActivity(new Intent(VerifyReport.this, Sidebar_HomePage.class));
    }

    public void declineReport() {
        Toast.makeText(VerifyReport.this, "Report declined.", Toast.LENGTH_SHORT).show();
        FirebaseDatabaseManager.FirebaseReports.child(PageNavigationManager.getClickedTabLocListItemPending().getReportID()).child("reportStatus").setValue("Declined");

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

    private void showDialogVerify() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("VERIFY REPORT");
        builder.setMessage("Confirm verification of report.");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("VERIFY", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                verifyReport();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        alert.getButton(alert.BUTTON_POSITIVE).setTextColor(Color.BLACK);
    }

    private void showDialogDecline() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("DECLINE REPORT");
        builder.setMessage("Decline this report?");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("DECLINE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                declineReport();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        alert.getButton(alert.BUTTON_POSITIVE).setTextColor(Color.BLACK);
    }
}
