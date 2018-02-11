package com.user.maevis;

import android.graphics.Color;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.os.Bundle;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.user.maevis.models.FirebaseDatabaseManager;
import com.user.maevis.models.ReportModel;
import com.user.maevis.session.SessionManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Tab1_Home extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    private List<ListItemVerified> listItemsVerified;
    private LinearLayoutManager layoutManager;

    private DatabaseReference FirebaseReports;
    private DatabaseReference FirebaseUsers;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_home, container, false);
        rootView.setBackgroundColor(Color.WHITE);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        listItems = new ArrayList<>();
        listItemsVerified = new ArrayList<>();

        FirebaseUsers = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maevis-ecd17.firebaseio.com/Users");
        FirebaseReports = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maevis-ecd17.firebaseio.com/Reports");

        loadRecyclerViewData();

        return rootView;
    }

    private void loadRecyclerViewData() {
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

                //add all Active reports to a List to be displayed
                switch(itemVerified.getReportStatus()) {
                    case "Active": FirebaseDatabaseManager.getActiveVerifiedReports().add(itemVerified);
                                   listItemsVerified.add(itemVerified);
                                   break;
                    case "Done": FirebaseDatabaseManager.getDoneVerifiedReports().add(itemVerified); break;

                }

                Collections.sort(listItemsVerified, new Comparator<ListItemVerified>() {
                    @Override
                    public int compare(ListItemVerified o1, ListItemVerified o2) {
                        if (o1.getDateTime() == null || o2.getDateTime() == null) {
                            return 0;
                        }
                        return o1.getDateTime().compareTo(o2.getDateTime());
                    }
                });

                adapter = new TabHomeAdapter(listItemsVerified, getContext());
                recyclerView.setAdapter(adapter);
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
                        fullName+" reported a " +
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

                //add all Verified reports to a List to be displayed
                if(item.getReportStatus().equals("Verified")) {
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

                /*adapter = new TabHomeAdapter(listItems, getContext());
                recyclerView.setAdapter(adapter);*/
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

        /*FirebaseReports.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Iterator<DataSnapshot> reports = dataSnapshot.getChildren().iterator();
                //while(reports.hasNext()) {
                    //DataSnapshot report = reports.next();

                    ListItem item = new ListItem(dataSnapshot.child("ReportedBy").getValue().toString() +
                            " reported a " + dataSnapshot.child("ReportType").getValue().toString() ,
                            dataSnapshot.child("Description").getValue().toString(),
                            dataSnapshot.child("DateTime").getValue().toString(),
                            dataSnapshot.child("ImageURL").getValue().toString());
                    listItems.add(item);


                //}

                adapter = new TabHomeAdapter(listItems, getContext());
                recyclerView.setAdapter(adapter);
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
        });*/


        /*final ProgressDialog progressDialog = new ProgressDialog(this.getActivity());
        progressDialog.setMessage("Timeline is loading.");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            //JSONObject jsonObject = new JSONObject(response);
                            //JSONArray array = jsonObject.getJSONArray("");
                            JSONArray array = new JSONArray(response);

                            for(int i=0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                ListItem item = new ListItem(
                                        o.getString("ReportedBy"),
                                        o.getString("Description")
                                        //o.getString("imageurl")
                                );
                                listItems.add(item);
                            }

                            adapter = new TabHomeAdapter(listItems, getContext());
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this.getActivity());
        requestQueue.add(stringRequest);*/
    }
}





//UPDATE DATA CODE
/*
    if(dataSnapshot.hasChild("reportDescription")) {
        String dateTime = dataSnapshot.child("dateTime").getValue().toString();
        String description = dataSnapshot.child("reportDescription").getValue().toString();
        String imageURL = dataSnapshot.child("imageURL").getValue().toString();
        String location = dataSnapshot.child("location").getValue().toString();

        double locationLatitude = 10.0000;
        Object locLat = dataSnapshot.child("locationLatitude").getValue();
        if (locLat instanceof Long) {
            locationLatitude = ((Long) locLat).doubleValue();
        } else {
            locationLatitude = (double) dataSnapshot.child("locationLatitude").getValue();
        }

        double locationLongitude = 120.0000;
        Object locLong = dataSnapshot.child("locationLongitude").getValue();
        if (locLong instanceof Long) {
            locationLongitude = ((Long) locLong).doubleValue();
        } else {
            locationLongitude = (double) dataSnapshot.child("locationLongitude").getValue();
        }

        String reportType = dataSnapshot.child("reportType").getValue().toString();
        String reportedBy = dataSnapshot.child("reportedBy").getValue().toString();

        ReportModel reportModel = new ReportModel(dateTime, description, imageURL, location, locationLatitude, locationLongitude, reportType, reportedBy);

        DatabaseReference newReport = FirebaseReports.push();
        newReport.setValue(reportModel);

        FirebaseReports.child(dataSnapshot.getKey()).removeValue();
    }
*/