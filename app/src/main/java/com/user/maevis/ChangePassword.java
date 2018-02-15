package com.user.maevis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.user.maevis.models.FirebaseDatabaseManager;
import com.user.maevis.session.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import me.grantland.widget.AutofitTextView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.user.maevis.models.FirebaseDatabaseManager;
import com.user.maevis.session.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import me.grantland.widget.AutofitTextView;


public class ChangePassword extends AppCompatActivity {

    private EditText username, oldPassword, fName, lName, email, newPassword, conPassword;

    private DatabaseReference dbUsername, dbPassword, dbFName, dbLName, dbEmail, dbAddress, dbUserPhoto;

    private static final String REQUIRED = "Required";

    private static final String TAG = "SidebarSettings";

    private String oldPass, newP, oldP, conP;

    private static String imageURL = "";

    private ProgressDialog progressDialog;

    //ImageView ivImage;
    VideoView videoView;
    CircleImageView ivImage;
    AutofitTextView txtFldAddress;
    Integer PHOTO_CODE, REQUEST_CAMERA = 1, REQUEST_VIDEO_CAPTURE = 1, SELECT_FILE = 0;
    String mCurrentPhotoPath;
    Uri photoURI, finalImageURI;
    File imageFile;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this, R.style.AlertDialogStyle);

        //User logged in
        oldPassword = (EditText) findViewById(R.id.txtFldEditPassword);
        newPassword = (EditText) findViewById(R.id.txtFldEditNewPassword);
        conPassword = (EditText) findViewById(R.id.txtFldEditConPassword);

        dbPassword = FirebaseDatabase.getInstance().getReference().child("Users").child(SessionManager.getUserID()).child("password");
        dbFName = FirebaseDatabase.getInstance().getReference().child("Users").child(SessionManager.getUserID()).child("firstName");


            dbPassword.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    oldPassword.setText(dataSnapshot.getValue(String.class));
                    oldPass = dataSnapshot.getValue(String.class);
                    newPassword.setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });





    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        oldP = oldPassword.getText().toString();
        newP = newPassword.getText().toString();
        conP = conPassword.getText().toString();

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if(oldP.length() != 0 || newP.length() != 0 || conP.length() != 0 ){
                displayDiscardDialog();
            }else{
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setImageURL(String imgURL) {
        this.imageURL = imgURL;
    }

    public String getImageURL() {
        return this.imageURL;
    }


    private void saveData(){
        //Toast.makeText(this, "Updated Profile", Toast.LENGTH_SHORT).show();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String oldPass = oldPassword.getText().toString().trim();
        final String newPass = newPassword.getText().toString().trim();
        final String conPass = conPassword.getText().toString().trim();


        // Password is required
        if (TextUtils.isEmpty(oldPass)) {
            oldPassword.setError(REQUIRED);
            return;
        }

        // New Password is required
        if (TextUtils.isEmpty(newPass)) {
            newPassword.setError(REQUIRED);
            return;
        }

        // Confirm Password is required
        if (TextUtils.isEmpty(conPass)) {
            conPassword.setError(REQUIRED);
            return;
        }


        String regex = "(.)*(\\d)(.)*";
        Pattern pattern = Pattern.compile(regex);
        boolean containsNumber = pattern.matcher(newPass).matches();

        if(newPass.length() < 8 && containsNumber != true){
            newPassword.setError("Passsword must have at least 8 characters with at least 1 digit");
            return;
        }

        if(!newPass.equals(conPass)){
            conPassword.setError("Your new password and confirmation password do not match.");
            return;
        }


        dbUsername = FirebaseDatabase.getInstance().getReference();

        AuthCredential credential = EmailAuthProvider.getCredential(SessionManager.getEmail(), oldPass);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                            } else {
                            }
                        }
                    });
                }else{
                }
            }
        });


        Intent i = new Intent(this, Sidebar_HomePage.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        oldP = oldPassword.getText().toString();
        newP = newPassword.getText().toString();
        conP = conPassword.getText().toString();

        if (id == R.id.action_save_changes) {
            showUpdateConfirmationDialog();
            return true;
        } else if (id == android.R.id.home) {
            if(oldP.length() != 0 || newP.length() != 0 || conP.length() != 0 ){
                displayDiscardDialog();
            }else{
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void showUpdateConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Update Password");
        builder.setMessage("Are you sure you want to apply these changes?");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //saveData();
                if (finalImageURI != null) {
                    StorageReference filePath = FirebaseDatabaseManager.FirebasePhotoStorage.child("UserPhotos").child(finalImageURI.getLastPathSegment());

                    progressDialog.setMessage("Updating Password.");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);

                    try {

                        Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), finalImageURI);
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
                        String path = MediaStore.Images.Media.insertImage(ChangePassword.this.getContentResolver(), bmp, finalImageURI.getLastPathSegment(), null);
                        finalImageURI = Uri.parse(path);

                        filePath.putFile(finalImageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                setImageURL(downloadUrl.toString());

                                saveData();

                                Toast.makeText(ChangePassword.this, "Update Successful!.", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ChangePassword.this, "Photo upload failed!", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        });
                    } catch (IOException ie) {
                        Toast.makeText(ChangePassword.this, "Update error.", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }

                }else{
                    saveData();
                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        alert.getButton(alert.BUTTON_POSITIVE).setTextColor(Color.BLACK);
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

    public String getPictureName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());

        return "IMGMAEVIS"+timestamp+".jpg";
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
