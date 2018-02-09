package com.user.maevis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.user.maevis.controllers.cNotification;
import com.user.maevis.models.FirebaseDatabaseManager;
import com.user.maevis.models.NotifModel;
import com.user.maevis.models.ReportModel;
import com.user.maevis.session.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.grantland.widget.AutofitTextView;

import static android.R.attr.bitmap;

public class UploadReport extends AppCompatActivity {

    private AutofitTextView txtFldLocation;
    private EditText txtFldDescription;
    private ProgressDialog progressDialog;
    private StorageReference firebaseStorage;
    private Uri reportPhoto;
    private static String imageURL = "";

    public static ReportModel reportModel;
    public static DatabaseReference newReport;

    ImageView ivImage, ivReportType;
    VideoView videoView;
    Integer PHOTO_CODE, REQUEST_CAMERA = 1, REQUEST_VIDEO_CAPTURE = 1, SELECT_FILE = 0;
    String mCurrentPhotoPath;
    Uri photoURI, finalImageURI;
    File imageFile;

    public static NotifModel notifModel;
    public static DatabaseReference newNotif;

    private List<String> nearbyAdmins;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseStorage = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this, R.style.AlertDialogStyle);

        ivImage = (ImageView) findViewById(R.id.ivImage);

        Button fab = (Button) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SelectImage();
            }
        });

        nearbyAdmins = new ArrayList<>();

        txtFldLocation = (AutofitTextView) findViewById(R.id.txtFldLocation);
        txtFldDescription = (EditText) findViewById(R.id.txtFldDescription);
        txtFldDescription.setHint("Write something about this report. [" + SelectionPage.getReportType() + "]");
        if(Tab2_Location.userLocAddress != null){
            txtFldLocation.setText(Tab2_Location.userLocAddress);
        }else{
            txtFldLocation.setText(Sidebar_HomePage.userAddress);
        }
        ivReportType = (ImageView) findViewById(R.id.reportIconType);
        Picasso.with(getApplicationContext())
                .load(ListItem.getReportMarkerImage(SelectionPage.getReportType()))
                .into(ivReportType);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            displayDiscardDialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void displayDiscardDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Discard changes?");
        builder.setMessage("You have unsaved changes.");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        android.app.AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        alert.getButton(alert.BUTTON_POSITIVE).setTextColor(Color.BLACK);
    }

    private void SelectImage() {

        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(UploadReport.this);
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    String pictureName = getPictureName();
                    imageFile = new File(pictureDirectory, pictureName);
                    //Uri pictureUri = Uri.fromFile(imageFile);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri pictureUri = FileProvider.getUriForFile(getApplicationContext(), "com.your.package.fileProvider", imageFile);
                    photoURI = pictureUri;
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);

                    PHOTO_CODE = REQUEST_CAMERA;

                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[i].equals("Gallery")) {

                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");

                    PHOTO_CODE = SELECT_FILE;

                    startActivityForResult(intent, SELECT_FILE);
                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();

    }

    public String getPictureName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());

        return "IMGMAEVIS"+timestamp+".jpg";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_CAMERA) {;
                ivImage.setImageURI(photoURI);
                finalImageURI = photoURI;
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                ivImage.setImageURI(selectedImageUri);
                finalImageURI = selectedImageUri;
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
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
            String description = txtFldDescription.getText().toString();

            if (TextUtils.isEmpty(description)) {
                Toast.makeText(this, "Please enter description.", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (finalImageURI == null) {
                Toast.makeText(this, "Please include a photo.", Toast.LENGTH_SHORT).show();
                return false;
            }

            StorageReference filePath = FirebaseDatabaseManager.FirebasePhotoStorage.child("Photos").child(finalImageURI.getLastPathSegment());

            progressDialog.setMessage("Sending report.");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);

            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), finalImageURI);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
                String path = MediaStore.Images.Media.insertImage(UploadReport.this.getContentResolver(), bmp, finalImageURI.getLastPathSegment(), null);
                finalImageURI = Uri.parse(path);

                filePath.putFile(finalImageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        setImageURL(downloadUrl.toString());

                        uploadReport();

                        Toast.makeText(UploadReport.this, "Report sent.", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadReport.this, "Photo upload failed!", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
            } catch (IOException ie) {
                Toast.makeText(UploadReport.this, "Upload error.", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }

            return true;

        }else if (id == android.R.id.home) {
            displayDiscardDialog();
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
        String mergedTo = "NULL";
        String reportStatus = "Pending";
        String reportType = SelectionPage.getReportType();
        String reportedBy = SessionManager.getUserID();

        if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please enter description.", Toast.LENGTH_SHORT).show();
            return;
        }

        reportModel = new ReportModel(dateTime, description, getImageURL(), location, locationLatitude, locationLongitude, mergedTo, reportStatus, reportType, reportedBy);

        newReport = FirebaseDatabaseManager.FirebaseReports.push();
        newReport.setValue(reportModel);

        //send notification to nearby Admins
        String fullName = FirebaseDatabaseManager.getFullName(reportModel.getReportedBy());
        //String notifMessage = fullName+" sent a "+reportModel.getReportType()+" Report near you.";
        String notifTitle = "MAEVIS: Pending "+reportModel.getReportType()+" Report";
        String notifReportID = newReport.getKey();

        for(int x=0; x<FirebaseDatabaseManager.getUserItems().size(); x++) {
            if(FirebaseDatabaseManager.getUserItems().get(x).getUserType().equals("Admin")) {
                UserItem nearbyAdmin = FirebaseDatabaseManager.getUserItems().get(x);
                double nearbyLatitude = nearbyAdmin.getCurrentLat();
                double nearbyLongitude = nearbyAdmin.getCurrentLong();
                float distance, nearby_distance;

                nearby_distance = 1000;
                Location nearby_admins = new Location("1");
                Location pendging_report_location = new Location("2");

                nearby_admins.setLatitude(nearbyLatitude);
                nearby_admins.setLongitude(nearbyLongitude);

                pendging_report_location.setLatitude(reportModel.getLocationLatitude());
                pendging_report_location.setLongitude(reportModel.getLocationLongitude());

                //Returns the approximate distance in meters between the current location and the given report location.
                distance = pendging_report_location.distanceTo(nearby_admins);

                if (distance <= nearby_distance) {
                    nearbyAdmins.add(nearbyAdmin.getUserID());
                }
            }
        }

        for(int x=0; x < nearbyAdmins.size(); x++) {
            String messageToAdmin = "["+FirebaseDatabaseManager.getFullName(nearbyAdmins.get(x))+"] "+fullName+" sent a "+reportModel.getReportType()+" Report near you.";
            notifModel = new NotifModel(messageToAdmin, notifReportID, notifTitle, nearbyAdmins.get(x));
            //notifModel = new NotifModel(notifMessage, notifReportID, notifTitle, nearbyAdmins.get(x));
            newNotif = FirebaseDatabaseManager.FirebaseNotifications.push();
            newNotif.setValue(notifModel);
        }

        Toast.makeText(UploadReport.this, "Report sent!", Toast.LENGTH_LONG).show();
        finish();
        startActivity(new Intent(UploadReport.this, Sidebar_HomePage.class));

        //cNotification.showVerifyReportNotification(getApplication());
        //cNotification.vibrateNotification(getApplication());
    }

}