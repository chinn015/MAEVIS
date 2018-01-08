package com.user.maevis;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.user.maevis.controllers.cNotification;
import com.user.maevis.models.FirebaseDatabaseManager;
import com.user.maevis.models.UserModel;
import com.user.maevis.session.SessionManager;

import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Tab2_Location extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private GPSTracker gpsTracker;
    private Location mUserLocation;
    static double userLatitude, userLongitude;
    static ListItem verifiedReport;
    static ListItem pendingReport;
    static ListItemVerified activeVerifiedReport;
    static String userLocAddress;

    static List<String> markerReportIDs;
    static List<String> markerPendingReportIDs;
    static List<String> markerActiveVerifiedReportIDs;

    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab2_location, container, false);

        gpsTracker = new GPSTracker(getActivity().getApplicationContext());

        mUserLocation = gpsTracker.getLocation();

        if(mUserLocation != null) {
            userLatitude = mUserLocation.getLatitude();
            userLongitude = mUserLocation.getLongitude();
        }else{
            userLatitude = 10.316590;
            userLongitude = 123.897093;
            Toast.makeText(getActivity(), "Location not found.",
                    Toast.LENGTH_LONG).show();
        }

        userLocAddress = getUserLocAddress(userLatitude, userLongitude);
        markerReportIDs = new ArrayList<>();
        markerPendingReportIDs = new ArrayList<>();
        markerActiveVerifiedReportIDs = new ArrayList<>();

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupportMapFragment fragment = new SupportMapFragment();
        transaction.add(R.id.map, fragment);
        transaction.commit();

        fragment.getMapAsync(this);
        return view;
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final LatLng user_location;
        final LatLng home_location;
        BitmapDrawable bitmapUser, bitmapHome;
        BitmapDrawable[] bitmapReports = new BitmapDrawable[6];
        Bitmap[] reports = new Bitmap[6];
        Bitmap[] reportMarker = new Bitmap[6];
        int[] reportIcons = {
                R.drawable.ic_marker_fire,
                R.drawable.ic_marker_flood,
                R.drawable.ic_marker_accident,
                R.drawable.ic_marker__verified_fire,
                R.drawable.ic_marker_verified_flood,
                R.drawable.ic_marker_verified_accident
        };

        bitmapUser = (BitmapDrawable)getResources().getDrawable(R.drawable.ic_marker_user);
        Bitmap user = bitmapUser.getBitmap();
        Bitmap userMarker = Bitmap.createScaledBitmap(user, 160, 160, false);

        bitmapHome = (BitmapDrawable)getResources().getDrawable(R.drawable.ic_marker_home);
        Bitmap home = bitmapHome.getBitmap();
        Bitmap homeMarker = Bitmap.createScaledBitmap(home, 160, 160, false);


        for (int x = 0; x < 6; x++){
            bitmapReports[x] = (BitmapDrawable)getResources().getDrawable(reportIcons[x]);
            reports[x] = bitmapReports[x].getBitmap();
            reportMarker[x] = Bitmap.createScaledBitmap(reports[x], 150, 150, false);
        }

        /*set marker for user's location*/
        if(mUserLocation == null){
            user_location = new LatLng(userLatitude, userLongitude);
            mMap.addMarker(new MarkerOptions().position(user_location).visible(true).alpha(1.0f).title("Cebu City").icon(BitmapDescriptorFactory.fromBitmap(userMarker)));
        }else{
            user_location = new LatLng(userLatitude, userLongitude);
            mMap.addMarker(new MarkerOptions().position(user_location).visible(true).alpha(1.0f).title("My Location").icon(BitmapDescriptorFactory.fromBitmap(userMarker)));
        }

         /*set marker for home location*/
        home_location = new LatLng(10.316590, 123.897093);
       // home_location = getHomeAddress(SessionManager.getAddress());
        mMap.addMarker(new MarkerOptions().position(home_location).visible(true).alpha(0.8f).title("Home Location").icon(BitmapDescriptorFactory.fromBitmap(homeMarker)));

        mMap.setOnMarkerClickListener(this);

        //Display location markers for activeVerifiedReports (officially validated) for Regular Users and Admins
        for (int x = 0; x < FirebaseDatabaseManager.getActiveVerifiedReports().size(); x++) {
            //verifiedReport = FirebaseDatabaseManager.getVerifiedReports().get(x);
            activeVerifiedReport = FirebaseDatabaseManager.getActiveVerifiedReports().get(x);
            double vLatitude = activeVerifiedReport.getLocationLatitude();
            double vLongitude = activeVerifiedReport.getLocationLongitude();
            float distance, limit_distance;

            limit_distance = 1000;
            Location report_locations = new Location("1");
            Location current_location = new Location("2");
            String vTitle = activeVerifiedReport.getReportType() + " " + FirebaseDatabaseManager.getFullName(activeVerifiedReport.getReportedBy());

            switch (activeVerifiedReport.getReportType()) {
                case "Fire":
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(vLatitude, vLongitude))
                            .visible(true).alpha(1.0f).title(vTitle + ": " + vLatitude + " - " + vLongitude)
                            .icon(BitmapDescriptorFactory.fromBitmap(reportMarker[3])));
                    break;

                case "Flood":
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(vLatitude, vLongitude))
                            .visible(true).alpha(1.0f).title(vTitle + ": " + vLatitude + " - " + vLongitude)
                            .icon(BitmapDescriptorFactory.fromBitmap(reportMarker[4])));
                    break;

                case "Vehicular Accident":
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(vLatitude, vLongitude))
                            .visible(true).alpha(1.0f).title(vTitle + ": " + vLatitude + " - " + vLongitude)
                            .icon(BitmapDescriptorFactory.fromBitmap(reportMarker[5])));
                    break;
            }

            markerReportIDs.add(activeVerifiedReport.getReportID());

            if(!FirebaseDatabaseManager.getActiveVerifiedReports().isEmpty()) {
                Log.d("REPORTS", FirebaseDatabaseManager.getActiveVerifiedReports().get(0).getDescription());
            } else {
                Log.d("REPORTS", "WAY SUD!!!");
            }


            report_locations.setLatitude(vLatitude);
            report_locations.setLongitude(vLongitude);

            current_location.setLatitude(userLatitude);
            current_location.setLongitude(userLongitude);

            //Returns the approximate distance in meters between the current location and the given report location.
            distance = current_location.distanceTo(report_locations);

            if (distance <= limit_distance) {
                //Toast.makeText(getContext(), FirebaseDatabaseManager.getFullName(activeVerifiedReport.getReportedBy()) + "Inside: " + distance, Toast.LENGTH_LONG).show();
                Log.d("Inside: ", FirebaseDatabaseManager.getFullName(activeVerifiedReport.getReportedBy()));
                cNotification.showActiveNotification(getContext(), activeVerifiedReport);
                cNotification.vibrateNotif(getContext());
            } else {
               // Toast.makeText(getContext(), FirebaseDatabaseManager.getFullName(activeVerifiedReport.getReportedBy()) + "Outside: " + distance, Toast.LENGTH_LONG).show();
                Log.d("Outside: ", FirebaseDatabaseManager.getFullName(activeVerifiedReport.getReportedBy()));
            }
        }

        Log.d("username", SessionManager.getUsername());
        Log.d("address", SessionManager.getAddress());

        //Display location markers for pendingReports to be validated by the admin
        if(SessionManager.getUserType().equals("Admin")) {
            for (int x = 0; x < FirebaseDatabaseManager.getPendingReports().size(); x++) {
                pendingReport = FirebaseDatabaseManager.getPendingReports().get(x);
                double vLatitude = pendingReport.getLocationLatitude();
                double vLongitude = pendingReport.getLocationLongitude();
                float distance, limit_distance;

                limit_distance = 1000;
                Location report_locations = new Location("1");
                Location current_location = new Location("2");
                String vTitle = pendingReport.getReportType() + " " + FirebaseDatabaseManager.getFullName(pendingReport.getReportedBy());

                switch (pendingReport.getReportType()) {
                    case "Fire":
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(vLatitude, vLongitude))
                                .visible(true).alpha(1.0f).title(vTitle + ": " + vLatitude + " - " + vLongitude)
                                .icon(BitmapDescriptorFactory.fromBitmap(reportMarker[0])));
                        break;

                    case "Flood":
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(vLatitude, vLongitude))
                                .visible(true).alpha(1.0f).title(vTitle + ": " + vLatitude + " - " + vLongitude)
                                .icon(BitmapDescriptorFactory.fromBitmap(reportMarker[1])));
                        break;

                    case "Vehicular Accident":
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(vLatitude, vLongitude))
                                .visible(true).alpha(1.0f).title(vTitle + ": " + vLatitude + " - " + vLongitude)
                                .icon(BitmapDescriptorFactory.fromBitmap(reportMarker[2])));
                        break;
                }

                markerReportIDs.add(pendingReport.getReportID());

                report_locations.setLatitude(vLatitude);
                report_locations.setLongitude(vLongitude);

                current_location.setLatitude(userLatitude);
                current_location.setLongitude(userLongitude);

                //Returns the approximate distance in meters between the current location and the given report location.
                distance = current_location.distanceTo(report_locations);

                if (distance <= limit_distance) {
                   // Toast.makeText(getContext(), FirebaseDatabaseManager.getFullName(pendingReport.getReportedBy()) + "Inside: " + distance, Toast.LENGTH_LONG).show();
                    Log.d("Inside: ", FirebaseDatabaseManager.getFullName(pendingReport.getReportedBy()));
                    cNotification.showPendingNotification(getContext(), pendingReport);
                    cNotification.vibrateNotif(getContext());
                } else {
                   // Toast.makeText(getContext(), FirebaseDatabaseManager.getFullName(pendingReport.getReportedBy()) + "Outside: " + distance, Toast.LENGTH_LONG).show();
                    Log.d("Outside: ", FirebaseDatabaseManager.getFullName(pendingReport.getReportedBy()));
                }
            }


        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(user_location, 17), 5000, null);

        Sidebar_HomePage.btnUserLoc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "My Location",
                        Toast.LENGTH_LONG).show();
                CameraPosition cameraPosition = new CameraPosition.Builder().target(user_location).zoom(17).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });

        Sidebar_HomePage.btnHomeLoc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Home Location : " + SessionManager.getAddress(),
                        Toast.LENGTH_LONG).show();
                CameraPosition cameraPosition = new CameraPosition.Builder().target(home_location).zoom(17).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });


    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String markerId;
        Intent i;
        int id;
        boolean status=false;

        markerId = marker.getId().replaceAll("[^\\d.]", "");
        id = Integer.parseInt(markerId) - 2;


        if( id != -1 && id != -2){
            switch(SessionManager.getUserType()) {
                case "Regular User":
                    Toast.makeText(getContext(), "Reg User MARKER ID: " + id +" "+ FirebaseDatabaseManager.getPendingReports().size(), Toast.LENGTH_LONG).show();

                    activeVerifiedReport = FirebaseDatabaseManager.getActiveVerifiedReport(markerReportIDs.get(id));

                    i = new Intent(getContext(), ReportPage.class);
                    startActivity(i);
                    break;
                case "Admin":
                    if(FirebaseDatabaseManager.isInActiveVerifiedReports(markerReportIDs.get(id))) {
                        //Toast.makeText(getContext(), "Admin AVR MARKER ID: " + id +" "+ FirebaseDatabaseManager.getActiveVerifiedReports().size(), Toast.LENGTH_LONG).show();
                        activeVerifiedReport = FirebaseDatabaseManager.getActiveVerifiedReport(markerReportIDs.get(id));
                        i = new Intent(getContext(), ReportPage.class);
                        startActivity(i);
                    } else {
                        //Toast.makeText(getContext(), "Admin PR MARKER ID: " + id +" "+ FirebaseDatabaseManager.getPendingReports().size(), Toast.LENGTH_LONG).show();
                        pendingReport = FirebaseDatabaseManager.getPendingReport(markerReportIDs.get(id));
                        i = new Intent(getContext(), VerifyReport.class);
                        startActivity(i);
                    }
                    break;
            }
        }else if( id == 0 ) {
            if (marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
            } else {
                marker.showInfoWindow();
            }
        }

        return true;
    }

    public static double getUserLatitude() {
        return userLatitude;
    }

    public static double getUserLongitude() {
        return userLongitude;
    }

    public LatLng getHomeAddress(String homeAddress){
        Geocoder coder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> address;
        Address location;
        LatLng latLng = new LatLng(10.316590, 123.897093);

        try {
            //Get latLng from String
            address = coder.getFromLocationName(homeAddress,1);

            if(address != null || !address.isEmpty()) {
                location = address.get(0);
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
            }else{
                latLng = new LatLng(10.316590, 123.897093);
            }

            for(int x = 0; x < address.size(); x++){
                Log.d("address", address.get(x).toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return latLng;
    }

    public String getUserLocAddress (double userLatitude, double userLongitude){
        String fullAddress = "No Location Found.";
        Geocoder geocoder;
        List<Address> addresses;

        geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(userLatitude, userLongitude, 1);
            fullAddress = addresses.get(0).getAddressLine(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fullAddress;
    }

}