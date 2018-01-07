package com.user.maevis;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.user.maevis.models.DeviceModel;
import com.user.maevis.models.FirebaseDatabaseManager;
import com.user.maevis.models.UserModel;
import com.user.maevis.session.SessionManager;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private TextView txtVwCreateAcc;
    private EditText txtFldLoginUsername, txtFldLoginPassword;
    private Button btnLogin, btnSignInFb, btnSignInGoogle;
    //private static FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference firebaseUsers, firebaseDevice;
    public static DatabaseReference newDevice;
    private ProgressDialog progressDialog;

    UserModel userModel;
    SessionManager UserSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        ImageView maevis_logo = (ImageView)findViewById(R.id.imgLogo);
        maevis_logo.setImageResource(R.drawable.maevis_logo);

        //references
        txtVwCreateAcc = (TextView) findViewById(R.id.btnSignUp);
        txtFldLoginUsername = (EditText) findViewById(R.id.txtFldLoginUsername);
        txtFldLoginPassword = (EditText) findViewById(R.id.txtFldLoginPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignInFb = (Button) findViewById(R.id.btnSignFb);
        btnSignInGoogle = (Button) findViewById(R.id.btnSignInGoogle);

        //firebase references
        //firebaseAuth = FirebaseAuth.getInstance();
        firebaseUsers = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maevis-ecd17.firebaseio.com/Users");
        firebaseDevice = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maevis-ecd17.firebaseio.com/Device");

        progressDialog = new ProgressDialog(this); //instantiate a progress diaglog

        userModel = new UserModel();
        UserSession = new SessionManager(getApplicationContext());

        btnLogin.setOnClickListener(this);
        btnSignInFb.setOnClickListener(this);
        btnSignInGoogle.setOnClickListener(this);
        txtVwCreateAcc.setOnClickListener(this);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null) {
                    finish();
                    startActivity(new Intent(Login.this, Sidebar_HomePage.class));
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        SessionManager.getFirebaseAuth().addAuthStateListener(authStateListener);
    }

    /*public void onSignUp(View v){
        if(v.getId() == R.id.btnSignUp){
            Intent i = new Intent(this, SignUp.class);
            startActivity(i);
        }
    }*/

    /*public void onLogin(View v) {
        if (v.getId() == R.id.btnLogin) {
            Intent i = new Intent(this, HomePage.class);
            startActivity(i);
        }
    }*/

    @Override
    public void onClick(View v) {
        if(v == btnLogin) {
            loginUser();
            return;
        }

        if(v == btnSignInFb) {
            return;
        }

        if(v == btnSignInGoogle) {
            return;
        }

        if(v == txtVwCreateAcc) {
            finish();
            Intent createAcc = new Intent(Login.this, SignUp.class);
            startActivity(createAcc);
        }
    }

    public void loginUser() {
        final String username = txtFldLoginUsername.getText().toString();
        final String password = txtFldLoginPassword.getText().toString();

        Log.v("E_VALUE", "Username: " + username + "  ||  Password: " + password);

        //validations
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please enter your username.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> users = dataSnapshot.getChildren().iterator();
                DeviceModel deviceModel;

                while (users.hasNext()) {
                    DataSnapshot user = users.next();

                    if (user.child("username").getValue().toString().equals(username) &&
                            user.child("password").getValue().toString().equals(password)) {
                        String sUserID = user.getKey().toString();
                        String sUsername = user.child("username").getValue().toString();
                        String sEmail = user.child("email").getValue().toString();
                        String sFirstName = user.child("firstName").getValue().toString();
                        String sLastName = user.child("lastName").getValue().toString();
                        String sBirthdate = user.child("birthdate").getValue().toString();
                        String sAddress = user.child("address").getValue().toString();
                        String sDeviceToken = FirebaseInstanceId.getInstance().getToken();

                        SessionManager.createLoginSession(sUserID, sUsername, sEmail, sFirstName, sLastName, sBirthdate, sAddress);

                        progressDialog.setMessage("Logging in.");
                        progressDialog.show();

                        SessionManager.getFirebaseAuth().signInWithEmailAndPassword(SessionManager.getEmail(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(Login.this, "User authentication problem.", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(Login.this, "Logged in as: " + SessionManager.getFirstName() + " " + SessionManager.getLastName(), Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(Login.this, "User authentication success!", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });


                        Log.d("token", sDeviceToken);
                        /*
                        deviceModel = new DeviceModel(SessionManager.getKeyDeviceToken(), SessionManager.getUserID());
                        newDevice = firebaseDevice.push();
                        newDevice.setValue(deviceModel);
                        */


                        return;

                    } else if (!users.hasNext()) {
                        Toast.makeText(Login.this, "Account not found. Please double-check and try again.", Toast.LENGTH_SHORT).show();
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        firebaseDevice.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                DeviceModel deviceModel;
//                DeviceModel device = new DeviceModel(
//                        dataSnapshot.child("deviceToken").getValue().toString(),
//                        dataSnapshot.child("deviceUser").getValue().toString()
//                       );
//
//                if(device.getDeviceToken().equals(SessionManager.getKeyDeviceToken())){
//                    Log.d("token-session", SessionManager.getKeyDeviceToken());
//                    Log.d("token-db", device.getDeviceToken());
//                } else {
//                    deviceModel = new DeviceModel(SessionManager.getKeyDeviceToken(), SessionManager.getUserID());
//                    newDevice = firebaseDevice.push();
//                    newDevice.setValue(deviceModel);
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }

}

















/*package com.user.maevis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Login extends AppCompatActivity {
    private TextView txtVwCreateAcc;
    private EditText txtFldLoginUsername, txtFldLoginPassword;
    private Button btnLogin, btnSignInFb, btnSignInGoogle;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseUsers;

    private ProgressDialog progressDialog;

    UserInformation userInformation;
    //UserSessionManager UserSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        ImageView maevis_logo = (ImageView)findViewById(R.id.imgLogo);
        maevis_logo.setImageResource(R.drawable.maevis_logo);

    }

    public void onSignUp(View v){
        if(v.getId() == R.id.btnSignUp){
            Intent i = new Intent(this, SignUp.class);
            startActivity(i);
        }
    }

    public void onLogin(View v) {
        if (v.getId() == R.id.btnLogin) {
            Intent i = new Intent(this, Sidebar_HomePage.class);
            startActivity(i);
        }
    }

}
*/