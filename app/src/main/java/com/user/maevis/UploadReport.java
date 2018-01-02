package com.user.maevis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.user.maevis.controllers.cNotification;
import com.user.maevis.models.ReportModel;
import com.user.maevis.session.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UploadReport extends AppCompatActivity {

    DatabaseReference FirebaseReports, FirebaseUsers;
    private EditText txtFldLocation;
    private EditText txtFldDescription;
    private ProgressDialog progressDialog;
    private StorageReference firebaseStorage;
    private Uri reportPhoto;
    private static String imageURL="";

    public static ReportModel reportModel;
    public static DatabaseReference newReport;

    ImageView ivImage;
    VideoView videoView;
    Integer REQUEST_CAMERA=1, REQUEST_VIDEO_CAPTURE = 1, SELECT_FILE=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseReports = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maevis-ecd17.firebaseio.com/Reports");
        FirebaseUsers = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maevis-ecd17.firebaseio.com/Users");

        firebaseStorage = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

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
        txtFldDescription.setHint("Description ["+SelectionPage.getReportType()+SessionManager.getFirstName()+"]");
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

            /*if(requestCode==REQUEST_CAMERA){

                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                ivImage.setImageBitmap(bmp);

            }else if(requestCode==SELECT_FILE){

                Uri selectedImageUri = data.getData();
                ivImage.setImageURI(selectedImageUri);

            }*/

            if(requestCode==REQUEST_CAMERA || requestCode==SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                ivImage.setImageURI(selectedImageUri);


                //upload photo
                progressDialog.setMessage("Uploading photo from camera.");
                progressDialog.show();

                StorageReference filePath = firebaseStorage.child("Photos").child(selectedImageUri.getLastPathSegment());

                filePath.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        //imageURL.concat(downloadUrl.toString());
                        setImageURL(downloadUrl.toString());

                        //Toast.makeText(UploadReport.this, "Report ready to be sent.", Toast.LENGTH_LONG).show();
                        Toast.makeText(UploadReport.this, "Sent! "+getImageURL(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadReport.this, "Photo upload failed!", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
            }
        }
    }

    public void setImageURL(String imgURL) {
        this.imageURL = imgURL;
    }

    public String getImageURL() {
        return this.imageURL;
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

            //finish();
            //startActivity(new Intent(UploadReport.this, Sidebar_HomePage.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void uploadReport() {
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String dateTime = formatter.format(today);

        String description = txtFldDescription.getText().toString();
        String location = txtFldLocation.getText().toString();
        double locationLatitude = Tab2_Location.getUserLatitude();
        double locationLongitude = Tab2_Location.getUserLongitude();
        String reportType = SelectionPage.getReportType();
        String reportedBy = SessionManager.getUserID();

        if(TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please enter description.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(location)) {
            Toast.makeText(this, "Please enter location.", Toast.LENGTH_SHORT).show();
            return;
        }

        reportModel = new ReportModel(dateTime, description, getImageURL(), location, locationLatitude, locationLongitude, "Pending", reportType, reportedBy);

        newReport = FirebaseReports.push();
        newReport.setValue(reportModel);

        Toast.makeText(UploadReport.this, "Report sent!.", Toast.LENGTH_LONG).show();
        finish();
        startActivity(new Intent(UploadReport.this, Sidebar_HomePage.class));

        cNotification.showVerifyReportNotification(getApplication());
        cNotification.vibrateNotification(getApplication());
    }

   /* public void showNotification(){
        String fullName = FirebaseDatabaseManager.getFullName(reportModel.getReportedBy());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_notif_maevis_logo);
        builder.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        builder.setContentTitle(reportModel.getReportType() + " Report");
        builder.setContentText(fullName + " reported a " +  reportModel.getReportType()
                + " Report" + " at " + reportModel.getLocation());
        builder.setAutoCancel(true);

        Intent i = new Intent(this, NotificationView.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotificationView.class);
        stackBuilder.addNextIntent(i);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0, builder.build());
    }*/
}