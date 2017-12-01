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
        });


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
