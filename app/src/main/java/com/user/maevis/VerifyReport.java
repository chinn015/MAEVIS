package com.user.maevis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.user.maevis.models.FirebaseDatabaseManager;
import com.user.maevis.models.NotifModel;
import com.user.maevis.models.PageNavigationManager;
import com.user.maevis.models.ReportVerifiedModel;
import com.user.maevis.session.SessionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class VerifyReport extends AppCompatActivity implements View.OnClickListener {

    private TextView viewNotifHead;
    private TextView viewNotifDesc;
    private TextView viewNotifDateTime;
    private ImageView viewNotifImage;
    private ImageView viewNotifReportType;
    private CircleImageView imgViewProfilePic;

    private Button btnVerifyReport;
    private Button btnDeclineReport;
    private Button btnResolvedReport;


    private DatabaseReference FirebaseReports;
    private Iterator<DataSnapshot> items;
    private List<ListItem> listItems;
    private List<String> imageList;
    private List<String> mergedReportsID;
    ListItem clickedReportBasis;

    public static ReportVerifiedModel reportVerifiedModel;
    public static DatabaseReference newReportVerified;

    public static NotifModel notifModel;
    public static DatabaseReference newNotif;

    private List<String> nearbyUsers;
    private List<String> nearbyHomes;

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
        imgViewProfilePic = (CircleImageView) findViewById(R.id.imgViewProfilePic);

        imgViewProfilePic.setOnClickListener(this);

        btnVerifyReport = (Button) findViewById(R.id.btnBlockUser);
        btnDeclineReport = (Button) findViewById(R.id.btnProfileUM);
        btnResolvedReport = (Button) findViewById(R.id.btnResolved);


        btnVerifyReport.setOnClickListener(this);
        btnDeclineReport.setOnClickListener(this);
        btnResolvedReport.setOnClickListener(this);

        FirebaseReports = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maevis-ecd17.firebaseio.com/Reports");

        listItems = new ArrayList<>();
        imageList = new ArrayList<>();
        mergedReportsID = new ArrayList<>();
        nearbyUsers = new ArrayList<>();
        nearbyHomes = new ArrayList<>();

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

            Picasso.with(getApplicationContext())
                    .load(PageNavigationManager.getClickedTabLocListItemPending().getUserPhoto())
                    .into(imgViewProfilePic);
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

            Picasso.with(getApplicationContext())
                    .load(PageNavigationManager.getClickedTabNotifListItem().getUserPhoto())
                    .into(imgViewProfilePic);

            if(PageNavigationManager.getClickedTabNotifListItem().getReportStatus().equals("Verified")) {
                btnVerifyReport.setVisibility(View.GONE);
                btnDeclineReport.setVisibility(View.GONE);
                btnResolvedReport.setVisibility(View.VISIBLE);
            }else if(PageNavigationManager.getClickedTabNotifListItem().getReportStatus().equals("Resolved")) {
                btnVerifyReport.setVisibility(View.GONE);
                btnDeclineReport.setVisibility(View.GONE);
                btnResolvedReport.setVisibility(View.GONE);
            }else{
                btnVerifyReport.setVisibility(View.VISIBLE);
                btnDeclineReport.setVisibility(View.VISIBLE);
                btnResolvedReport.setVisibility(View.GONE);
            }
        }

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
                String userPhotoImgUrl = FirebaseDatabaseManager.getUserPhoto(dataSnapshot.child("reportedBy").getValue().toString());

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
                        formatDateTime,
                        userPhotoImgUrl);

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

        if(v==btnResolvedReport) {
            showDialogResolved();
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
        if(PageNavigationManager.getClickedTabLocListItemPending()!=null) {
            clickedReportBasis = PageNavigationManager.getClickedTabLocListItemPending();
        } else if (PageNavigationManager.getClickedTabNotifListItem()!=null) {
            clickedReportBasis = PageNavigationManager.getClickedTabNotifListItem();
        }

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
        //String reportedBy = SessionManager.getUserID();
        String reportedBy = clickedReportBasis.getReportedBy();
        List<String> mergedReportsID = getMergedReportsID();
        int starCount = 0;
        Map<String, Boolean> stars = new HashMap<>();


        reportVerifiedModel = new ReportVerifiedModel(dateTime, description, imageList, imageThumbnailURL, location, locationLatitude, locationLongitude, mergedReportsID, reportStatus, reportType, reportedBy, starCount, stars);
        newReportVerified.setValue(reportVerifiedModel);

        String fullName = FirebaseDatabaseManager.getFullName(reportVerifiedModel.getReportedBy());
        String notifMessageUser = fullName+" reported a "+reportVerifiedModel.getReportType()+" near you.";
        String notifMessageHome = fullName+" reported a "+reportVerifiedModel.getReportType()+" near your home.";
        String notifTitle = "MAEVIS: "+reportVerifiedModel.getReportType()+" Report";
        String notifReportID = newReportVerified.getKey();

        for(int x=0; x<FirebaseDatabaseManager.getUserItems().size(); x++) {
            UserItem nearbyUser = FirebaseDatabaseManager.getUserItems().get(x);
            double nearbyLatitude = nearbyUser.getCurrentLat();
            double nearbyLongitude = nearbyUser.getCurrentLong();
            double nearbyHomeLat = nearbyUser.getHomeLat();
            double nearbyHomeLong = nearbyUser.getHomeLong();
            float nearbyUser_distance, nearbyHome_distance, nearby_distance;

            nearby_distance = 1000;
            Location nearby_users = new Location("1");
            Location verified_report_location = new Location("2");
            Location nearby_homes = new Location("3");
            //String vTitle = pendingReport.getReportType()+" "+FirebaseDatabaseManager.getFullName(pendingReport.getReportedBy());

            nearby_users.setLatitude(nearbyLatitude);
            nearby_users.setLongitude(nearbyLongitude);

            nearby_homes.setLatitude(nearbyHomeLat);
            nearby_homes.setLongitude(nearbyHomeLong);

            verified_report_location.setLatitude(reportVerifiedModel.getLocationLatitude());
            verified_report_location.setLongitude(reportVerifiedModel.getLocationLongitude());

            //Returns the approximate distance in meters between the current location and the given report location.
            nearbyUser_distance = verified_report_location.distanceTo(nearby_users);
            nearbyHome_distance = verified_report_location.distanceTo(nearby_users);

            if(nearbyUser_distance <= nearby_distance){
                nearbyUsers.add(nearbyUser.getUserID());
            }

            if(nearbyHome_distance <= nearby_distance){
                nearbyHomes.add(nearbyUser.getUserID());
            }
        }

        for(int x=0; x < nearbyUsers.size(); x++) {
            String messageToUser = "["+FirebaseDatabaseManager.getFullName(nearbyUsers.get(x))+"] "+fullName+" reported a "+reportVerifiedModel.getReportType()+" near you.";
            notifModel = new NotifModel(messageToUser, notifReportID, notifTitle, nearbyUsers.get(x));
            //notifModel = new NotifModel(notifMessageUser, notifReportID, notifTitle, nearbyUsers.get(x));
            newNotif = FirebaseDatabaseManager.FirebaseNotifications.push();
            newNotif.setValue(notifModel);
        }

        for(int x=0; x < nearbyHomes.size(); x++) {
            String messageToHome = "["+FirebaseDatabaseManager.getFullName(nearbyUsers.get(x))+"'s Home] "+fullName+" reported a "+reportVerifiedModel.getReportType()+" near you.";
            notifModel = new NotifModel(messageToHome, notifReportID, notifTitle, nearbyHomes.get(x));
            //notifModel = new NotifModel(notifMessageHome, notifReportID, notifTitle, nearbyHomes.get(x));
            newNotif = FirebaseDatabaseManager.FirebaseNotifications.push();
            newNotif.setValue(notifModel);
        }

        Toast.makeText(VerifyReport.this, "Report verified!.", Toast.LENGTH_LONG).show();
        finish();
        startActivity(new Intent(VerifyReport.this, Sidebar_HomePage.class));
    }

    public void declineReport() {
        if(PageNavigationManager.getClickedTabLocListItemPending()!=null) {
            clickedReportBasis = PageNavigationManager.getClickedTabLocListItemPending();
        } else if (PageNavigationManager.getClickedTabNotifListItem()!=null) {
            clickedReportBasis = PageNavigationManager.getClickedTabNotifListItem();
        }

        Toast.makeText(VerifyReport.this, "Report declined.", Toast.LENGTH_SHORT).show();
        FirebaseDatabaseManager.FirebaseReports.child(clickedReportBasis.getReportID()).child("reportStatus").setValue("Declined");

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
        Log.d("V0 Session First Name: ", SessionManager.getFirstName());
        Log.d("V0 Session Last Name: ", SessionManager.getLastName());
        Log.d("V0 Session User Type: ", SessionManager.getUserType());

        for(int x=0; x<FirebaseDatabaseManager.getUserItems().size(); x++) {
            Log.d("User Item ["+x+"] First Name: ", FirebaseDatabaseManager.getUserItems().get(x).getFirstName());
            Log.d("User Item ["+x+"] Last Name: ", FirebaseDatabaseManager.getUserItems().get(x).getLastName());
        }

        for(int x=0; x<FirebaseDatabaseManager.getPendingReports().size(); x++) {
            Log.d("Pending Report Item ["+x+"] Desc: ", FirebaseDatabaseManager.getPendingReports().get(x).getDescription());
            Log.d("Pending Report Item ["+x+"] Type: ", FirebaseDatabaseManager.getPendingReports().get(x).getReportType());
        }

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

    private void showDialogResolved() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("RESOLVE REPORT");
        builder.setMessage("Resolve this report?");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("RESOLVE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                resolveReport();
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

    public void resolveReport() {
        if(PageNavigationManager.getClickedTabLocListItemPending()!=null) {
            clickedReportBasis = PageNavigationManager.getClickedTabLocListItemPending();
        } else if (PageNavigationManager.getClickedTabNotifListItem()!=null) {
            clickedReportBasis = PageNavigationManager.getClickedTabNotifListItem();
        }

        Toast.makeText(VerifyReport.this, "Report resolved.", Toast.LENGTH_SHORT).show();
        FirebaseDatabaseManager.FirebaseReports.child(clickedReportBasis.getReportID()).child("reportStatus").setValue("Resolved");
        for(int x =0; x < FirebaseDatabaseManager.getActiveVerifiedReports().size(); x++){
            Log.d("active", (FirebaseDatabaseManager.getActiveVerifiedReports().get(x).getDescription()));
            Log.d("verfied", (clickedReportBasis.getDescription()));


            if(FirebaseDatabaseManager.isWithinTimeRange(FirebaseDatabaseManager.getActiveVerifiedReports().get(x).getDateTime(),clickedReportBasis.getDateTime())){
               //FirebaseDatabaseManager.FirebaseReportsVerified.child(FirebaseDatabaseManager.getActiveVerifiedReports().get(x).getReportID()).child("reportStatus").setValue("Resolved");
                FirebaseDatabaseManager.FirebaseReportsVerified.child(FirebaseDatabaseManager.getActiveVerifiedReports().get(x).getReportID()).removeValue();

            }
        }

        finish();
        startActivity(new Intent(VerifyReport.this, Sidebar_HomePage.class));
    }
}
