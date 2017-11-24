package com.user.maevis;

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

//public class Login extends AppCompatActivity implements View.OnClickListener {
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

//        //references
//        txtVwCreateAcc = (TextView) findViewById(R.id.btnSignUp);
//        txtFldLoginUsername = (EditText) findViewById(R.id.txtFldLoginUsername);
//        txtFldLoginPassword = (EditText) findViewById(R.id.txtFldLoginPassword);
//        btnLogin = (Button) findViewById(R.id.btnLogin);
//        btnSignInFb = (Button) findViewById(R.id.btnSignFb);
//        btnSignInGoogle = (Button) findViewById(R.id.btnSignInGoogle);
//
//        //firebase references
//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseUsers = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maevis-ecd17.firebaseio.com/Users");
//
//        progressDialog = new ProgressDialog(this); //instantiate a progress diaglog
//        userInformation = new UserInformation();

//        btnLogin.setOnClickListener(this);
//        btnSignInFb.setOnClickListener(this);
//        btnSignInGoogle.setOnClickListener(this);
//        txtVwCreateAcc.setOnClickListener(this);
    }

    /*public void onSignUp(View v){
        if(v.getId() == R.id.btnSignUp){
            Intent i = new Intent(this, SignUp.class);
            startActivity(i);
        }
    }*/

    public void onLogin(View v) {
        if (v.getId() == R.id.btnLogin) {
            Intent i = new Intent(this, Sidebar_HomePage.class);
            startActivity(i);
        }
    }

//    @Override
//    public void onClick(View v) {
//        if(v == btnLogin) {
//            loginUser();
//            return;
//        }
//
//        if(v == btnSignInFb) {
//            return;
//        }
//
//        if(v == btnSignInGoogle) {
//            return;
//        }
//
//        if(v == txtVwCreateAcc) {
//            finish();
//            Intent createAcc = new Intent(Login.this, SignUp.class);
//            startActivity(createAcc);
//        }
//    }
//
//    public void loginUser() {
//        String username = txtFldLoginUsername.getText().toString();
//        String password = txtFldLoginPassword.getText().toString();
//
//        Log.v("E_VALUE", "Username: "+username+"  ||  Password: "+password);
//
//        //validations
//        if(TextUtils.isEmpty(username)) {
//            Toast.makeText(this, "Please enter your username.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if(TextUtils.isEmpty(password)) {
//            Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        retrieveUserFromDatabase(username, password);
//    }
//
//    public void retrieveUserFromDatabase(final String username, final String password) {
//        firebaseUsers.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if(dataSnapshot.child(username).exists()) {
//                    if(dataSnapshot.child(username).child("Password").getValue().toString().equals(password)) {
//                        userInformation.setUsername(dataSnapshot.child(username).child("Username").getValue().toString());
//                        userInformation.setPassword(dataSnapshot.child(username).child("Password").getValue().toString());
//                        userInformation.setEmail(dataSnapshot.child(username).child("Email Address").getValue().toString());
//                        Toast.makeText(Login.this, "Logged in as: "+dataSnapshot.child(username).child("Username").getValue().toString(), Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(Login.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(Login.this, "Invalid Username.", Toast.LENGTH_SHORT).show();
//                }
//
//                /*Iterator<DataSnapshot> users = dataSnapshot.getChildren().iterator();
//                Toast.makeText(Login.this, "Total Number of Users: "+dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
//                while(users.hasNext()) {
//                    DataSnapshot user = users.next();
//
//                    if(user.child("Username").getValue().toString().equals(username) &&
//                            user.child("Password").getValue().toString().equals(password)) {
//                        userInformation.setUsername(user.child("Username").getValue().toString());
//                        userInformation.setPassword(user.child("Password").getValue().toString());
//                    }
//
//                }*/
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
}
