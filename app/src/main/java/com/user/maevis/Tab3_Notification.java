package com.user.maevis;

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

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_home, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

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
        FirebaseReports.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Iterator<DataSnapshot> reports = dataSnapshot.getChildren().iterator();
                //while(reports.hasNext()) {
                //DataSnapshot report = reports.next();

                ListNotif item = new ListNotif(dataSnapshot.child("ReportedBy").getValue().toString() + " reported a " +
                        dataSnapshot.child("ReportType").getValue().toString() + " at " +
                        dataSnapshot.child("Location").getValue().toString(),
                        dataSnapshot.child("DateTime").getValue().toString());
                listItems.add(item);
                //}

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
}
