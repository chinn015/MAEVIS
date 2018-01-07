package com.user.maevis;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.user.maevis.controllers.FirebaseInstanceIdNotif;
import com.user.maevis.models.FirebaseDatabaseManager;
import com.user.maevis.session.SessionManager;

import java.util.Collections;
import java.util.Comparator;

public class LoadingScreeen extends AppCompatActivity {
    private static final String TAG = LoadingScreeen.class.getSimpleName();
    private final int SPLASH_DISPLAY_LENGTH = 5000;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        ActionBar actionBar = getSupportActionBar();
        if(null != actionBar){
            actionBar.hide();
        }
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent startActivityIntent = new Intent(LoadingScreeen.this, Login.class);
                startActivity(startActivityIntent);
                LoadingScreeen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

//        broadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Log.d("token", SessionManager.getKeyDeviceToken());
//
//            }
//        };
//
//        registerReceiver(broadcastReceiver, new IntentFilter(FirebaseInstanceIdNotif.TOKEN_BROADCAST));


    }
}