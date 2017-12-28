package com.user.maevis;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ReportPage extends AppCompatActivity {

    private TextView viewReportHead;
    private TextView viewReportDesc;
    private TextView viewReportDateTime;
    private ImageView viewReportImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewReportHead = (TextView) findViewById(R.id.viewReportHead);
        viewReportDesc = (TextView) findViewById(R.id.viewReportDesc);
        viewReportDateTime = (TextView) findViewById(R.id.viewReportDateTime);
        viewReportImage = (ImageView) findViewById(R.id.viewReportImage);

        if(TabHomeAdapter.getClickedItem() != null) {
            viewReportHead.setText(TabHomeAdapter.getClickedItem().getHead());
            viewReportDesc.setText(TabHomeAdapter.getClickedItem().getDescription());
            viewReportDateTime.setText(TabHomeAdapter.getClickedItem().getDateTime());
            Picasso.with(getApplicationContext()).load(TabHomeAdapter.getClickedItem().getImageURL()).into(viewReportImage);
        }else{
            viewReportHead.setText(Tab2_Location.verifiedReport.getHead());
            viewReportDesc.setText(Tab2_Location.verifiedReport.getDescription());
            viewReportDateTime.setText(Tab2_Location.verifiedReport.getDateTime());
            Picasso.with(getApplicationContext()).load(Tab2_Location.verifiedReport.getImageURL()).into(viewReportImage);
        }
    }
}
