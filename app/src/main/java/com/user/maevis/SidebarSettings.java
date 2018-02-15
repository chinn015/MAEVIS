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


public class SidebarSettings extends AppCompatActivity {

    private EditText username, password, fName, lName, email, conPassword;

    private DatabaseReference dbUsername, dbPassword, dbFName, dbLName, dbEmail, dbAddress, dbUserPhoto;

    private static final String REQUIRED = "Required";

    private static final String TAG = "SidebarSettings";

    private String oldPass;

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
        setContentView(R.layout.sidebar_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this, R.style.AlertDialogStyle);

        //User logged in
        username = (EditText) findViewById(R.id.txtFldEditUsername);
        fName = (EditText) findViewById(R.id.txtFldEditFirstname);
        lName = (EditText) findViewById(R.id.txtFldEditLastname);
        email = (EditText) findViewById(R.id.txtFldEditEmail);
        txtFldAddress = (AutofitTextView) findViewById(R.id.txtFldEditAddress);
        ivImage = (CircleImageView) findViewById(R.id.imgChangePhoto);

        Picasso.with(this).load(SessionManager.getUserPhoto()).into(ivImage);

        dbUsername = FirebaseDatabase.getInstance().getReference().child("Users").child(SessionManager.getUserID()).child("username");
        dbFName = FirebaseDatabase.getInstance().getReference().child("Users").child(SessionManager.getUserID()).child("firstName");
        dbLName = FirebaseDatabase.getInstance().getReference().child("Users").child(SessionManager.getUserID()).child("lastName");
        dbEmail = FirebaseDatabase.getInstance().getReference().child("Users").child(SessionManager.getUserID()).child("email");
        dbAddress = FirebaseDatabase.getInstance().getReference().child("Users").child(SessionManager.getUserID()).child("address");
        dbUserPhoto = FirebaseDatabase.getInstance().getReference().child("Users").child(SessionManager.getUserID()).child("userPhoto");

        ivImage.setOnClickListener(new View.OnClickListener() {
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
                final String userEmail = email.getText().toString();
                final String userFname = fName.getText().toString();
                final String userLname = lName.getText().toString();

                i.putExtra("userName", userName);
                i.putExtra("userEmail", userEmail);
                i.putExtra("userFname", userFname);
                i.putExtra("userLname", userLname);

                startActivity(i);
                //finish();
            }
        });


        if(UpdateHomeAddress.userHomeAddress != null){
            Intent i = getIntent();
            username.setText(UpdateHomeAddress.userName);
            fName.setText(UpdateHomeAddress.userFname);
            lName.setText(UpdateHomeAddress.userLname);
            email.setText(UpdateHomeAddress.userEmail);
            txtFldAddress.setText(UpdateHomeAddress.userHomeAddress);

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            displayDiscardDialog();
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
        final String userName = username.getText().toString().trim();
        final String first = fName.getText().toString().trim();
        final String last = lName.getText().toString().trim();
        final String emailAdd = email.getText().toString().trim();
        final String add = txtFldAddress.getText().toString().trim();
        final Double homeLat = UpdateHomeAddress.userHomeLat;
        final Double homeLong = UpdateHomeAddress.userHomeLong;

        // Username is required
        if (TextUtils.isEmpty(userName)) {
            username.setError(REQUIRED);
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

        if(userName.length() < 8){
            username.setError("Username must have at least 8 characters");
            return;
        }


        if(isEmailValid(emailAdd) != true){
            email.setError("Please enter a valid email address.");
            return;
        }

        dbUsername = FirebaseDatabase.getInstance().getReference();
        dbUsername.child("Users").child(SessionManager.getUserID()).child("username").setValue(userName);
        //dbUsername.child("Users").child(SessionManager.getUserID()).child("password").setValue(pass);
        dbUsername.child("Users").child(SessionManager.getUserID()).child("firstName").setValue(first);
        dbUsername.child("Users").child(SessionManager.getUserID()).child("lastName").setValue(last);
        dbUsername.child("Users").child(SessionManager.getUserID()).child("email").setValue(emailAdd);
        dbUsername.child("Users").child(SessionManager.getUserID()).child("address").setValue(add);

        if(homeLat == 0 && homeLong == null){
            dbUsername.child("Users").child(SessionManager.getUserID()).child("homeLat").setValue(SessionManager.getHomeLat());
            dbUsername.child("Users").child(SessionManager.getUserID()).child("homeLong").setValue(SessionManager.getHomeLong());
        }else{
            dbUsername.child("Users").child(SessionManager.getUserID()).child("homeLat").setValue(homeLat);
            dbUsername.child("Users").child(SessionManager.getUserID()).child("homeLong").setValue(homeLong);
        }

        if(finalImageURI != null) {
            dbUsername.child("Users").child(SessionManager.getUserID()).child("userPhoto").setValue(getImageURL());
        }else{
            dbUsername.child("Users").child(SessionManager.getUserID()).child("userPhoto").setValue(SessionManager.getUserPhoto());
        }






        if(finalImageURI != null) {
            SessionManager.updateSession(SessionManager.getUserID(), userName, emailAdd, first, last, SessionManager.getBirthdate(), add, SessionManager.getUserStatus(), SessionManager.getUserType(), SessionManager.getDeviceToken(), SessionManager.getCurrentLat(), SessionManager.getCurrentLong(), FirebaseDatabaseManager.parseObjectToFloat(homeLat), FirebaseDatabaseManager.parseObjectToFloat(homeLong), getImageURL());
        }else{
            SessionManager.updateSession(SessionManager.getUserID(), userName, emailAdd, first, last, SessionManager.getBirthdate(), add, SessionManager.getUserStatus(), SessionManager.getUserType(), SessionManager.getDeviceToken(), SessionManager.getCurrentLat(), SessionManager.getCurrentLong(), FirebaseDatabaseManager.parseObjectToFloat(homeLat), FirebaseDatabaseManager.parseObjectToFloat(homeLong), SessionManager.getUserPhoto());
        }

        Intent i = new Intent(this, Sidebar_HomePage.class);
        startActivity(i);
        finish();
    }

    private void SelectImage(){


        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(SidebarSettings.this);
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

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

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
               //saveData();
                if (finalImageURI != null) {
                    StorageReference filePath = FirebaseDatabaseManager.FirebasePhotoStorage.child("UserPhotos").child(finalImageURI.getLastPathSegment());

                    progressDialog.setMessage("Updating profile.");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);

                    try {

                        Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), finalImageURI);
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
                        String path = MediaStore.Images.Media.insertImage(SidebarSettings.this.getContentResolver(), bmp, finalImageURI.getLastPathSegment(), null);
                        finalImageURI = Uri.parse(path);

                        filePath.putFile(finalImageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                setImageURL(downloadUrl.toString());

                                saveData();

                                Toast.makeText(SidebarSettings.this, "Update Successful!.", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SidebarSettings.this, "Photo upload failed!", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        });
                    } catch (IOException ie) {
                        Toast.makeText(SidebarSettings.this, "Update error.", Toast.LENGTH_LONG).show();
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
