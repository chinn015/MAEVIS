package com.user.maevis;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.user.maevis.session.SessionManager;

import de.hdodenhof.circleimageview.CircleImageView;


public class SidebarSettings extends AppCompatActivity {

    ImageView ivImage;
    VideoView videoView;
    Integer REQUEST_CAMERA=1, REQUEST_VIDEO_CAPTURE = 1, SELECT_FILE=0;
    CircleImageView btnChangePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sidebar_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnChangePhoto = (CircleImageView) findViewById(R.id.imgChangePhoto);
        btnChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SelectImage();
            }
        });

    }

    private void SelectImage(){

        final CharSequence[] items={"Camera","Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(SidebarSettings.this);
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
                btnChangePhoto.setImageBitmap(bmp);

            }else if(requestCode==SELECT_FILE){

                Uri selectedImageUri = data.getData();
                btnChangePhoto.setImageURI(selectedImageUri);

            }

//            if(requestCode==REQUEST_CAMERA || requestCode==SELECT_FILE) {
//                Uri selectedImageUri = data.getData();
//                ivImage.setImageURI(selectedImageUri);
//
//
//                //upload photo
//                progressDialog.setMessage("Uploading photo from camera.");
//                progressDialog.show();
//
//                StorageReference filePath = firebaseStorage.child("Photos").child(selectedImageUri.getLastPathSegment());
//
//                filePath.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                        //imageURL.concat(downloadUrl.toString());
//                        setImageURL(downloadUrl.toString());
//
//                        //Toast.makeText(UploadReport.this, "Report ready to be sent.", Toast.LENGTH_LONG).show();
//                        Toast.makeText(UploadReport.this, "Sent! "+getImageURL(), Toast.LENGTH_LONG).show();
//                        progressDialog.dismiss();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(UploadReport.this, "Photo upload failed!", Toast.LENGTH_LONG).show();
//                        progressDialog.dismiss();
//                    }
//                });
//            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_save_changes) {
            //Toast.makeText(getApplicationContext(), "Upload Successful", Toast.LENGTH_LONG).show();
            Toast.makeText(SidebarSettings.this, "Logged in "+ SessionManager.isLoggedIn()+" as: "+ SessionManager.getFirstName()+" "+ SessionManager.getLastName(), Toast.LENGTH_LONG).show();

            //finish();
            //startActivity(new Intent(UploadReport.this, Sidebar_HomePage.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
