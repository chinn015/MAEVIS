package com.user.maevis;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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

import de.hdodenhof.circleimageview.CircleImageView;
import me.grantland.widget.AutofitTextView;


public class SidebarSettings extends AppCompatActivity {

    private EditText username, password, fName, lName, email;

    private DatabaseReference dbUsername, dbPassword, dbFName, dbLName, dbEmail, dbAddress;

    private static final String REQUIRED = "Required";

    private static final String TAG = "SidebarSettings";

    private String oldPass;

    ImageView ivImage;
    VideoView videoView;
    Integer REQUEST_CAMERA=1, REQUEST_VIDEO_CAPTURE = 1, SELECT_FILE=0;
    CircleImageView btnChangePhoto;
    AutofitTextView txtFldAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sidebar_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //User logged in
        username = (EditText) findViewById(R.id.txtFldEditUsername);
        password = (EditText) findViewById(R.id.txtFldEditPassword);
        fName = (EditText) findViewById(R.id.txtFldEditFirstname);
        lName = (EditText) findViewById(R.id.txtFldEditLastname);
        email = (EditText) findViewById(R.id.txtFldEditEmail);
        txtFldAddress = (AutofitTextView) findViewById(R.id.txtFldEditAddress);
        btnChangePhoto = (CircleImageView) findViewById(R.id.imgChangePhoto);
        Picasso.with(this).load(SessionManager.getUserPhoto()).into(btnChangePhoto);

        dbUsername = FirebaseDatabase.getInstance().getReference().child("Users").child(SessionManager.getUserID()).child("username");
        dbPassword = FirebaseDatabase.getInstance().getReference().child("Users").child(SessionManager.getUserID()).child("password");
        dbFName = FirebaseDatabase.getInstance().getReference().child("Users").child(SessionManager.getUserID()).child("firstName");
        dbLName = FirebaseDatabase.getInstance().getReference().child("Users").child(SessionManager.getUserID()).child("lastName");
        dbEmail = FirebaseDatabase.getInstance().getReference().child("Users").child(SessionManager.getUserID()).child("email");
        dbAddress = FirebaseDatabase.getInstance().getReference().child("Users").child(SessionManager.getUserID()).child("address");
        btnChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SelectImage();
            }
        });

        txtFldAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), UpdateHomeAddress.class);
                final String userName = username.getText().toString();
                final String userPassword = password.getText().toString();
                final String userEmail = email.getText().toString();
                final String userFname = fName.getText().toString();
                final String userLname = lName.getText().toString();

                i.putExtra("userName", userName);
                i.putExtra("userPassword", userPassword);
                i.putExtra("userEmail", userEmail);
                i.putExtra("userFname", userFname);
                i.putExtra("userLname", userLname);

                startActivity(i);
                finish();
            }
        });


        if(UpdateHomeAddress.userHomeAddress != null){
            Intent i = getIntent();
            username.setText(UpdateHomeAddress.userName);
            password.setText(UpdateHomeAddress.userPassword);
            fName.setText(UpdateHomeAddress.userFname);
            lName.setText(UpdateHomeAddress.userLname);
            email.setText(UpdateHomeAddress.userEmail);
            txtFldAddress.setText(UpdateHomeAddress.userHomeAddress);
            oldPass = UpdateHomeAddress.userPassword;
            //Toast.makeText(this, "get1 : " + UpdateHomeAddress.userName, Toast.LENGTH_LONG).show();
        }else{
            dbAddress.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    txtFldAddress.setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            dbUsername.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    username.setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            dbPassword.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    password.setText(dataSnapshot.getValue(String.class));
                    oldPass = dataSnapshot.getValue(String.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            dbFName.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    fName.setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            dbLName.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    lName.setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            dbEmail.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    email.setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    private void saveData(){
        //Toast.makeText(this, "Updated Profile", Toast.LENGTH_SHORT).show();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userName = username.getText().toString();
        final String pass = password.getText().toString();
        final String first = fName.getText().toString();
        final String last = lName.getText().toString();
        final String emailAdd = email.getText().toString();
        final String add = txtFldAddress.getText().toString();
        final Double homeLat = UpdateHomeAddress.userHomeLat;
        final Double homeLong = UpdateHomeAddress.userHomeLong;

        // Username is required
        if (TextUtils.isEmpty(userName)) {
            username.setError(REQUIRED);
            return;
        }

        // Password is required
        if (TextUtils.isEmpty(pass)) {
            password.setError(REQUIRED);
            return;
        }

        // First Name is required
        if (TextUtils.isEmpty(first)) {
            fName.setError(REQUIRED);
            return;
        }

        // Last Name is required
        if (TextUtils.isEmpty(last)) {
            lName.setError(REQUIRED);
            return;
        }

        // Email Address is required
        if (TextUtils.isEmpty(emailAdd)) {
            email.setError(REQUIRED);
            return;
        }

        // Address is required
        if (TextUtils.isEmpty(add)) {
            txtFldAddress.setError(REQUIRED);
            return;
        }

        dbUsername = FirebaseDatabase.getInstance().getReference();
        dbUsername.child("Users").child(SessionManager.getUserID()).child("username").setValue(userName);
        dbUsername.child("Users").child(SessionManager.getUserID()).child("password").setValue(pass);
        dbUsername.child("Users").child(SessionManager.getUserID()).child("firstName").setValue(first);
        dbUsername.child("Users").child(SessionManager.getUserID()).child("lastName").setValue(last);
        dbUsername.child("Users").child(SessionManager.getUserID()).child("email").setValue(emailAdd);
        dbUsername.child("Users").child(SessionManager.getUserID()).child("address").setValue(add);
        dbUsername.child("Users").child(SessionManager.getUserID()).child("homeLat").setValue(homeLat);
        dbUsername.child("Users").child(SessionManager.getUserID()).child("homeLong").setValue(homeLong);

        AuthCredential credential = EmailAuthProvider.getCredential(SessionManager.getEmail(), oldPass);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "User re-authenticated.");

                    user.updateEmail(emailAdd)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Log.d(TAG, "User email address updated.");
                                    }else{
                                        Log.d(TAG, "User email address not updated.");
                                    }
                                }
                            });

                }else{
                    Log.d(TAG, "User not re-authenticated.");
                }
            }
        });

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "User re-authenticated.");

                    user.updatePassword(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Password updated");
                            } else {
                                Log.d(TAG, "Error password not updated");
                            }
                        }
                    });
                }else{
                    Log.d(TAG, "User not re-authenticated.");
                }
            }
        });

        SessionManager.updateSession(SessionManager.getUserID(), userName, emailAdd, first, last, SessionManager.getBirthdate(), add, SessionManager.getUserStatus(), SessionManager.getUserType(), SessionManager.getDeviceToken(), SessionManager.getCurrentLat(), SessionManager.getCurrentLong(), FirebaseDatabaseManager.parseObjectToFloat(homeLat), FirebaseDatabaseManager.parseObjectToFloat(homeLong), SessionManager.getUserPhoto());

        Intent i = new Intent(this, Sidebar_HomePage.class);
        startActivity(i);
        finish();
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
            showUpdateConfirmationDialog();
            return true;
        } else if (id == android.R.id.home) {
            displayDiscardDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showUpdateConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Update Profile");
        builder.setMessage("Are you sure you want to apply these changes?");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
               saveData();
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
                startActivity(
                        new Intent(SidebarSettings.this, Sidebar_HomePage.class));
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
}
