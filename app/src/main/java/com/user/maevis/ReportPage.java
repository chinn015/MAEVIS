package com.user.maevis;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.user.maevis.models.FirebaseDatabaseManager;

import org.w3c.dom.Text;

public class ReportPage extends AppCompatActivity {

    private TextView viewReportHead;
    private TextView viewReportDesc;
    private TextView viewReportDateTime;
    private ImageView viewReportImage;
    private ImageView viewReportType;

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
        viewReportType = (ImageView) findViewById(R.id.viewReportType);

        if(TabHomeAdapter.getClickedItem() != null) {
            viewReportHead.setText(TabHomeAdapter.getClickedItem().getHead());
            viewReportDesc.setText(TabHomeAdapter.getClickedItem().getDescription());
            viewReportDateTime.setText(TabHomeAdapter.getClickedItem().getDisplayDateTime());
            Picasso.with(getApplicationContext())
                    .load(TabHomeAdapter.getClickedItem().getImageURL())
                    .fit()
                    .into(viewReportImage);
            Picasso.with(getApplicationContext())
                    .load(ListItem.getReportTypeImage(TabHomeAdapter.getClickedItem().getReportType()))
                    .into(viewReportType);

        }else{
            viewReportHead.setText(Tab2_Location.verifiedReport.getHead());
            viewReportDesc.setText(Tab2_Location.verifiedReport.getDescription());
            viewReportDateTime.setText(Tab2_Location.verifiedReport.getDisplayDateTime());
            Picasso.with(getApplicationContext())
                    .load(Tab2_Location.verifiedReport.getImageURL())
                    .fit()
                    .into(viewReportImage);
            Picasso.with(getApplicationContext())
                    .load(ListItem.getReportTypeImage(Tab2_Location.verifiedReport.getReportType()))
                    .into(viewReportType);
        }
    }
}
