package com.user.maevis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.app.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import java.util.Calendar;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    DatabaseReference FirebaseUsers;
    private EditText txtFldUsername;
    private EditText txtFldPassword;
    private EditText txtFldFirstName;
    private EditText txtFldLastName;
    private EditText txtFldEmail;
    private EditText txtFldBirthdate;
    private EditText txtFldAddress;
    private Button btnCreateAccount;
<<<<<<< HEAD
=======

>>>>>>> 37ee98514508da4575c9018faf9b80f664f82132
    private Calendar currentDate;
    private int day, month, year;
    private int pk;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up);

        ImageView maevis_logo = (ImageView)findViewById(R.id.imgLogo);
        maevis_logo.setImageResource(R.drawable.maevis_logo);

    }


}
