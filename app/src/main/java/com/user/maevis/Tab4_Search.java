package com.user.maevis;

import android.app.SearchManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.user.maevis.controllers.cNotification;
import com.user.maevis.models.FirebaseDatabaseManager;
import com.user.maevis.models.ReportModel;
import com.user.maevis.models.UserModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Tab4_Search extends Fragment {
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
        setHasOptionsMenu(true);
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

                List<String> imageList = new ArrayList<>();
                Iterator<DataSnapshot> images = dataSnapshot.child("imageList").getChildren().iterator();
                while (images.hasNext()) {
                    DataSnapshot image = images.next();
                    imageList.add(image.getValue().toString());
                }

                List<String> mergedReportsID = new ArrayList<>();
                Iterator<DataSnapshot> reports = dataSnapshot.child("imageList").getChildren().iterator();
                while (reports.hasNext()) {
                    DataSnapshot report = reports.next();
                    imageList.add(report.getValue().toString());
                }

                ListItemVerified itemVerified = new ListItemVerified(dataSnapshot.getKey().toString(),
                        fullName + " reported a " +
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
                        formatDateTime);

                //add all Active reports to a List to be displayed
                switch (itemVerified.getReportStatus()) {
                    case "Active":
                        FirebaseDatabaseManager.getActiveVerifiedReports().add(itemVerified);
                        listItemsVerified.add(itemVerified);
                        break;
                    case "Done":
                        FirebaseDatabaseManager.getDoneVerifiedReports().add(itemVerified);
                        break;

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
                        formatDateTime);

                //add all Verified reports to a List to be displayed
                if (item.getReportStatus().equals("Verified")) {
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
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);

        try {
            // Associate searchable configuration with the SearchView
            SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getActivity().getComponentName())
            );

            searchView.setQueryHint("Search Location");
            searchView.setIconified(false);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String s) {
                    // do your search

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    // do your search on change or save the last string or...
                    return false;
                }
            });


        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
