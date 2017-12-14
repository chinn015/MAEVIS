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
    /*
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayAdapter adapter;
    private DatabaseReference FirebaseReports;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recyclerViewReports);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        FirebaseReports = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maevis-ecd17.firebaseio.com/Reports");

        FirebaseReports.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> reports = dataSnapshot.getChildren().iterator();
                Toast.makeText(getActivity(), "Total Number of Users: "+dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
                while(reports.hasNext()) {
                    DataSnapshot report = reports.next();
                    ArrayList<HashMap<String, String>> allReports = new ArrayList<HashMap<String, String>>();
                    HashMap<String, String> reportItem = new HashMap<String, String>();
                    reportItem.put("DateTime", report.child("DateTime").getValue().toString());
                    reportItem.put("Description", report.child("Description").getValue().toString());
                    reportItem.put("Location", report.child("Location").getValue().toString());
                    reportItem.put("LocationLatitude", report.child("LocationLatitude").getValue().toString());
                    reportItem.put("LocationLongitude", report.child("LocationLongitude").getValue().toString());
                    reportItem.put("ReportType", report.child("ReportType").getValue().toString());
                    reportItem.put("ReportedBy", report.child("ReportedBy").getValue().toString());
                    allReports.add(reportItem);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }*/

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    //RecyclerView.LayoutManager layoutManager;
    private LinearLayoutManager layoutManager;

    //private static final String URL_DATA="https://simplifiedcoding.net/demos/marvel/";
    private static final String URL_DATA="https://maevis-ecd17.firebaseio.com/Reports";
    private DatabaseReference FirebaseReports;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_home, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        //recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        listItems = new ArrayList<>();

        FirebaseReports = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maevis-ecd17.firebaseio.com/Reports");

        /*for(int i = 0; i <= 10; i++){
            ListItem listItem = new ListItem("heading " + i, "Lorem ipsum dummyasdassdassdasdasssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
            listItems.add(listItem);
        }


        adapter = new TabHomeAdapter(listItems, this.getActivity());
        recyclerView.setAdapter(adapter);*/

        loadRecyclerViewData();

        return rootView;
    }

    private void loadRecyclerViewData() {
        FirebaseReports.orderByChild("DateTime").addChildEventListener(new ChildEventListener() {
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

                /*String hour = "";
                String min = "";
                String period = "";*/

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

                /*if(time.length()==10) {
                    hour = time.substring(0,1);
                    min = time.substring(2,4);
                    period = time.substring(8,time.length());
                } else if(time.length()==11){
                    hour = time.substring(0,2);
                    min = time.substring(3,5);
                    period = time.substring(9, time.length());
                }*/

                String formatDateTime = hour+":"+min+" "+period+" - "+month+" "+day+" "+year;

                ListItem item = new ListItem(dataSnapshot.child("reportedBy").getValue().toString() +
                        " reported a " + dataSnapshot.child("reportType").getValue().toString() ,
                        dataSnapshot.child("description").getValue().toString(),
                        dataSnapshot.child("dateTime").getValue().toString(),
                        dataSnapshot.child("imageURL").getValue().toString(),
                        formatDateTime);

                listItems.add(item);

                Collections.sort(listItems, new Comparator<ListItem>() {
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