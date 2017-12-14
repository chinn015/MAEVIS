package com.user.maevis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class VerifyReport extends AppCompatActivity implements View.OnClickListener {

    private TextView viewNotifHead;
    private TextView viewNotifDesc;
    private TextView viewNotifDateTime;
    private ImageView viewNotifImage;

    private Button btnVerifyReport;
    private Button btnDeclineReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_report);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewNotifHead = (TextView) findViewById(R.id.viewNotifHead);
        viewNotifDesc = (TextView) findViewById(R.id.viewNotifDesc);
        viewNotifDateTime = (TextView) findViewById(R.id.viewNotifDateTime);
        viewNotifImage = (ImageView) findViewById(R.id.viewNotifImage);

        //display details of clicked item
        viewNotifHead.setText(TabNotifAdapter.getClickedItem().getHead());
        viewNotifDesc.setText(TabNotifAdapter.getClickedItem().getDescription());
        viewNotifDateTime.setText(TabNotifAdapter.getClickedItem().getDisplayDateTime());
        Picasso.with(getApplicationContext()).load(TabNotifAdapter.getClickedItem().getImageURL()).into(viewNotifImage);

        btnVerifyReport = (Button) findViewById(R.id.btnVerifyReport);
        btnDeclineReport = (Button) findViewById(R.id.btnDeclineReport);
    }

    @Override
    public void onClick(View v) {
        if(v==btnVerifyReport) {
            return;
        }

        if(v==btnDeclineReport) {
            return;
        }
    }
}
