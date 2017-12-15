package com.user.maevis;

import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.os.Bundle;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.List;


public class Tab1_Home extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    private LinearLayoutManager layoutManager;

    private DatabaseReference FirebaseReports;
    private DatabaseReference FirebaseUsers;

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

        FirebaseUsers = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maevis-ecd17.firebaseio.com/Users");
        FirebaseReports = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maevis-ecd17.firebaseio.com/Reports");



        loadRecyclerViewData();

        return rootView;
    }

    private void loadRecyclerViewData() {
        FirebaseReports.orderByChild("dateTime").addChildEventListener(new ChildEventListener() {
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

                String reportedByFN="FirstName";
                String reportedByLN="LastName";

                for(int x=0; x < FirebaseDatabaseManager.getUserItems().size(); x++) {
                    UserItem userItem = FirebaseDatabaseManager.getUserItems().get(x);

                    if(userItem.getUserID().equals(dataSnapshot.child("reportedBy").getValue().toString())) {
                        reportedByFN = userItem.getFirstName();
                        reportedByLN = userItem.getLastName();
                    }
                }

                ListItem item = new ListItem(dataSnapshot.getKey().toString(),
                        reportedByFN+" "+reportedByLN+" reported a " +
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

                /*ListItem item = new ListItem(dataSnapshot.getKey().toString(),
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
                        formatDateTime);*/

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