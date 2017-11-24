package com.user.maevis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import java.util.Calendar;

import android.support.annotation.NonNull;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.user.maevis.models.UserModel;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference FirebaseUsers;
    private EditText txtFldUsername;
    private EditText txtFldPassword;
    private EditText txtFldFirstName;
    private EditText txtFldLastName;
    private EditText txtFldEmail;
    private EditText txtFldBirthdate;
    private EditText txtFldAddress;
    private Button btnCreateAccount;
    //private Button btnLogin;
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

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        FirebaseUsers = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maevis-ecd17.firebaseio.com/Users");

        txtFldUsername = (EditText) findViewById(R.id.txtFldSignUpUsername);
        txtFldPassword = (EditText) findViewById(R.id.txtFldSignUpPassword);
        txtFldFirstName = (EditText) findViewById(R.id.txtFldFirstName);
        txtFldLastName = (EditText) findViewById(R.id.txtFldLastName);
        txtFldEmail = (EditText) findViewById(R.id.txtFldEmail);
        txtFldBirthdate = (EditText) findViewById(R.id.txtFldBirthdate);
        txtFldAddress = (EditText) findViewById(R.id.txtFldAddress);

        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        //btnLogin = (Button) findViewById(R.id.btnLogin);

        txtFldBirthdate.setOnClickListener(this);
        btnCreateAccount.setOnClickListener(this);
        //btnLogin.setOnClickListener(this);

        currentDate = Calendar.getInstance();
        day = currentDate.get(Calendar.DAY_OF_MONTH);
        month = currentDate.get(Calendar.MONTH);
        year = currentDate.get(Calendar.YEAR);
        month = month+1;

        txtFldBirthdate.setHint("Birthdate ["+day+"/"+month+"/"+year+"]");
    }

    @Override
    public void onClick(View v) {
        if(v == btnCreateAccount) {
            createAccount();
        }

        /*if(v == btnLogin) {
            Intent loginPage = new Intent(SignUp.this, Login.class);
            finish();
            startActivity(loginPage);
        }*/

        if(v == txtFldBirthdate) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(SignUp.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int birthYear, int birthMonth, int birthDay) {
                    birthMonth = birthMonth+1;
                    txtFldBirthdate.setText(birthDay+"/"+birthMonth+"/"+birthYear);
                }
            }, year, month, day);
            datePickerDialog.show();
        }
    }

    private void createAccount() {
        //String primaryKey = Integer.toString(++pk);

        String email = txtFldEmail.getText().toString();
        String username = txtFldUsername.getText().toString();
        String password = txtFldPassword.getText().toString();
        String firstName = txtFldFirstName.getText().toString();
        String lastName = txtFldLastName.getText().toString();
        String birthdate = txtFldBirthdate.getText().toString();
        String address = txtFldAddress.getText().toString();

        if(TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please enter your username.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(firstName)) {
            Toast.makeText(this, "Enter your first name.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(lastName)) {
            Toast.makeText(this, "Enter your last name.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(birthdate)) {
            Toast.makeText(this, "Enter your birthdate.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Enter your first address.", Toast.LENGTH_SHORT).show();
            return;
        }

        //final UserModel userModel = new UserModel(email, username, password);
        final UserModel userModel = new UserModel(username, password, email, firstName, lastName, birthdate, address);

        progressDialog.setMessage("Registering User.");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if(task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                            storeData(userModel);
                            finish();
                            startActivity(new Intent(getApplicationContext(), Login.class));
                        } else {
                            Toast.makeText(SignUp.this, "Registration Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void storeData(UserModel userModel) {
        DatabaseReference newUser = FirebaseUsers.child(userModel.getUsername());
        newUser.setValue(true);
        newUser.child("Username").setValue(userModel.getUsername());
        newUser.child("Password").setValue(userModel.getPassword());
        newUser.child("Email Address").setValue(userModel.getEmail());
        newUser.child("First Name").setValue(userModel.getFirstName());
        newUser.child("Last Name").setValue(userModel.getLastName());
        newUser.child("Birthdate").setValue(userModel.getBirthdate());
        newUser.child("Address").setValue(userModel.getAddress());
    }
}
