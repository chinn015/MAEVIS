package com.user.maevis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.user.maevis.models.ReportModel;
import com.user.maevis.session.SessionManager;

import java.util.Calendar;
import java.util.HashMap;

public class UploadReport extends AppCompatActivity {

    DatabaseReference FirebaseReports;
    private EditText txtFldLocation;
    private EditText txtFldDescription;
    private ProgressDialog progressDialog;

    ImageView ivImage;
    VideoView videoView;
    Integer REQUEST_CAMERA=1, REQUEST_VIDEO_CAPTURE = 1, SELECT_FILE=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseReports = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maevis-ecd17.firebaseio.com/Reports");

        ivImage = (ImageView) findViewById(R.id.ivImage);

        Button fab = (Button) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SelectImage();
            }
        });

        txtFldLocation = (EditText) findViewById(R.id.txtFldLocation);
        txtFldDescription = (EditText) findViewById(R.id.txtFldDescription);
        txtFldDescription.setHint("Description ["+SelectionPage.getReportType()+"]");
    }

    private void SelectImage(){

        final CharSequence[] items={"Camera","Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(UploadReport.this);
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

//                    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);

                } else if (items[i].equals("Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, SELECT_FILE);

                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();

    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(resultCode== Activity.RESULT_OK){

            if(requestCode==REQUEST_CAMERA){

                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                ivImage.setImageBitmap(bmp);

            }else if(requestCode==SELECT_FILE){

                Uri selectedImageUri = data.getData();
                ivImage.setImageURI(selectedImageUri);
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_upload_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_upload_report) {
            //Toast.makeText(getApplicationContext(), "Upload Successful", Toast.LENGTH_LONG).show();
            Toast.makeText(UploadReport.this, "Logged in "+ SessionManager.isLoggedIn()+" as: "+ SessionManager.getFirstName()+" "+ SessionManager.getLastName(), Toast.LENGTH_LONG).show();
            uploadReport();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void uploadReport() {
        String dateTime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        String description = txtFldDescription.getText().toString();
        String location = txtFldLocation.getText().toString();
        double locationLatitude = Tab2_Location.getUserLatitude();
        double locationLongitude = Tab2_Location.getUserLongitude();
        String reportType = SelectionPage.getReportType();
        String reportedBy = SessionManager.getUsername();

        if(TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please enter description.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(location)) {
            Toast.makeText(this, "Please enter location.", Toast.LENGTH_SHORT).show();
            return;
        }

        ReportModel reportModel = new ReportModel(dateTime, description, location, locationLatitude, locationLongitude, reportType, reportedBy);

        storeReport(reportModel);
    }

    public void storeReport(ReportModel reportModel) {
        DatabaseReference newReport = FirebaseReports.push();
        newReport.child("DateTime").setValue(reportModel.getDateTime());
        newReport.child("Description").setValue(reportModel.getReportDescription());
        newReport.child("Location").setValue(reportModel.getLocation());
        newReport.child("LocationLatitude").setValue(reportModel.getLocationLatitude());
        newReport.child("LocationLongitude").setValue(reportModel.getLocationLongitude());
        newReport.child("ReportType").setValue(reportModel.getReportType());
        newReport.child("ReportedBy").setValue(reportModel.getReportedBy());

        Toast.makeText(this, reportModel.getReportType()+" - "+reportModel.getReportedBy(), Toast.LENGTH_SHORT).show();
    }
}