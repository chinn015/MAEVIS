package com.user.maevis;

import android.support.v4.app.Fragment;
import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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

    private  RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    RecyclerView.LayoutManager layoutManager;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_home, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        listItems = new ArrayList<>();

        for(int i = 0; i <= 10; i++){
                ListItem listItem = new ListItem("heading " + i, "Lorem ipsum dummyasdassdassdasdasssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
                listItems.add(listItem);
        }


        adapter = new MyAdapter(listItems, this.getActivity());
        recyclerView.setAdapter(adapter);

        return rootView;
    }
}
