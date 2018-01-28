package com.user.maevis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.user.maevis.models.FirebaseDatabaseManager;
import com.user.maevis.models.UserModel;
import com.user.maevis.session.SessionManager;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference FirebaseUsers;
    private EditText txtFldUsername;
    private EditText txtFldPassword;
    private EditText txtFldFirstName;
    private EditText txtFldLastName;
    private EditText txtFldEmail;
    private EditText txtFldBirthdate;
    private TextView txtFldAddress;
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
        progressDialog = new ProgressDialog(this, R.style.AlertDialogStyle);

        FirebaseUsers = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maevis-ecd17.firebaseio.com/Users");

        txtFldUsername = (EditText) findViewById(R.id.txtFldSignUpUsername);
        txtFldPassword = (EditText) findViewById(R.id.txtFldSignUpPassword);
        txtFldFirstName = (EditText) findViewById(R.id.txtFldFirstName);
        txtFldLastName = (EditText) findViewById(R.id.txtFldLastName);
        txtFldEmail = (EditText) findViewById(R.id.txtFldEmail);
        txtFldBirthdate = (EditText) findViewById(R.id.txtFldBirthdate);
        txtFldAddress = (TextView) findViewById(R.id.txtFldAddress);

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
        txtFldAddress.setOnClickListener(this);

        if(AddHomeAddress.userHomeAddress != null){
            txtFldUsername.setText(AddHomeAddress.userName);
            txtFldEmail.setText(AddHomeAddress.userEmail);
            txtFldFirstName.setText(AddHomeAddress.userFname);
            txtFldLastName.setText(AddHomeAddress.userLname);
            txtFldPassword.setText(AddHomeAddress.userPassword);
            txtFldAddress.setText(AddHomeAddress.userHomeAddress);
            txtFldBirthdate.setText(AddHomeAddress.userBdate);
        }

    }

    @Override
    public void onClick(View v) {
        if (v == btnCreateAccount) {
            createAccount();
        }

        /*if(v == btnLogin) {
            Intent loginPage = new Intent(SignUp.this, Login.class);
            finish();
            startActivity(loginPage);
        }*/

        if (v == txtFldBirthdate) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(SignUp.this, AlertDialog.THEME_HOLO_LIGHT,
                    new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int birthYear, int birthMonth, int birthDay) {
                    birthMonth = birthMonth + 1;
                    txtFldBirthdate.setText(birthDay + "/" + birthMonth + "/" + birthYear);
                }
            }, year, month, day);
            datePickerDialog.show();
        }

        if (v == txtFldAddress) {
            Intent i = new Intent(getApplicationContext(), AddHomeAddress.class);
            final String userName = txtFldUsername.getText().toString();
            final String userPassword = txtFldPassword.getText().toString();
            final String userEmail = txtFldEmail.getText().toString();
            final String userFname = txtFldFirstName.getText().toString();
            final String userLname = txtFldLastName.getText().toString();
            final String userBdate = txtFldBirthdate.getText().toString();

            i.putExtra("userName", userName);
            i.putExtra("userPassword", userPassword);
            i.putExtra("userEmail", userEmail);
            i.putExtra("userFname", userFname);
            i.putExtra("userLname", userLname);
            i.putExtra("userBdate", userBdate);

            startActivity(i);
            //finish();
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
        String userType = "Regular User";
        String userStatus = "Active";
        String deviceToken = FirebaseInstanceId.getInstance().getToken();

        if(deviceToken.equals("")) {
            deviceToken = "NULL";
        }

        if(TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please enter your username.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(FirebaseDatabaseManager.isUsernameUsed(username)) {
            showDialogUsernameUsed();
            return;
        }

        if(FirebaseDatabaseManager.isEmailUsed(email)) {
            showDialogEmailUsed();
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

        double currentLat = 10.316590;
        double currentLong = 123.897093;
        double homeLat = 10.316590;
        double homeLong = 123.897093;
        final String userPhoto = "https://firebasestorage.googleapis.com/v0/b/maevis-ecd17.appspot.com/o/UserPhotos%2Fdefault_user.png?alt=media&token=338722ca-9d00-4dd8-bd4a-e3c3bffd3cfa";

        final UserModel userModel = new UserModel(address, birthdate, currentLat, currentLong, deviceToken, email, firstName, homeLat, homeLong, lastName, password, userPhoto, userStatus, userType, username);

        progressDialog.setMessage("Registering User.");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            DatabaseReference newUser = FirebaseUsers.child(user.getUid());
                            newUser.setValue(userModel);

                            Toast.makeText(SignUp.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                            float cLat = (float) userModel.getCurrentLat();
                            float cLong= (float) userModel.getCurrentLong();
                            float hLat = (float) userModel.getHomeLat();
                            float hLong = (float) userModel.getHomeLat();

                            SessionManager.createLoginSession(user.getUid(), userModel.getUsername(), userModel.getEmail(), userModel.getFirstName(), userModel.getLastName(), userModel.getBirthdate(), userModel.getAddress(), userModel.getUserStatus(), userModel.getUserType(), userModel.getDeviceToken(), cLat, cLong, hLat, hLong, userPhoto);

                            finish();
                            startActivity(new Intent(getApplicationContext(), Login.class));
                        } else {
                            Toast.makeText(SignUp.this, "Registration Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showDialogUsernameUsed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("USERNAME EXISTS");
        builder.setMessage("Username has already been used. Please try a new one.");
        builder.setInverseBackgroundForced(true);

        builder.setNegativeButton("Return", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        alert.getButton(alert.BUTTON_POSITIVE).setTextColor(Color.BLACK);
    }

    private void showDialogEmailUsed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("EMAIL EXISTS");
        builder.setMessage("Email has already been used. Please try a new one.");
        builder.setInverseBackgroundForced(true);

        builder.setNegativeButton("Return", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        alert.getButton(alert.BUTTON_POSITIVE).setTextColor(Color.BLACK);
    }
}
