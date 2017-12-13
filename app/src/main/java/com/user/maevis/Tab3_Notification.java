package com.user.maevis;

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
import com.google.firebase.database.ValueEventListener;
import com.user.maevis.session.SessionManager;

import java.util.ArrayList;
import java.util.List;


public class Tab3_Notification extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListNotif> listItems;
    RecyclerView.LayoutManager layoutManager;
    //private static final String URL_DATA="https://simplifiedcoding.net/demos/marvel/";
    private static final String URL_DATA="https://maevis-ecd17.firebaseio.com/Reports";
    private DatabaseReference FirebaseReports;
    //static int noOfReports;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_home, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        listItems = new ArrayList<>();

        FirebaseReports = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maevis-ecd17.firebaseio.com/Reports");

        countReports();
        loadRecyclerViewData();

        return rootView;
    }

    private void loadRecyclerViewData() {
        FirebaseReports.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                ListNotif item = new ListNotif(dataSnapshot.child("ReportedBy").getValue().toString() + " reported a " +
                        dataSnapshot.child("ReportType").getValue().toString() + " at " +
                        dataSnapshot.child("Location").getValue().toString(),
                        dataSnapshot.child("DateTime").getValue().toString());
                listItems.add(item);

                adapter = new TabNotifAdapter(listItems, getContext());
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

    }

    public void countReports() {
        DatabaseReference fbDb = null;
        if (fbDb == null) {
            fbDb = FirebaseDatabase.getInstance().getReference();
        }

        fbDb.child("Reports").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get total number of reports
                int noOfReports =  (int) dataSnapshot.getChildrenCount();
                Sidebar_HomePage.badge.updateTabBadge(noOfReports);
                Toast.makeText(getActivity(), "no of reports : " + noOfReports, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
