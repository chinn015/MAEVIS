package com.user.maevis;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.user.maevis.models.FirebaseDatabaseManager;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReportPage extends AppCompatActivity  implements View.OnClickListener  {

    private TextView viewReportHead;
    private TextView viewReportDesc;
    private TextView viewReportDateTime;
    private ImageView viewReportImage;
    private ImageView viewReportType;
    private CircleImageView viewUserImage;

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
        viewUserImage = (CircleImageView) findViewById(R.id.imgViewProfilePic);

        if(TabHomeAdapter.getClickedItemVerified() != null) {
            viewReportHead.setText(TabHomeAdapter.getClickedItemVerified().getHead());
            viewReportDesc.setText(TabHomeAdapter.getClickedItemVerified().getDescription());
            viewReportDateTime.setText(TabHomeAdapter.getClickedItemVerified().getDisplayDateTime());
            Picasso.with(getApplicationContext())
                    .load(TabHomeAdapter.getClickedItemVerified().getImageThumbnailURL())
                    .fit()
                    .into(viewReportImage);
            Picasso.with(getApplicationContext())
                    .load(ListItem.getReportTypeImage(TabHomeAdapter.getClickedItemVerified().getReportType()))
                    .into(viewReportType);
        }else{
            viewReportHead.setText(Tab2_Location.activeVerifiedReport.getHead());
            viewReportDesc.setText(Tab2_Location.activeVerifiedReport.getDescription());
            viewReportDateTime.setText(Tab2_Location.activeVerifiedReport.getDisplayDateTime());
            Picasso.with(getApplicationContext())
                    .load(Tab2_Location.activeVerifiedReport.getImageThumbnailURL())
                    .fit()
                    .into(viewReportImage);
            Picasso.with(getApplicationContext())
                    .load(ListItem.getReportTypeImage(Tab2_Location.activeVerifiedReport.getReportType()))
                    .into(viewReportType);
        }

        viewUserImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==viewUserImage) {
            Toast.makeText(this, "User", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, UserManagement.class);
            startActivity(i);
        }
    }
}
