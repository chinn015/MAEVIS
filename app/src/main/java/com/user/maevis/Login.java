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
