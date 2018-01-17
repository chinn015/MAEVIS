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

        /*if(TabHomeAdapter.getClickedItemVerified() != null) {
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
        }*/

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
        }

        viewUserImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==viewUserImage) {
            if(SessionManager.getUserType().equals("Admin")) {
                Toast.makeText(this, "User Management", Toast.LENGTH_LONG).show();

                if (PageNavigationManager.getClickedTabHomeListItemVerified() != null /*TabHomeAdapter.clickedStatus*/) {
                    /*clickedUserID = PageNavigationManager.getClickedTabHomeListItemVerified().getReportedBy();
                    tab = "TabLoc";
                    listItemTemp = PageNavigationManager.getClickedTabLocListItemPending();
                    Toast.makeText(this, "[TabLoc] User ID: "+clickedUserID, Toast.LENGTH_LONG).show();*/

                    PageNavigationManager.markTab(PageNavigationManager.getClickedTabHomeListItemVerified().getReportedBy(),
                            PageNavigationManager.KEY_TABHOME,
                            PageNavigationManager.getClickedTabHomeListItemVerified());

                    Toast.makeText(this, "[TabLoc] User ID: "+PageNavigationManager.getClickedUserID(), Toast.LENGTH_LONG).show();
                }

                if (PageNavigationManager.getClickedTabNotifListItem() != null /*TabNotifAdapter.clickedStatus*/) {
                    /*clickedUserID = PageNavigationManager.getClickedTabLocListItemPending().getReportedBy();
                    tab = "TabLoc";
                    listItemTemp = PageNavigationManager.getClickedTabLocListItemPending();*/

                    PageNavigationManager.markTab(PageNavigationManager.getClickedTabNotifListItem().getReportedBy(),
                            PageNavigationManager.KEY_TABNOTIF,
                            PageNavigationManager.getClickedTabNotifListItem());

                    Toast.makeText(this, "[TabLoc] User ID: "+PageNavigationManager.getClickedUserID(), Toast.LENGTH_LONG).show();
                }

                if (PageNavigationManager.getClickedTabLocListItemVerified() != null) {
                    /*clickedUserID = PageNavigationManager.getClickedTabLocListItemPending().getReportedBy();
                    tab = "TabLoc";
                    listItemTemp = PageNavigationManager.getClickedTabLocListItemPending();*/

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

                /*for (int x = 0; x < FirebaseDatabaseManager.getUserItems().size(); x++) {
                    if (userID.equals(FirebaseDatabaseManager.getUserItems().get(x).getUserID())) {
                        clickedUserItem = FirebaseDatabaseManager.getUserItems().get(x);
                        clickedUserItemStatus = true;

                        PageNavigationManager.clickReportPageUserItem(FirebaseDatabaseManager.getUserItems().get(x));

                        /*VerifyReport.clickedUserItemStatus = false;
                        TabHomeAdapter.clickedUserItemStatus = false;
                    }
                }*/
            }
        }
    }

    /*public static UserItem getClickedUserItem() {
        return clickedUserItem;
    }

    public static void setClickedUserItem(UserItem clickedUserItem) {
        ReportPage.clickedUserItem = clickedUserItem;
    }*/
}
