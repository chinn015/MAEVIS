package com.user.maevis;

import android.*;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
//import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.user.maevis.models.FirebaseDatabaseManager;
import com.user.maevis.models.PageNavigationManager;
import com.user.maevis.session.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UpdateHomeAddress extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMyLocationChangeListener{

    static boolean updateHomeAddressStatus = false;
    private GoogleMap mMap;
    private GPSTracker gpsTracker;
    private Location mUserLocation;
    static double userLatitude, userLongitude;
    static String userHomeAddress;
    static double userHomeLat, userHomeLong;
    Marker mVisitingMarker;
    FloatingActionButton btnHomeLoc, btnUserLoc;
    Bitmap homeMarkerRed;
    MarkerOptions homeNewmarker = null;
    ArrayList<Marker> markers = new ArrayList<>();
    static String userName, userPassword, userEmail, userFname, userLname, userConPassword;
    static String userLocAddress;
    MarkerOptions userNewmarker = null;
    Bitmap userMarker;
    LatLng user_location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_home_address);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnHomeLoc = (FloatingActionButton) findViewById(R.id.btnHomeLocation1);
        btnUserLoc = (FloatingActionButton) findViewById(R.id.btnUserLocation1);

        showDialogInstructions();

        gpsTracker = new GPSTracker(getApplicationContext().getApplicationContext());

        mUserLocation = gpsTracker.getLocation();

        if(mUserLocation != null) {
            userLatitude = mUserLocation.getLatitude();
            userLongitude = mUserLocation.getLongitude();
        }else{
            userLatitude = 10.316590;
            userLongitude = 123.897093;
            //showDialogGPS();
            Toast.makeText(getApplicationContext(), "Location not found.",
                    Toast.LENGTH_LONG).show();
        }

        user_location = new LatLng(userLatitude, userLongitude);
        userLocAddress = getUserLocAddress(userLatitude, userLongitude);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(this);
        final LatLng home_location;

        BitmapDrawable bitmapUser = (BitmapDrawable)getResources().getDrawable(R.mipmap.ic_marker_user);
        Bitmap user = bitmapUser.getBitmap();

        BitmapDrawable bitmapOldHome = (BitmapDrawable)getResources().getDrawable(R.mipmap.ic_marker_old_home);
        Bitmap oldHome = bitmapOldHome.getBitmap();
        Bitmap homeMarkerGray;

        BitmapDrawable bitmapNewHome = (BitmapDrawable)getResources().getDrawable(R.mipmap.ic_marker_home);
        Bitmap newHome = bitmapNewHome.getBitmap();
        //homeMarkerRed = Bitmap.createScaledBitmap(newHome, 160, 160, false);


        if(Integer.valueOf(android.os.Build.VERSION.SDK) < 24) {
            userMarker = Bitmap.createScaledBitmap(user, 170, 170, false);
            homeMarkerRed = Bitmap.createScaledBitmap(newHome, 170, 170, false);
            homeMarkerGray = Bitmap.createScaledBitmap(oldHome, 170, 170, false);

        }else{
            userMarker = Bitmap.createScaledBitmap(user, 255, 255, false);
            homeMarkerRed = Bitmap.createScaledBitmap(newHome, 255, 255, false);
            homeMarkerGray = Bitmap.createScaledBitmap(oldHome, 255, 255, false);

        }

         /*set marker for user's location*/
        if(mUserLocation == null){
            user_location = new LatLng(userLatitude, userLongitude);
            mMap.addMarker(new MarkerOptions().position(user_location).visible(true).alpha(1.0f).title("Cebu City").icon(BitmapDescriptorFactory.fromBitmap(userMarker)));
        }
        /*else{
            user_location = new LatLng(userLatitude, userLongitude);
            mMap.addMarker(new MarkerOptions().position(user_location).visible(true).alpha(1.0f).title("My Location").icon(BitmapDescriptorFactory.fromBitmap(userMarker)));
        }*/

        getData();

        home_location = new LatLng(SessionManager.getHomeLat(), SessionManager.getHomeLong());
        //home_location = getHomeAddress(SessionManager.getAddress());
        mMap.addMarker(new MarkerOptions().position(home_location).visible(true).alpha(1.0f).title("Old Home Address").icon(BitmapDescriptorFactory.fromBitmap(homeMarkerGray)));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(user_location, 17), 5000, null);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapClickListener(this);

        btnHomeLoc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CameraPosition cameraPosition = new CameraPosition.Builder().target(home_location).zoom(17).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

    }

    @Override
    public void onMarkerDragStart(Marker arg0) {
        Log.d("System out", "onMarkerDragStart..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onMarkerDragEnd(Marker arg0) {
        Log.d("System out", "onMarkerDragEnd..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
        userHomeAddress = getUserHomeLocAddress(arg0.getPosition().latitude, arg0.getPosition().longitude);
        userHomeLat = arg0.getPosition().latitude;
        userHomeLong = arg0.getPosition().longitude;
        Toast.makeText(this, userHomeAddress, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onMarkerDrag(Marker arg0) {
        Log.i("System out", "onMarkerDrag...");
    }

    @Override
    public void onMapClick(LatLng point) {
        Marker temp;

        homeNewmarker = new MarkerOptions().position(
                new LatLng(point.latitude, point.longitude)).title("New Home Address").icon(BitmapDescriptorFactory.fromBitmap(homeMarkerRed)).draggable(true);
        markers.add(mMap.addMarker(homeNewmarker));

        if(markers.size() != 1) {
            for (int x = 0; x < markers.size()-1; x++) {
                markers.get(x).setVisible(false);
            }
        }
        temp = markers.get(markers.size() - 1);

        userHomeAddress = getUserHomeLocAddress(temp.getPosition().latitude, temp.getPosition().longitude);
        userHomeLat = temp.getPosition().latitude;
        userHomeLong = temp.getPosition().longitude;
        Toast.makeText(this, userHomeAddress, Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), "marker " + markers.size() + (markers.size() - 1)  , Toast.LENGTH_LONG).show();
        // System.out.println(point.latitude+"---"+ point.longitude);
    }

    public String getUserHomeLocAddress (double userLatitude, double userLongitude){
        String fullAddress = "No Location Found.";
        Geocoder geocoder;
        List<Address> addresses;

        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(userLatitude, userLongitude, 1);
            fullAddress = addresses.get(0).getAddressLine(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fullAddress;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(SessionManager.isLoggedIn()) {
            if (id == R.id.action_update_address) {
                Toast.makeText(getApplicationContext(), "Update successful!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, SidebarSettings.class);
                startActivity(i);
                finish();
                return true;
            }
        }else{
            if (id == R.id.action_update_address) {
                Toast.makeText(getApplicationContext(), "Update successful!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, SignUp.class));
                finish();
                return true;
            }
        }


        return super.onOptionsItemSelected(item);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_update_address, menu);
        return true;
    }

    public void getData(){
        Intent in = getIntent();

        userName = in.getStringExtra("userName");
        userFname = in.getStringExtra("userFname");
        userLname = in.getStringExtra("userLname");
        userPassword = in.getStringExtra("userPassword");
        userEmail = in.getStringExtra("userEmail");
        userConPassword = in.getStringExtra("userConPassword");


        //Toast.makeText(this, "get : " + userName, Toast.LENGTH_LONG).show();

    }

    private void showDialogInstructions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        builder.setTitle("Update Home Address");
        builder.setMessage("Locate your home address by tapping any area on the map. You can drag and hold the Home Marker to adjust.");
        builder.setInverseBackgroundForced(true);
        builder.setNegativeButton("Return", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
    }

    @Override
    public void onMyLocationChange(Location location) {
        /*
        CameraUpdate myLoc = CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder().target(new LatLng(location.getLatitude(),
                        location.getLongitude())).zoom(17).build());

        mMap.moveCamera(myLoc);

        */

        userLatitude = location.getLatitude();
        userLongitude = location.getLongitude();

        userNewmarker = new MarkerOptions().position(
                new LatLng(location.getLatitude(), location.getLongitude())).title("Current Location").icon(BitmapDescriptorFactory.fromBitmap(userMarker));
        markers.add(mMap.addMarker(userNewmarker));

        if(markers.size() != 1) {
            for (int x = 0; x < markers.size()-1; x++) {
                markers.get(x).setVisible(false);
            }
        }

        btnUserLoc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "My Location: " + userLocAddress,
                        Toast.LENGTH_LONG).show();
                CameraPosition cameraPosition = new CameraPosition.Builder().target(user_location).zoom(17).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });



        //Toast.makeText(getContext(), "location changed", Toast.LENGTH_LONG).show();

    }

    public String getUserLocAddress (double userLatitude, double userLongitude){
        String fullAddress = "No Location Found.";
        Geocoder geocoder;
        List<Address> addresses;

        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(userLatitude, userLongitude, 1);
            fullAddress = addresses.get(0).getAddressLine(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fullAddress;
    }
}
