package com.user.maevis;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.user.maevis.models.FirebaseDatabaseManager;

import android.widget.Toast;

import static android.R.attr.radius;
import static android.R.attr.start;


public class Tab2_Location extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private GPSTracker gpsTracker;
    private Location mUserLocation;
    static double userLatitude, userLongitude;
    static ListItem verifiedReport;
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
        BitmapDrawable bitmapUser, bitmapHome;
        BitmapDrawable[] bitmapReports = new BitmapDrawable[3];
        Bitmap[] reports = new Bitmap[3];
        Bitmap[] reportMarker = new Bitmap[3];
        int[] reportIcons = {
                R.drawable.ic_marker_fire,
                R.drawable.ic_marker_flood,
                R.drawable.ic_marker_accident
        };

        bitmapUser = (BitmapDrawable)getResources().getDrawable(R.drawable.ic_marker_user);
        Bitmap user = bitmapUser.getBitmap();
        Bitmap userMarker = Bitmap.createScaledBitmap(user, 160, 160, false);

        for (int x = 0; x < 3; x++){
            bitmapReports[x] = (BitmapDrawable)getResources().getDrawable(reportIcons[x]);
            reports[x] = bitmapReports[x].getBitmap();
            reportMarker[x] = Bitmap.createScaledBitmap(reports[x], 150, 150, false);
        }

        // bitmapReport = (BitmapDrawable)getResources().getDrawable(R.drawable.ic_marker_fire);
        //Bitmap report = bitmapReport.getBitmap();
        //Bitmap reportMarker = Bitmap.createScaledBitmap(report, 150, 150, false);

        //mMap.addMarker(new MarkerOptions().position(new LatLng(10.315000, 123.888899)).visible(true).alpha(0.8f).title("Location1").icon(BitmapDescriptorFactory.fromBitmap(reportMarker)));
        //mMap.addMarker(new MarkerOptions().position(new LatLng(10.317000, 123.900000)).visible(true).alpha(0.8f).title("Location2").icon(BitmapDescriptorFactory.fromBitmap(reportMarker)));
        //mMap.addMarker(new MarkerOptions().position(new LatLng(10.3171367, 123.9125538)).visible(true).alpha(0.8f).title("Static - Location3").icon(BitmapDescriptorFactory.fromBitmap(reportMarker)));

        /*set marker for user's location*/
        if(mUserLocation == null){
            user_location = new LatLng(userLatitude, userLongitude);
            mMap.addMarker(new MarkerOptions().position(user_location).visible(true).alpha(0.8f).title("Cebu City").icon(BitmapDescriptorFactory.fromBitmap(userMarker)));
        }else{
            user_location = new LatLng(userLatitude, userLongitude);
            mMap.addMarker(new MarkerOptions().position(user_location).visible(true).alpha(0.8f).title("My Location").icon(BitmapDescriptorFactory.fromBitmap(userMarker)));
        }

//        Circle circle =  mMap.addCircle(new CircleOptions()
//                .center(user_location)
//                .radius(1000)
//                .strokeColor(Color.parseColor("#8c6b1913"))
//                .fillColor(Color.parseColor("#5089534f")));

        mMap.setOnMarkerClickListener(this);

        for(int x=0; x < FirebaseDatabaseManager.getVerifiedReports().size(); x++) {
            verifiedReport = FirebaseDatabaseManager.getVerifiedReports().get(x);
            double vLatitude = verifiedReport.getLocationLatitude();
            double vLongitude = verifiedReport.getLocationLongitude();
            float distance, limit_distance;

            limit_distance = 1000;
            Location report_locations = new Location("1");
            Location current_location = new Location("2");
            String vTitle = verifiedReport.getReportType()+" "+FirebaseDatabaseManager.getFullName(verifiedReport.getReportedBy());

            switch (verifiedReport.getReportType()) {
                case "Fire":
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(vLatitude, vLongitude))
                            .visible(true).alpha(0.8f).title(vTitle+": "+vLatitude+" - "+vLongitude)
                            .icon(BitmapDescriptorFactory.fromBitmap(reportMarker[0])));
                    break;

                case "Flood":
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(vLatitude, vLongitude))
                            .visible(true).alpha(0.8f).title(vTitle+": "+vLatitude+" - "+vLongitude)
                            .icon(BitmapDescriptorFactory.fromBitmap(reportMarker[1])));
                    break;

                case "Vehicular Accident":
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(vLatitude, vLongitude))
                            .visible(true).alpha(0.8f).title(vTitle+": "+vLatitude+" - "+vLongitude)
                            .icon(BitmapDescriptorFactory.fromBitmap(reportMarker[2])));
                    break;
            }

            report_locations.setLatitude(vLatitude);
            report_locations.setLongitude(vLongitude);

            current_location.setLatitude(userLatitude);
            current_location.setLongitude(userLongitude);

            //Returns the approximate distance in meters between the current location and the given report location.
            distance = current_location.distanceTo(report_locations);

            if(distance <= limit_distance){
                Toast.makeText(getContext(), "Inside " + distance, Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getContext(), "Outside " + distance, Toast.LENGTH_LONG).show();
            }

        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(user_location, 17), 5000, null);

        Sidebar_HomePage.btnUserLoc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "LatLng: "+ getUserLatitude() +" "+ getUserLongitude(),
                        Toast.LENGTH_LONG).show();
                CameraPosition cameraPosition = new CameraPosition.Builder().target(user_location).zoom(17).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });

        Sidebar_HomePage.btnHomeLoc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Home Location",
                        Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Toast.makeText(getContext(), marker.getTitle(),Toast.LENGTH_LONG).show();
        marker.getId();
        Intent i;
        i = new Intent(getContext(), ReportPage.class);
        startActivity(i);
        return true;

    }

    public static double getUserLatitude() {
        return userLatitude;
    }

    public static double getUserLongitude() {
        return userLongitude;
    }
}