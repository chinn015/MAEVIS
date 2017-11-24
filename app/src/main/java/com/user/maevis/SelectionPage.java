package com.user.maevis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

public class SelectionPage extends Activity {

    ImageButton btnAccident, btnFlood, btnFire;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_selection_page);

        btnFlood = (ImageButton) findViewById(R.id.btnFlood);
        btnFire = (ImageButton) findViewById(R.id.btnFire);
        btnAccident = (ImageButton) findViewById(R.id.btnAccident);

        btnFlood.setOnClickListener(new View.OnClickListener() {
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
        });
    }
}
