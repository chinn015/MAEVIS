package com.user.maevis;

import android.content.Intent;
import android.os.Bundle;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.user.maevis.models.UserModel;
import com.user.maevis.session.SessionManager;

import java.util.HashMap;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private TextView txtVwCreateAcc;
    private EditText txtFldLoginUsername, txtFldLoginPassword;
    private Button btnLogin, btnSignInFb, btnSignInGoogle;
    private FirebaseAuth firebaseAuth;
    //private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference firebaseUsers;
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
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUsers = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maevis-ecd17.firebaseio.com/Users");

        progressDialog = new ProgressDialog(this); //instantiate a progress diaglog

        userModel = new UserModel();
        UserSession = new SessionManager(getApplicationContext());

        btnLogin.setOnClickListener(this);
        btnSignInFb.setOnClickListener(this);
        btnSignInGoogle.setOnClickListener(this);
        txtVwCreateAcc.setOnClickListener(this);

        /*authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null) {
                    finish();
                    startActivity(new Intent(Login.this, HomePage.class));
                }
            }
        };*/
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }*/

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

        Log.v("E_VALUE", "Username: "+username+"  ||  Password: "+password);

        //validations
        if(TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please enter your username.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(username).exists()) {
                    if(dataSnapshot.child(username).child("Password").getValue().toString().equals(password)) {
                        String sUserID = dataSnapshot.getKey().toString();
                        String sUsername = dataSnapshot.child(username).child("Username").getValue().toString();
                        String sEmail = dataSnapshot.child(username).child("Email Address").getValue().toString();
                        String sFirstName = dataSnapshot.child(username).child("First Name").getValue().toString();
                        String sLastName = dataSnapshot.child(username).child("Last Name").getValue().toString();
                        String sBirthdate = dataSnapshot.child(username).child("Birthdate").getValue().toString();
                        String sAddress = dataSnapshot.child(username).child("Address").getValue().toString();

                        SessionManager.createLoginSession(sUserID, sUsername, sEmail, sFirstName, sLastName, sBirthdate, sAddress);
                        HashMap<String, String> user = UserSession.getUserSessionDetails();
                        //Toast.makeText(Login.this, "Logged in as: "+dataSnapshot.child(username).child("Username").getValue().toString(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(Login.this, "Logged in as: "+user.get(SessionManager.KEY_FIRSTNAME)+" "+user.get(SessionManager.KEY_LASTNAME));

                        finish();
                        startActivity(new Intent(Login.this, Sidebar_HomePage.class));

                        /*firebaseAuth.signInWithEmailAndPassword(userModel.getEmail(), userModel.getPassword())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(!task.isSuccessful()){
                                            Toast.makeText(Login.this, "Login failed.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });*/
                    } else {
                        Toast.makeText(Login.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "Invalid Username.", Toast.LENGTH_SHORT).show();
                }

                /*Iterator<DataSnapshot> users = dataSnapshot.getChildren().iterator();
                Toast.makeText(Login.this, "Total Number of Users: "+dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
                while(users.hasNext()) {
                    DataSnapshot user = users.next();

                    if(user.child("Username").getValue().toString().equals(username) &&
                            user.child("Password").getValue().toString().equals(password)) {
                        userModel.setUsername(user.child("Username").getValue().toString());
                        userModel.setPassword(user.child("Password").getValue().toString());
                    }

                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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