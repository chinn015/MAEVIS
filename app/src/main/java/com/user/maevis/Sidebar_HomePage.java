package com.user.maevis;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.user.maevis.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class Sidebar_HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    TabLayout tabLayout;
    private FragmentManager fragmentManager;
    private Fragment fragment = null;
    static FloatingActionButton btnAddReport, btnHomeLoc, btnUserLoc;
    private ViewPager viewPager;
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
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        Toast.makeText(Sidebar_HomePage.this, "Logged in "+ SessionManager.isLoggedIn()+" as: "+ SessionManager.getFirstName()+" "+ SessionManager.getLastName(), Toast.LENGTH_LONG).show();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        btnHomeLoc = (FloatingActionButton) findViewById(R.id.btnHomeLocation);
        btnUserLoc = (FloatingActionButton) findViewById(R.id.btnUserLocation);
        btnAddReport = (FloatingActionButton) findViewById(R.id.fab);

        btnAddReport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplication(), SelectionPage.class);
                startActivity(i);
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0: toolbar.setTitle("Home");
                        break;
                    case 1: toolbar.setTitle("Location");
                        break;
                    case 2: toolbar.setTitle("Notification");
                        break;
                    case 3: toolbar.setTitle("Search");
                        break;
                }

                if (position == 1) {
                    btnHomeLoc.show();
                    btnUserLoc.show();
                } else {
                    btnHomeLoc.hide();
                    btnUserLoc.hide();
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    btnHomeLoc.show();
                    btnUserLoc.show();
                } else {
                    btnHomeLoc.hide();
                    btnUserLoc.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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
            case R.id.nav_home :
                i = new Intent(Sidebar_HomePage.this, Sidebar_HomePage.class);
                startActivity(i);
                break;

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
                i = new Intent(Sidebar_HomePage.this, SidebarShare.class);
                startActivity(i);
                break;

            case R.id.nav_logout :
                i = new Intent(Sidebar_HomePage.this, SidebarShare.class);
                startActivity(i);
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

