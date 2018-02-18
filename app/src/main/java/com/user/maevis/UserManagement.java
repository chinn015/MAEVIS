package com.user.maevis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.user.maevis.models.FirebaseDatabaseManager;
import com.user.maevis.models.PageNavigationManager;

import org.w3c.dom.Text;


public class UserManagement extends AppCompatActivity implements View.OnClickListener {
    private Button btnBlockUser;
    private Button btnProfileUM;
    private TextView viewUMName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_management);

        btnBlockUser = (Button) findViewById(R.id.btnBlockUser);
        btnProfileUM = (Button) findViewById(R.id.btnProfileUM);

        btnBlockUser.setOnClickListener(this);

        viewUMName = (TextView) findViewById(R.id.viewUMName);

        if(PageNavigationManager.getClickedReportPageUserItem() != null /*ReportPage.clickedUserItemStatus*/) {
            viewUMName.setText(PageNavigationManager.getClickedReportPageUserItem().getFirstName()+" "+PageNavigationManager.getClickedReportPageUserItem().getLastName());
            PageNavigationManager.clickUMBlockUserItem(PageNavigationManager.getClickedReportPageUserItem());
        } else if (PageNavigationManager.getClickedVerifyReportUserItem() != null /*VerifyReport.clickedUserItemStatus*/) {
            //viewUMName.setText(VerifyReport.getClickedUserItem().getFirstName()+" "+VerifyReport.getClickedUserItem().getLastName());
            viewUMName.setText(PageNavigationManager.getClickedVerifyReportUserItem().getFirstName()+" "+PageNavigationManager.getClickedVerifyReportUserItem().getLastName());
            PageNavigationManager.clickUMBlockUserItem(PageNavigationManager.getClickedVerifyReportUserItem());
        }
      
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onClick(View v) {
        if(v==btnBlockUser) {
            showDialogBlock();
            //FirebaseDatabaseManager.FirebaseUsers.child(PageNavigationManager.getClickedUMBlockUserItem().getUserID()).child("userStatus").setValue("Blocked");

            /*finish();
            startActivity(new Intent(UserManagement.this, Sidebar_HomePage.class));*/
            return;
        }

        if(v==btnProfileUM) {

            return;
        }
    }

    private void showDialogBlock() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        String name = "";

        if(PageNavigationManager.getClickedUMBlockUserItem() != null) {
            name = PageNavigationManager.getClickedUMBlockUserItem().getFirstName()+" "+PageNavigationManager.getClickedUMBlockUserItem().getLastName();
        }

        builder.setTitle("Block User");
        builder.setMessage("Are you sure you want to block "+name+"?");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Block", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(UserManagement.this, "User blocked.", Toast.LENGTH_SHORT).show();

                FirebaseDatabaseManager.FirebaseUsers.child(PageNavigationManager.getClickedUMBlockUserItem().getUserID()).child("userStatus").setValue("Blocked");

                finish();
                startActivity(new Intent(UserManagement.this, Sidebar_HomePage.class));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        alert.getButton(alert.BUTTON_POSITIVE).setTextColor(Color.BLACK);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
