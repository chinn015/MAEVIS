package com.user.maevis;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

public class SelectionPage extends Activity implements View.OnClickListener {

    ImageButton btnAccident, btnFlood, btnFire, btnCloseSelectionPage;
    public static String reportType="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_selection_page);

        btnFlood = (ImageButton) findViewById(R.id.btnFlood);
        btnFire = (ImageButton) findViewById(R.id.btnFire);
        btnAccident = (ImageButton) findViewById(R.id.btnAccident);
        btnCloseSelectionPage = (ImageButton) findViewById(R.id.closeSelectionPage);

        btnFlood.setOnClickListener(this);
        btnFire.setOnClickListener(this);
        btnAccident.setOnClickListener(this);
        btnCloseSelectionPage.setOnClickListener(this);
//        btnCloseSelectionPage.setBackgroundColor(Color.parseColor("#FFFFFF"));

    }

    @Override
    public void onClick(View v) {
        if(v==btnFlood) {
            //this.reportType.concat("Flood");
            this.reportType="Flood";
            startActivity(new Intent(SelectionPage.this, UploadReport.class));
        }

        if(v==btnFire) {
            //this.reportType.concat("Fire");
            this.reportType="Fire";
            startActivity(new Intent(SelectionPage.this, UploadReport.class));
        }

        if(v==btnAccident) {
            //this.reportType.concat("Vehicular Accident");
            this.reportType="Vehicular Accident";
            startActivity(new Intent(SelectionPage.this, UploadReport.class));
        }

        if(v == btnCloseSelectionPage){
            finish();
        }

    }

    public static String getReportType() {
        return reportType;
    }

}
