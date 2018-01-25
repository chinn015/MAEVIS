/*
 * Copyright (C) 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.user.maevis;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.user.maevis.session.SessionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SidebarProfileMedia extends Fragment {
	private RecyclerView recyclerView;
	private RecyclerView.Adapter adapter;
	private List<ListItem> listItems;
	private LinearLayoutManager layoutManager;

	private DatabaseReference FirebaseReports;
	private DatabaseReference FirebaseUsers;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.sidebar_profile_media, container, false);
		recyclerView = (RecyclerView) rootView.findViewById(R.id.media_recycler_view);
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

//				ListItem item = new ListItem(dataSnapshot.getKey().toString(),
//						fullName + " reported a " +
//								dataSnapshot.child("reportType").getValue().toString() + " at " +
//								dataSnapshot.child("location").getValue().toString(),
//						dataSnapshot.child("dateTime").getValue().toString(),
//						dataSnapshot.child("description").getValue().toString(),
//						dataSnapshot.child("imageURL").getValue().toString(),
//						dataSnapshot.child("location").getValue().toString(),
//						locationLatitude,
//						locationLongitude,
//						dataSnapshot.child("mergedTo").getValue().toString(),
//						dataSnapshot.child("reportStatus").getValue().toString(),
//						dataSnapshot.child("reportType").getValue().toString(),
//						dataSnapshot.child("reportedBy").getValue().toString(),
//						formatDateTime);
//
//				if (item.getReportedBy().equals(SessionManager.getUserID())) {
//					listItems.add(item);
//				}

				Collections.sort(listItems, new Comparator<ListItem>() {
					@Override
					public int compare(ListItem o1, ListItem o2) {
						if (o1.getDateTime() == null || o2.getDateTime() == null) {
							return 0;
						}
						return o1.getDateTime().compareTo(o2.getDateTime());
					}
				});

				adapter = new TabMediaAdapter(listItems, getContext());
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
