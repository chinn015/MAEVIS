package com.user.maevis;

import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.user.maevis.models.FirebaseDatabaseManager;
import com.user.maevis.session.SessionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Tab3_Notification extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    private List<ListItemVerified> listItemsVerified;
    private LinearLayoutManager layoutManager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_home, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        listItems = new ArrayList<>();
        listItemsVerified = new ArrayList<>();

        loadRecyclerViewData();

        return rootView;
    }

    private void loadRecyclerViewData() {
        FirebaseDatabaseManager.FirebaseReports.orderByChild("dateTime").addChildEventListener(new ChildEventListener() {
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
                        fullName + " reported a " +
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

                float distance, limit_distance;

                limit_distance = 1000;
                Location report_locations = new Location("1");
                Location current_location = new Location("2");

                report_locations.setLatitude(item.getLocationLatitude());
                report_locations.setLongitude(item.getLocationLongitude());

                current_location.setLatitude(Tab2_Location.userLatitude);
                current_location.setLongitude(Tab2_Location.userLongitude);

                distance = current_location.distanceTo(report_locations);

                if( SessionManager.getUserType().equals("Admin") && distance <= limit_distance) {
                    //Toast.makeText(getContext(), FirebaseDatabaseManager.getFullName(item.getReportedBy()) + "Inside: " + distance, Toast.LENGTH_LONG).show();
                    Log.d("Inside Notif: ", FirebaseDatabaseManager.getFullName(item.getReportedBy()));
                    listItems.add(item);

                    adapter = new TabNotifAdapter(listItems, getContext());
                    recyclerView.setAdapter(adapter);
                }
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

        FirebaseDatabaseManager.FirebaseReportsVerified.orderByChild("dateTime").addChildEventListener(new ChildEventListener() {
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

                //retrieve userPhoto
                String userPhotoImgUrl = FirebaseDatabaseManager.getUserPhoto(dataSnapshot.child("reportedBy").getValue().toString());

                List<String> imageList = new ArrayList<>();
                Iterator<DataSnapshot> images = dataSnapshot.child("imageList").getChildren().iterator();
                while(images.hasNext()) {
                    DataSnapshot image = images.next();
                    imageList.add(image.getValue().toString());
                }

                List<String> mergedReportsID = new ArrayList<>();
                Iterator<DataSnapshot> reports = dataSnapshot.child("imageList").getChildren().iterator();
                while(reports.hasNext()) {
                    DataSnapshot report = reports.next();
                    imageList.add(report.getValue().toString());
                }

                Map<String, Boolean> stars = new HashMap<>();
                Iterator<DataSnapshot> starList = dataSnapshot.child("stars").getChildren().iterator();
                while(starList.hasNext()){
                    DataSnapshot star = starList.next();
                    stars.put(star.getKey(), (Boolean) star.getValue());
                }

                ListItemVerified itemVerified = new ListItemVerified(dataSnapshot.getKey().toString(),
                        fullName+" reported a " +
                                dataSnapshot.child("reportType").getValue().toString() + " at " +
                                dataSnapshot.child("location").getValue().toString(),
                        dataSnapshot.child("dateTime").getValue().toString(),
                        dataSnapshot.child("description").getValue().toString(),
                        imageList,
                        dataSnapshot.child("imageThumbnailURL").getValue().toString(),
                        dataSnapshot.child("location").getValue().toString(),
                        locationLatitude,
                        locationLongitude,
                        mergedReportsID,
                        dataSnapshot.child("reportStatus").getValue().toString(),
                        dataSnapshot.child("reportType").getValue().toString(),
                        dataSnapshot.child("reportedBy").getValue().toString(),
                        formatDateTime,
                        userPhotoImgUrl,
                        Integer.valueOf(dataSnapshot.child("starCount").getValue().toString()),
                        stars);

                float distance, limit_distance;

                limit_distance = 1000;
                Location report_locations = new Location("1");
                Location current_location = new Location("2");

                report_locations.setLatitude(itemVerified.getLocationLatitude());
                report_locations.setLongitude(itemVerified.getLocationLongitude());

                current_location.setLatitude(Tab2_Location.userLatitude);
                current_location.setLongitude(Tab2_Location.userLongitude);

                distance = current_location.distanceTo(report_locations);

                if( SessionManager.getUserType().equals("Regular User") && distance <= limit_distance) {
                    //Toast.makeText(getContext(), FirebaseDatabaseManager.getFullName(item.getReportedBy()) + "Inside: " + distance, Toast.LENGTH_LONG).show();
                    Log.d("Inside Notif: ", FirebaseDatabaseManager.getFullName(itemVerified.getReportedBy()));
                    listItemsVerified.add(itemVerified);

                    adapter = new TabNotifAdapterRegUser(listItemsVerified, getContext());
                    recyclerView.setAdapter(adapter);
                }
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
