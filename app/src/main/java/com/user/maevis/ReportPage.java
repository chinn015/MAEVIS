package com.user.maevis;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.user.maevis.models.FirebaseDatabaseManager;
import com.user.maevis.models.PageNavigationManager;
import com.user.maevis.session.SessionManager;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReportPage extends AppCompatActivity  implements View.OnClickListener  {

    private TextView viewReportHead;
    private TextView viewReportDesc;
    private TextView viewReportDateTime;
    private ImageView viewReportImage;
    private ImageView viewReportType;
    private CircleImageView viewUserImage;

    /*private static UserItem clickedUserItem = null;
    static boolean clickedUserItemStatus = false;*/

    /*private String clickedUserID = "";
    private ListItem listItemTemp = null;
    private String tab = "";*/

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

        if(PageNavigationManager.getClickedTabHomeListItemVerified() != null) {
            viewReportHead.setText(PageNavigationManager.getClickedTabHomeListItemVerified().getHead());
            viewReportDesc.setText(PageNavigationManager.getClickedTabHomeListItemVerified().getDescription());
            viewReportDateTime.setText(PageNavigationManager.getClickedTabHomeListItemVerified().getDisplayDateTime());
            Picasso.with(getApplicationContext())
                    .load(PageNavigationManager.getClickedTabHomeListItemVerified().getImageThumbnailURL())
                    .fit()
                    .into(viewReportImage);
            Picasso.with(getApplicationContext())
                    .load(ListItem.getReportTypeImage(PageNavigationManager.getClickedTabHomeListItemVerified().getReportType()))
                    .into(viewReportType);
            Picasso.with(getApplicationContext())
                    .load(PageNavigationManager.getClickedTabHomeListItemVerified().getUserPhoto())
                    .into(viewUserImage);

        }else if (PageNavigationManager.getClickedTabLocListItemVerified() != null){
            viewReportHead.setText(PageNavigationManager.getClickedTabLocListItemVerified().getHead());
            viewReportDesc.setText(PageNavigationManager.getClickedTabLocListItemVerified().getDescription());
            viewReportDateTime.setText(PageNavigationManager.getClickedTabLocListItemVerified().getDisplayDateTime());
            Picasso.with(getApplicationContext())
                    .load(PageNavigationManager.getClickedTabLocListItemVerified().getImageThumbnailURL())
                    .fit()
                    .into(viewReportImage);
            Picasso.with(getApplicationContext())
                    .load(ListItem.getReportTypeImage(PageNavigationManager.getClickedTabLocListItemVerified().getReportType()))
                    .into(viewReportType);
            Picasso.with(getApplicationContext())
                    .load(PageNavigationManager.getClickedTabLocListItemVerified().getUserPhoto())
                    .into(viewUserImage);

        }else if(TabProfileTimelineAdapter.getClickedItem() != null){
            viewReportHead.setText(TabProfileTimelineAdapter.getClickedItem().getHead());
            viewReportDesc.setText(TabProfileTimelineAdapter.getClickedItem().getDescription());
            viewReportDateTime.setText(TabProfileTimelineAdapter.getClickedItem().getDisplayDateTime());
            Picasso.with(getApplicationContext())
                    .load(TabProfileTimelineAdapter.getClickedItem().getImageURL())
                    .fit()
                    .into(viewReportImage);
            Picasso.with(getApplicationContext())
                    .load(TabProfileTimelineAdapter.getClickedItem().getReportType())
                    .into(viewReportType);
            Picasso.with(getApplicationContext())
                    .load(TabProfileTimelineAdapter.getClickedItem().getUserPhoto())
                    .into(viewUserImage);

        }else if(TabNotifAdapterRegUser.getClickedItem() != null){
            viewReportHead.setText(TabNotifAdapterRegUser.getClickedItem().getHead());
            viewReportDesc.setText(TabNotifAdapterRegUser.getClickedItem().getDescription());
            viewReportDateTime.setText(TabNotifAdapterRegUser.getClickedItem().getDisplayDateTime());
            Picasso.with(getApplicationContext())
                    .load(TabNotifAdapterRegUser.getClickedItem().getImageThumbnailURL())
                    .fit()
                    .into(viewReportImage);
            Picasso.with(getApplicationContext())
                    .load(TabNotifAdapterRegUser.getClickedItem().getReportType())
                    .into(viewReportType);
            Picasso.with(getApplicationContext())
                    .load(TabNotifAdapterRegUser.getClickedItem().getUserPhoto())
                    .into(viewUserImage);
        }

        viewUserImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==viewUserImage) {
            if(SessionManager.getUserType().equals("Admin")) {
                Toast.makeText(this, "User Management", Toast.LENGTH_LONG).show();

                if (PageNavigationManager.getClickedTabHomeListItemVerified() != null) {
                    PageNavigationManager.markTab(PageNavigationManager.getClickedTabHomeListItemVerified().getReportedBy(),
                            PageNavigationManager.KEY_TABHOME,
                            PageNavigationManager.getClickedTabHomeListItemVerified());

                    Toast.makeText(this, "[TabLoc] User ID: "+PageNavigationManager.getClickedUserID(), Toast.LENGTH_LONG).show();
                }

                if (PageNavigationManager.getClickedTabNotifListItem() != null) {
                    PageNavigationManager.markTab(PageNavigationManager.getClickedTabNotifListItem().getReportedBy(),
                            PageNavigationManager.KEY_TABNOTIF,
                            PageNavigationManager.getClickedTabNotifListItem());

                    Toast.makeText(this, "[TabLoc] User ID: "+PageNavigationManager.getClickedUserID(), Toast.LENGTH_LONG).show();
                }

                if (PageNavigationManager.getClickedTabLocListItemVerified() != null) {
                    PageNavigationManager.markTab(PageNavigationManager.getClickedTabLocListItemVerified().getReportedBy(),
                            PageNavigationManager.KEY_TABLOCVERIFIED,
                            PageNavigationManager.getClickedTabLocListItemVerified());

                    Toast.makeText(this, "[TabLoc] User ID: "+PageNavigationManager.getClickedUserID(), Toast.LENGTH_LONG).show();
                }

                PageNavigationManager.clickReportPageUserItem(FirebaseDatabaseManager.getUserItem(PageNavigationManager.getClickedUserID()));
                if(PageNavigationManager.getClickedReportPageUserItem() != null) {
                    Intent i = new Intent(this, UserManagement.class);
                    startActivity(i);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home  && TabProfileTimelineAdapter.getClickedItem() != null) {

            startActivity(new Intent(ReportPage.this, SidebarProfile.class));
            finish();

        }else if (id == android.R.id.home  && PageNavigationManager.getClickedTabHomeListItemVerified() != null) {

            startActivity(new Intent(ReportPage.this, Sidebar_HomePage.class));
            finish();

        }else if (id == android.R.id.home  && PageNavigationManager.getClickedTabLocListItemVerified() != null){

            startActivity(new Intent(ReportPage.this, Sidebar_HomePage.class));
            finish();

        }else if (id == android.R.id.home  && TabNotifAdapterRegUser.getClickedItem() != null){

            startActivity(new Intent(ReportPage.this, Sidebar_HomePage.class));
            finish();

        }

        TabProfileTimelineAdapter.setClickedItem(null);
        PageNavigationManager.setClickedTabHomeListItemVerified(null);
        PageNavigationManager.setClickedTabLocListItemVerified(null);
        TabNotifAdapterRegUser.setClickedItem(null);

        return super.onOptionsItemSelected(item);
    }
}
