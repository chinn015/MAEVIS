package com.user.maevis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

public class SelectionPage extends Activity implements View.OnClickListener {

    ImageButton btnAccident, btnFlood, btnFire;
    public static String reportType="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_selection_page);

        btnFlood = (ImageButton) findViewById(R.id.btnFlood);
        btnFire = (ImageButton) findViewById(R.id.btnFire);
        btnAccident = (ImageButton) findViewById(R.id.btnAccident);

        /*btnFlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(SelectionPage.this, "It works!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(SelectionPage.this, UploadReport.class);
                startActivity(i);
            }
        });

        btnFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(SelectionPage.this, "It works!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(SelectionPage.this, UploadReport.class);
                startActivity(i);
            }
        });

        btnAccident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(SelectionPage.this, "It works!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(SelectionPage.this, UploadReport.class);
                startActivity(i);
            }
        });*/
        btnFlood.setOnClickListener(this);
        btnFire.setOnClickListener(this);
        btnAccident.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==btnFlood) {
            //this.reportType.concat("Flood");
            this.reportType="Flood";
        }

        if(v==btnFire) {
            //this.reportType.concat("Fire");
            this.reportType="Fire";
        }

        if(v==btnAccident) {
            //this.reportType.concat("Vehicular Accident");

            this.reportType="Vehicular Accident";
        }

        Log.v("E_VALUE", "Report Type: "+this.getReportType());
        startActivity(new Intent(SelectionPage.this, UploadReport.class));
    }

    public static String getReportType() {
        return reportType;
    }
}
