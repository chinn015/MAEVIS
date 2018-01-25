package com.user.maevis;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Base64;
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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 234;
    private static final String TAG = "Login";
    GoogleSignInClient mGoogleSignInClient;

    private CallbackManager mCallbackManager;

    private TextView txtVwCreateAcc;
    private EditText txtFldLoginUsername, txtFldLoginPassword;
    private Button btnLogin, btnSignInFb, btnSignInGoogle;
    private static FirebaseAuth mAuth;
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

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.user.maevis",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestProfile()
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

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
        mAuth = FirebaseAuth.getInstance();
        firebaseUsers = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maevis-ecd17.firebaseio.com/Users");
        firebaseDevice = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maevis-ecd17.firebaseio.com/Device");

        progressDialog = new ProgressDialog(this, R.style.AlertDialogStyle); //instantiate a progress diaglog

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

                    //FirebaseDatabaseManager.FirebaseUsers.child(SessionManager.getUserID()).child("deviceToken").setValue(SessionManager.getDeviceToken());

                    new CountDownTimer(500, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            FirebaseDatabaseManager.FirebaseUsers.child(SessionManager.getUserID()).child("deviceToken").setValue(SessionManager.getDeviceToken());

                            SessionManager.setCurrentLatLong((float) Tab2_Location.userLatitude, (float) Tab2_Location.userLongitude);

                            FirebaseDatabaseManager.FirebaseUsers.child(SessionManager.getUserID()).child("currentLat").setValue(SessionManager.getCurrentLat());
                            FirebaseDatabaseManager.FirebaseUsers.child(SessionManager.getUserID()).child("currentLong").setValue(SessionManager.getCurrentLong());
                        }

                    }.start();

 
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

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            updateUI();
        }

    }

    private void updateUI(){
        Toast.makeText(this, "You are loggin in.", Toast.LENGTH_LONG).show();
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if(v == btnLogin) {
            loginUser();
            return;
        }

        if(v == btnSignInFb) {
            signInFb();
            return;
        }

        if(v == btnSignInGoogle) {
            signIn();
            return;
        }

        if(v == txtVwCreateAcc) {
            finish();
            Intent createAcc = new Intent(Login.this, SignUp.class);
            startActivity(createAcc);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }

        }else{
            // Pass the activity result back to the Facebook SDK
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);

                            Toast.makeText(Login.this, "User signed in using Google Sign In", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signInFb(){
        LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("email", "public_profile"));

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);

                //Toast.makeText(Login.this, "Loggin in thru Fb", Toast.LENGTH_SHORT).show();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });
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
                        String sUserStatus = user.child("userStatus").getValue().toString();
                        String sUserType = user.child("userType").getValue().toString();
                        String sDeviceToken = FirebaseInstanceId.getInstance().getToken();
                        float currentLat = (float) Tab2_Location.userLatitude;
                        float currentLong = (float) Tab2_Location.userLongitude;
                        float homeLat = FirebaseDatabaseManager.parseObjectToFloat(user.child("homeLat").getValue());
                        float homeLong = FirebaseDatabaseManager.parseObjectToFloat(user.child("homeLong").getValue());
                        String userPhoto = user.child("userPhoto").getValue().toString();

                        if (sDeviceToken.equals("")) {
                            sDeviceToken = "NULL";
                        }

                        SessionManager.createLoginSession(sUserID, sUsername, sEmail, sFirstName, sLastName, sBirthdate, sAddress, sUserStatus, sUserType, sDeviceToken, currentLat, currentLong, homeLat, homeLong, userPhoto);

                        progressDialog.setMessage("Logging in.");
                        progressDialog.show();

                        SessionManager.getFirebaseAuth().signInWithEmailAndPassword(SessionManager.getEmail(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(Login.this, "User authentication problem. " + SessionManager.getEmail(), Toast.LENGTH_LONG).show();
                                } else {
                                    FirebaseDatabaseManager.FirebaseUsers.child(SessionManager.getUserID()).child("deviceToken").setValue(SessionManager.getDeviceToken());
                                    Toast.makeText(Login.this, "Logged in as: " + SessionManager.getFirstName() + " " + SessionManager.getLastName() + " " + SessionManager.getUserStatus(), Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();
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
                        //Toast.makeText(Login.this, "Account not found. Please double-check and try again.", Toast.LENGTH_SHORT).show();
                        showAccountNotFoundDialog(username);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

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

    private void showAccountNotFoundDialog(final String username) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Account not found");
        builder.setMessage("It looks like " + username + " doesn't match an existing account. Please double check and try again.");
        builder.setInverseBackgroundForced(true);
        builder.setNegativeButton("RETURN", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        android.app.AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(Color.BLACK);

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