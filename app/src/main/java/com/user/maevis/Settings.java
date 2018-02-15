package com.user.maevis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by Chen on 2/16/2018.
 */

public class Settings extends AppCompatActivity implements View.OnClickListener {


    private CardView cvAccountSettings;
    private CardView cvChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cvAccountSettings = (CardView) findViewById(R.id.cardViewAccountSettings);
        cvChangePassword = (CardView) findViewById(R.id.cardViewChangePassword);

        cvAccountSettings.setOnClickListener(this);
        cvChangePassword.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if(view == cvAccountSettings){
            startActivity(new Intent(this, SidebarSettings.class));
        }else if (view == cvChangePassword){
            startActivity(new Intent(this, ChangePassword.class));

        }
    }
}