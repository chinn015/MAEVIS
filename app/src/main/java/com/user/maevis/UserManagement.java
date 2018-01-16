package com.user.maevis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.user.maevis.models.PageNavigationManager;

import org.w3c.dom.Text;

/**
 * Created by Chen on 1/15/2018.
 */

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

        viewUMName = (TextView) findViewById(R.id.viewUMName);

        if(PageNavigationManager.getClickedReportPageUserItem() != null /*ReportPage.clickedUserItemStatus*/) {
            viewUMName.setText(PageNavigationManager.getClickedReportPageUserItem().getFirstName()+" "+PageNavigationManager.getClickedReportPageUserItem().getLastName());
        } else if (PageNavigationManager.getClickedVerifyReportUserItem() != null /*VerifyReport.clickedUserItemStatus*/) {
            //viewUMName.setText(VerifyReport.getClickedUserItem().getFirstName()+" "+VerifyReport.getClickedUserItem().getLastName());
            viewUMName.setText(PageNavigationManager.getClickedVerifyReportUserItem().getFirstName()+" "+PageNavigationManager.getClickedVerifyReportUserItem().getLastName());
        }
    }

    @Override
    public void onClick(View v) {
        if(v==btnBlockUser) {

            return;
        }

        if(v==btnProfileUM) {

            return;
        }
    }
}
