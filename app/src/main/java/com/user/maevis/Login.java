package com.user.maevis;

import android.content.Context;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.MotionEvent;
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
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
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

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

public class Login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,  View.OnTouchListener {

    private static final int RC_SIGN_IN = 234;
    private static final String TAG = "Login";
    private static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 10;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 11;
    private static GoogleSignInClient mGoogleSignInClient;
    private static GoogleApiClient googleApiClient;
    private static final int GOOGLE_REQ_CODE = 9001;


    private CallbackManager mCallbackManager;

    private TextView txtVwCreateAcc, txtVwForgotPass;
    private EditText txtFldLoginUsername, txtFldLoginPassword;
    private Button btnLogin, btnSignInFb, btnSignInGoogle;
    //private static FirebaseAuth mAuth;
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
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        ImageView maevis_logo = (ImageView)findViewById(R.id.imgLogo);
        maevis_logo.setImageResource(R.drawable.maevis_logo);

        //references
        txtVwCreateAcc = (TextView) findViewById(R.id.btnSignUp);
        txtVwForgotPass = (TextView) findViewById(R.id.btnForgotPassword);
        txtFldLoginUsername = (EditText) findViewById(R.id.txtFldLoginUsername);
        txtFldLoginPassword = (EditText) findViewById(R.id.txtFldLoginPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignInFb = (Button) findViewById(R.id.btnSignFb);
        btnSignInGoogle = (Button) findViewById(R.id.btnSignInGoogle);

        btnSignInFb.setVisibility(View.GONE);
        //firebase references
        //mAuth = FirebaseAuth.getInstance();
        firebaseUsers = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maevis-ecd17.firebaseio.com/Users");
        firebaseDevice = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maevis-ecd17.firebaseio.com/Device");

        progressDialog = new ProgressDialog(this, R.style.AlertDialogStyle); //instantiate a progress diaglog

        userModel = new UserModel();
        UserSession = new SessionManager(getApplicationContext());

        btnLogin.setOnTouchListener(this);
        btnLogin.setOnClickListener(this);
        btnSignInFb.setOnClickListener(this);
        btnSignInGoogle.setOnClickListener(this);
        txtVwCreateAcc.setOnClickListener(this);
        txtVwForgotPass.setOnClickListener(this);

        if(!isNetworkAvailable(getApplicationContext())) {
            showNoInternetConnection();
        }

        //checkPermissions();

        int Permission_All = 1;
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE};
        if(!hasPermissions(this, permissions)) {
            ActivityCompat.requestPermissions(this, permissions, Permission_All);
        }
    }

    @Override
    protected void onStart() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestProfile()
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        googleApiClient.connect();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        super.onStart();
        //SessionManager.getFirebaseAuth().addAuthStateListener(authStateListener);

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = SessionManager.getFirebaseAuth().getCurrentUser();

        if(currentUser != null){
            /*finish();
            startActivity(new Intent(this, Sidebar_HomePage.class));*/

            if(currentUser.isEmailVerified()) {
                finish();
                startActivity(new Intent(this, Sidebar_HomePage.class));
                updateUI();
            } else {
                finish();
                startActivity(new Intent(this, EmailVerification.class));
            }

        }

    }

    private void updateUI(){
        Toast.makeText(this, "You are loggin in.", Toast.LENGTH_LONG).show();
    }

    private void signInFb(){
        LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("email", "public_profile"));

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                final String userID = loginResult.getAccessToken().getUserId();
                final LoginResult loginRes = loginResult;

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        String profileImg = "https://graph.facebook.com/" +userID+ "/picture?type=normal";
                        //displayUserInfo(jsonObject, profileImg);
                        handleFacebookAccessToken(loginRes.getAccessToken(), jsonObject, profileImg);
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name, last_name, email, id, picture");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();

                Toast.makeText(Login.this, "Loggin in thru Fb", Toast.LENGTH_SHORT).show();
                //handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(Login.this, "FB ON CANCEL", Toast.LENGTH_SHORT).show();
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(Login.this, "FB ON ERROR", Toast.LENGTH_SHORT).show();
                // ...
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token, JSONObject obj, String profImg) {

        final JSONObject object = obj;
        final String profileImage = profImg;

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        SessionManager.getFirebaseAuth().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            /*
                            FirebaseUser user = SessionManager.getFirebaseAuth().getCurrentUser();

                            updateUI();*/
                            try {
                                final String email = object.getString("email");
                                String username = "NULL";
                                String password = "NULL";
                                String firstName = object.getString("first_name");
                                String lastName = object.getString("last_name");
                                String birthdate = "NULL";
                                String address = "NULL";
                                String userType = "Regular User";
                                String userStatus = "Active";
                                String deviceToken = FirebaseInstanceId.getInstance().getToken();
                                double homeLat = 10.316590;
                                double homeLong = 123.897093;
                                double currentLat = 10.316590;
                                double currentLong = 123.897093;
                                final String userPhoto = profileImage;

                                final UserModel userModel = new UserModel(address, birthdate, currentLat, currentLong, deviceToken, email, firstName, homeLat, homeLong, lastName, userPhoto, userStatus, userType, username);

                                if(!FirebaseDatabaseManager.isEmailUsed(email)) {
                                    FirebaseUser user = SessionManager.getFirebaseAuth().getCurrentUser();
                                    DatabaseReference newUser = FirebaseDatabaseManager.FirebaseUsers.child(user.getUid());
                                    newUser.setValue(userModel);

                                    float cLat = (float) userModel.getCurrentLat();
                                    float cLong= (float) userModel.getCurrentLong();
                                    float hLat = (float) userModel.getHomeLat();
                                    float hLong = (float) userModel.getHomeLong();

                                    SessionManager.createLoginSession(user.getUid(), userModel.getUsername(), userModel.getEmail(), userModel.getFirstName(), userModel.getLastName(), userModel.getBirthdate(), userModel.getAddress(), userModel.getUserStatus(), userModel.getUserType(), userModel.getDeviceToken(), cLat, cLong, hLat, hLong, userPhoto);
                                    showFBDetailsDialog(SessionManager.getFirstName(), SessionManager.getLastName(), SessionManager.getEmail(), SessionManager.getUserPhoto());
                                    Toast.makeText(Login.this, "New User with FB. Add to Firebase!!!", Toast.LENGTH_SHORT).show();
                                } else {
                                    FirebaseUser user = SessionManager.getFirebaseAuth().getCurrentUser();
                                    UserItem userItem = FirebaseDatabaseManager.getUserItem(user.getUid());
                                    SessionManager.createLoginSession(userItem);
                                    Toast.makeText(Login.this, "Existing user with FB. Logging in.", Toast.LENGTH_SHORT).show();

                                    //showFBDetailsDialog(userItem);
                                }

                                //showFBDetailsDialog(firstName, lastName, email, profileImage);
                                showFBDetailsDialog(SessionManager.getFirstName(), SessionManager.getLastName(), SessionManager.getEmail(), SessionManager.getUserPhoto());
                                //showFBDetailsDialog(firstName, lastName, email, profileImage);
                                //showFBDetailsDialog(userItem);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            // If sign in fails, display a message to the user.

                            if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                                showFacebookEmailUsed();
                                /*Toast.makeText(getApplicationContext(), "User with Email id already exists",
                                        Toast.LENGTH_SHORT).show();*/
                            } else {
                                Toast.makeText(Login.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
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
            signInGoogle();
            return;
        }

        if(v == txtVwCreateAcc) {
            //finish();
            Intent createAcc = new Intent(Login.this, SignUp.class);
            startActivity(createAcc);
        }

        if (v == txtVwForgotPass){
            Intent forgotPass = new Intent(Login.this, ForgotPass.class);
            startActivity(forgotPass);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        googleApiClient.stopAutoManage(this);
        googleApiClient.disconnect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.stopAutoManage(this);
            googleApiClient.disconnect();
        }
    }

    public static GoogleSignInClient getmGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    public static void setmGoogleSignInClient(GoogleSignInClient mGoogleSignInClient) {
        Login.mGoogleSignInClient = mGoogleSignInClient;
    }

    public static GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    public static void setGoogleApiClient(GoogleApiClient googleApiClient) {
        Login.googleApiClient = googleApiClient;
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
                Toast.makeText(this, "Google Sign In Failed.", Toast.LENGTH_LONG).show();
                // ...
            }

        }else{
            // Pass the activity result back to the Facebook SDK
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        final String email = acct.getEmail();
        String username = "NULL";
        String password = "NULL";
        String firstName = acct.getGivenName();
        String lastName = acct.getFamilyName();
        String birthdate = "NULL";
        String address = "NULL";
        String userType = "Regular User";
        String userStatus = "Active";
        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        double homeLat = 10.316590;
        double homeLong = 123.897093;
        double currentLat = 10.316590;
        double currentLong = 123.897093;
        final String userPhoto = acct.getPhotoUrl().toString();
        final UserModel userModel = new UserModel(address, birthdate, currentLat, currentLong, deviceToken, email, firstName, homeLat, homeLong, lastName, userPhoto, userStatus, userType, username);

        //showGoogleAccountDialog(firstName, email, userPhoto);
        progressDialog.setMessage("Signing in with Google.");
        progressDialog.show();

        SessionManager.getFirebaseAuth().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                                showFacebookEmailUsed();
                                /*Toast.makeText(getApplicationContext(), "User with Email id already exists",
                                        Toast.LENGTH_SHORT).show();*/
                                return;
                            }

                            if(!FirebaseDatabaseManager.isEmailUsed(email)) {
                                FirebaseUser user = SessionManager.getFirebaseAuth().getCurrentUser();
                                DatabaseReference newUser = FirebaseDatabaseManager.FirebaseUsers.child(user.getUid());
                                newUser.setValue(userModel);

                                float cLat = (float) userModel.getCurrentLat();
                                float cLong= (float) userModel.getCurrentLong();
                                float hLat = (float) userModel.getHomeLat();
                                float hLong = (float) userModel.getHomeLong();

                                SessionManager.createLoginSession(user.getUid(), userModel.getUsername(), userModel.getEmail(), userModel.getFirstName(), userModel.getLastName(), userModel.getBirthdate(), userModel.getAddress(), userModel.getUserStatus(), userModel.getUserType(), userModel.getDeviceToken(), cLat, cLong, hLat, hLong, userPhoto);
                                Toast.makeText(Login.this, "New User. Add to Firebase!!!", Toast.LENGTH_SHORT).show();
                            } else {
                                FirebaseUser user = SessionManager.getFirebaseAuth().getCurrentUser();
                                UserItem userItem = FirebaseDatabaseManager.getUserItem(user.getUid());
                                SessionManager.createLoginSession(userItem);
                                Toast.makeText(Login.this, "Existing user. Logging in.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                                showFacebookEmailUsed();
                            }
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        progressDialog.dismiss();
                    }
                });
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void loginUser() {
        final String username = txtFldLoginUsername.getText().toString().trim();
        final String password = txtFldLoginPassword.getText().toString().trim();

        //validations
        if (TextUtils.isEmpty(username)) {
            txtFldLoginUsername.setError("Please input your username.");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            txtFldLoginPassword.setError("Please input your password.");
            return;
        }

        if(isNetworkAvailable(getApplicationContext())) {
            FirebaseDatabaseManager.FirebaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterator<DataSnapshot> users = dataSnapshot.getChildren().iterator();
                    DeviceModel deviceModel;

                    while (users.hasNext()) {
                        DataSnapshot user = users.next();

                        if (user.child("username").getValue().toString().equals(username)) {
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
                                        //Toast.makeText(Login.this, "User authentication problem. " + SessionManager.getEmail(), Toast.LENGTH_LONG).show();
                                        showAuthProbDialog();
                                    } else {
                                        //FirebaseDatabaseManager.FirebaseUsers.child(SessionManager.getUserID()).child("deviceToken").setValue(SessionManager.getDeviceToken());
                                        Toast.makeText(Login.this, "Logged in as: " + SessionManager.getFirstName() + " " + SessionManager.getLastName() + " " + SessionManager.getUserStatus(), Toast.LENGTH_SHORT).show();

                                        FirebaseUser user = SessionManager.getFirebaseAuth().getCurrentUser();
                                        /*finish();
                                        startActivity(new Intent(Login.this, Sidebar_HomePage.class));*/

                                        if(user.isEmailVerified()) {
                                            finish();
                                            startActivity(new Intent(Login.this, Sidebar_HomePage.class));
                                        } else {
                                            finish();
                                            startActivity(new Intent(Login.this, EmailVerification.class));
                                        }

                                    }
                                    //progressDialog.dismiss();
                                }
                            });

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
                    showNoInternetConnection();
                }
            });
        } else {
            showNoInternetConnection();
        }
    }

    public boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                || (connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState() == NetworkInfo.State.CONNECTED)) {
            return true;
        } else {
            return false;
        }
    }

    /*private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }*/

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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            btnLogin.setBackgroundResource(R.drawable.btn_rounded);
            btnLogin.setTextColor(Color.WHITE);
        } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            btnLogin.setBackgroundResource(R.drawable.btn_rounded1);
            btnLogin.setTextColor(Color.BLACK);
        }
        return false;
    }

    private void showNoInternetConnection() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Network Connection Problem");
        builder.setMessage("There seems to be a problem with your network. Please check your network connection and try again.");
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

    private void showAuthProbDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Authentication Problem");
        builder.setMessage("There seems to be a problem authenticating your account. Please try again in a few moments.");
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

    public static boolean hasPermissions(Context context, String... permissions) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for(String permission : permissions) {
                if(ActivityCompat.checkSelfPermission(context, permission)!=PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }

        return true;
    }

    public void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
        } else {
            notifyStorageGranted();
        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            notifyLocationGranted();
        }
    }

    private void notifyStorageGranted() {
        Toast.makeText(this, "Storage Permission granted.", Toast.LENGTH_LONG).show();
    }

    private void notifyLocationGranted() {
        Toast.makeText(this, "Location Permission granted.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    notifyStorageGranted();
                } else {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        enableStoragePermissionDialog();
                    } else {
                        new AlertDialog.Builder(this).
                                setTitle("Storage Permission Denied").
                                setMessage("You denied read Storage Permission. Report feature will be disabled. To use this feature, you can enable Storage Permissions in your Settings at a later time.").show();
                    }
                }
                break;
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    notifyLocationGranted();
                } else {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        enableLocationPermissionDialog();
                    } else {
                        new AlertDialog.Builder(this).
                                setTitle("Location Permission Denied").
                                setMessage("You denied read Location Permission. Report feature will be disabled. To use this feature, you can enable Location Permissions in your Settings at a later time.").show();
                    }
                }
                break;
        }
    }

    public void enableStoragePermissionDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Enable Storage Permission");
        builder.setMessage("You need to grant Storage permission to send out reports with authenticity using photos from your device.");
        builder.setInverseBackgroundForced(true);
        builder.setNegativeButton("ENABLE STORAGE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        android.app.AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    public void enableLocationPermissionDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Enable Location Permission");
        builder.setMessage("You need to grant Location permission to send out reports with with your current location.");
        builder.setInverseBackgroundForced(true);
        builder.setNegativeButton("ENABLE LOCATION", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        android.app.AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    public void showFacebookEmailUsed() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Facebook Email Already Used");
        builder.setMessage("The email address associated with your Facebook account has already been used.");
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

    public void showFBDetailsDialog(String firstName, String lastName, String email, String profileImage) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Facebook Logged In");
        builder.setMessage("FirstName: "+firstName+" LastName: "+lastName+" Email: "+email+" FBURL: "+profileImage);
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

    public void showFBDetailsDialog(UserItem userItem) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}