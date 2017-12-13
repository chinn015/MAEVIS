package com.user.maevis;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.widget.Toast;


public class Tab2_Location extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GPSTracker gpsTracker;
    private Location mUserLocation;
    static double userLatitude, userLongitude;
    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab2_location, container, false);

        gpsTracker = new GPSTracker(getActivity().getApplicationContext());

        mUserLocation = gpsTracker.getLocation();

        if(mUserLocation != null) {
            userLatitude = mUserLocation.getLatitude();
            userLongitude = mUserLocation.getLongitude();
        }else{
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
        BitmapDrawable bitmapReport, bitmapUser, bitmapHome;

        bitmapReport = (BitmapDrawable)getResources().getDrawable(R.drawable.ic_accident_marker);
        Bitmap report = bitmapReport.getBitmap();
        Bitmap reportMarker = Bitmap.createScaledBitmap(report, 200, 200, false);

        bitmapUser = (BitmapDrawable)getResources().getDrawable(R.drawable.ic_user_marker);
        Bitmap user = bitmapUser.getBitmap();
        Bitmap userMarker = Bitmap.createScaledBitmap(user, 259, 250, false);


        mMap.addMarker(new MarkerOptions().position(new LatLng(10.315000, 123.888899)).visible(true).alpha(0.8f).title("Location1").icon(BitmapDescriptorFactory.fromBitmap(reportMarker)));
        mMap.addMarker(new MarkerOptions().position(new LatLng(10.317000, 123.90)).visible(true).alpha(0.8f).title("Location2").icon(BitmapDescriptorFactory.fromBitmap(reportMarker)));

        if(mUserLocation == null){
            user_location = new LatLng(10.316590, 123.897093);
            mMap.addMarker(new MarkerOptions().position(user_location).visible(true).alpha(0.8f).title("Cebu City").icon(BitmapDescriptorFactory.fromBitmap(userMarker)));
        }else{
            user_location = new LatLng(userLatitude, userLongitude);
            mMap.addMarker(new MarkerOptions().position(user_location).visible(true).alpha(0.8f).title("My Location").icon(BitmapDescriptorFactory.fromBitmap(userMarker)));
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(user_location, 17), 5000, null);

        Sidebar_HomePage.btnUserLoc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Current Location",
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

    public static double getUserLatitude() {
        return userLatitude;
    }

    public static double getUserLongitude() {
        return userLongitude;
    }
}