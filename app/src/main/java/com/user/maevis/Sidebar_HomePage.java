package com.user.maevis;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.user.maevis.models.FirebaseDatabaseManager;
import com.user.maevis.models.PageNavigationManager;
import com.user.maevis.session.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Sidebar_HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    TabLayout tabLayout;
    private FragmentManager fragmentManager;
    private Fragment fragment = null;
    static FloatingActionButton btnAddReport, btnHomeLoc, btnUserLoc;
    private ViewPager viewPager;
    TextView profileName;
    static TabNotifBadge badge;
    static int noOfReports;
    private GPSTracker gpsTracker;
    private Location mUserLocation;
    static String userAddress;
    CircleImageView profilePic;
    TextView user_location;

    private int[] tabIcons = {
            R.drawable.ic_home_black_24dp,
            R.drawable.ic_my_location_black_24dp,
            R.drawable.ic_notifications_none_black_24dp,
            R.drawable.ic_search_black_24dp

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidebar_homepage);

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        viewPager = (ViewPager) findViewById(R.id.simpleViewPager);
        viewPager.setOffscreenPageLimit(1);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        btnHomeLoc = (FloatingActionButton) findViewById(R.id.btnHomeLocation);
        btnUserLoc = (FloatingActionButton) findViewById(R.id.btnUserLocation);
        btnAddReport = (FloatingActionButton) findViewById(R.id.btnAddReport);

        btnAddReport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(SessionManager.getUserStatus().equals("Active")) {
                    Intent i = new Intent(getApplication(), SelectionPage.class);
                    startActivity(i);
                } else if(SessionManager.getUserStatus().equals("Blocked")) {
                    showDialogBlocked();
                }
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        profileName = (TextView) headerView.findViewById(R.id.txtViewProfileName);
        profileName.setText(SessionManager.getFirstName()+" "+ SessionManager.getLastName());
        profilePic = (CircleImageView) headerView.findViewById(R.id.user_photo);
        Picasso.with(this).load(SessionManager.getUserPhoto()).into(profilePic);
        user_location = (TextView) headerView.findViewById(R.id.txtViewUserLocation);
        user_location.setText(SessionManager.getAddress());

        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(!enabled) {
            showDialogGPS();
        }else{
            gpsTracker = new GPSTracker(getApplicationContext());
            mUserLocation = gpsTracker.getLocation();
            userAddress = getUserLocAddress(mUserLocation.getLatitude(), mUserLocation.getLongitude());
            //Toast.makeText(this, "Location: " + userAddress, Toast.LENGTH_LONG).show();
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0: toolbar.setTitle("Home");
                        btnHomeLoc.hide();
                        btnUserLoc.hide();
                        break;

                    case 1: toolbar.setTitle("Location");
                        btnHomeLoc.show();
                        btnUserLoc.show();
                        break;
                    case 2: toolbar.setTitle("Notification");
                        btnHomeLoc.hide();
                        btnUserLoc.hide();
                        break;
                    case 3: toolbar.setTitle("Search");
                        btnHomeLoc.hide();
                        btnUserLoc.hide();
                        break;
                }

            }

            @Override
            public void onPageSelected(int position) {
                switch(position) {
                    case 0:
                        Fragment newFragment = new Tab1_Home();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.homeLayout, newFragment);
                        transaction.commit();
                        btnHomeLoc.hide();
                        btnUserLoc.hide();
                        onStart();
                        break;

                    case 1:

                        Fragment newFragment1 = new Tab2_Location();
                        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                        transaction1.replace(R.id.map, newFragment1);
                        transaction1.commit();
                        btnHomeLoc.show();
                        btnUserLoc.show();
                        break;

                    case 2:
                        btnHomeLoc.hide();
                        btnUserLoc.hide();
                        break;

                    case 3:
                        btnHomeLoc.hide();
                        btnUserLoc.hide();
                        break;


                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //listener to store all users from the Firebase Database to a List
        FirebaseDatabaseManager.FirebaseUsers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                UserItem user = new UserItem(dataSnapshot.getKey().toString(),
                        dataSnapshot.child("address").getValue().toString(),
                        dataSnapshot.child("birthdate").getValue().toString(),
                        FirebaseDatabaseManager.parseLongToDouble(dataSnapshot.child("currentLat").getValue()),
                        FirebaseDatabaseManager.parseLongToDouble(dataSnapshot.child("currentLong").getValue()),
                        dataSnapshot.child("deviceToken").getValue().toString(),
                        dataSnapshot.child("email").getValue().toString(),
                        dataSnapshot.child("firstName").getValue().toString(),
                        FirebaseDatabaseManager.parseLongToDouble(dataSnapshot.child("homeLat").getValue()),
                        FirebaseDatabaseManager.parseLongToDouble(dataSnapshot.child("homeLong").getValue()),
                        dataSnapshot.child("lastName").getValue().toString(),
                        dataSnapshot.child("userPhoto").getValue().toString(),
                        dataSnapshot.child("userStatus").getValue().toString(),
                        dataSnapshot.child("userType").getValue().toString(),
                        dataSnapshot.child("username").getValue().toString());

                FirebaseDatabaseManager.getUserItems().add(user);
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

        //listener to store all reports from the Firebase Database to a List
        FirebaseDatabaseManager.FirebaseReports.orderByChild("dateTime").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String reportDateTime = dataSnapshot.child("dateTime").getValue().toString();

                //format date from (yyyy-mm-dd hh:mm:ss A) to (hh:mm A - MMM-dd-yyyy)
                String formatDateTime = FirebaseDatabaseManager.formatDate(reportDateTime);

                //parse Long to Double for Latitude and Longitude values
                double locationLatitude=1.5;
                double locationLongitude=1.5;
                locationLatitude = FirebaseDatabaseManager.parseLongToDouble(dataSnapshot.child("locationLatitude").getValue());
                locationLongitude = FirebaseDatabaseManager.parseLongToDouble(dataSnapshot.child("locationLongitude").getValue());

                //retrieve full name
                String fullName = FirebaseDatabaseManager.getFullName(dataSnapshot.child("reportedBy").getValue().toString());
                String userPhotoImgUrl = FirebaseDatabaseManager.getUserPhoto(dataSnapshot.child("reportedBy").getValue().toString());

                ListItem item = new ListItem(dataSnapshot.getKey().toString(),
                        fullName+" reported a " +
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
                        formatDateTime,
                        userPhotoImgUrl);

                FirebaseDatabaseManager.getListItems().add(item);

                switch(item.getReportStatus()) {
                    case "Verified": FirebaseDatabaseManager.getVerifiedReports().add(item); break;
                    case "Pending": FirebaseDatabaseManager.getPendingReports().add(item); break;
                }
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

    private void showDialogBlocked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Sidebar_HomePage.this);
        builder.setCancelable(false);
        builder.setTitle("Account Blocked");
        builder.setMessage("Your account has been blocked. \nYou cannot send any reports for now.");
        builder.setInverseBackgroundForced(true);

        builder.setNegativeButton("Return", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        alert.getButton(alert.BUTTON_POSITIVE).setTextColor(Color.BLACK);
    }

    public String getUserLocAddress (double userLatitude, double userLongitude){
        String fullAddress = "No Location Found.";
        Geocoder geocoder;
        List<Address> addresses;

        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(userLatitude, userLongitude, 1);
            fullAddress = addresses.get(0).getAddressLine(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fullAddress;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1_Home(), "ONE");
        adapter.addFragment(new Tab2_Location(), "TWO");
        adapter.addFragment(new Tab3_Notification(), "THREE");
        adapter.addFragment(new Tab4_Search(), "FOUR");

        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);

        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);

        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        //tabLayout.getTabAt(2).setCustomView(R.layout.tab_badge);
        //badge = new TabNotifBadge(this, tabLayout.getTabAt(2).getCustomView().findViewById(R.id.tab_badge));
        //countReports();

        /*
        badge.updateTabBadge(7);
        badge.updateTabBadge(Tab3_Notification.noOfReports);
        */

        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(3).getIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) tab.setCustomView(R.layout.custom_tab);
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            //returns total number of pages
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //  return mFragmentTitleList.get(position);
            return null;
        }
    }

    private void initInstancesDrawer() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout); // get the reference of TabLayout
        TabLayout.Tab [] tabs = new TabLayout.Tab[4];

        for(int i = 0; i < tabs.length; i++){
            tabs[i] = tabLayout.newTab();
            tabs[i].setIcon(tabIcons[i]);
            tabLayout.addTab(tabs[i]);
            tabLayout.getTabAt(i).getIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        }

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sidebar, menu);
        //getMenuInflater().inflate(R.menu.menu_search, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent i;
        FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentManager.beginTransaction().replace(R.id.content_frame, new Tab1_Home()).commit();

        switch (id){
            /*
            case R.id.nav_home :
                i = new Intent(Sidebar_HomePage.this, Sidebar_HomePage.class);
                startActivity(i);
                break;
                */

            case R.id.nav_profile :
                i = new Intent(Sidebar_HomePage.this, SidebarProfile.class);
                startActivity(i);
                break;

            case R.id.nav_settings :
                i = new Intent(Sidebar_HomePage.this, SidebarSettings.class);
                startActivity(i);
                break;

            case R.id.nav_help :
                i = new Intent(Sidebar_HomePage.this, SidebarHelp.class);
                startActivity(i);
                break;

            case R.id.nav_share :
                i = new Intent(Sidebar_HomePage.this, SidebarHotline.class);
                startActivity(i);
                break;

            case R.id.nav_logout :
                SessionManager.clearSession();
                SessionManager.getFirebaseAuth().signOut();
                Toast.makeText(Sidebar_HomePage.this, "Logged out.", Toast.LENGTH_LONG).show();

                i = new Intent(Sidebar_HomePage.this, LoadingScreeen.class);
                startActivity(i);
                Sidebar_HomePage.this.finish();
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void countReports() {
        DatabaseReference dataRef = null;
        if (dataRef == null) {
            dataRef = FirebaseDatabase.getInstance().getReference();
        }

        dataRef.child("Reports").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get total number of reports
                noOfReports =  (int) dataSnapshot.getChildrenCount();
                Sidebar_HomePage.badge.updateTabBadge(noOfReports);
                //Toast.makeText(getApplication(), "no of reports : " + noOfReports, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * Show a dialog to the user requesting that GPS be enabled
     */
    private void showDialogGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Enable Location");
        builder.setMessage("Your Location Settings is set to OFF. Please enable Location to use this app.");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(
                        new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        alert.getButton(alert.BUTTON_POSITIVE).setTextColor(Color.BLACK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Intent refresh = new Intent(this, Sidebar_HomePage.class);
            startActivity(refresh);
            this.finish();
        }
    }

}
