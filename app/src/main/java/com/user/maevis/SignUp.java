package com.user.maevis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.ImageView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class SignUp extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {


    DatabaseReference FirebaseUsers;
    private EditText txtFldUsername;
    private EditText txtFldPassword;
    private EditText txtFldConPassword;
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

    private static UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up);

        ImageView maevis_logo = (ImageView)findViewById(R.id.imgLogo);
        maevis_logo.setImageResource(R.drawable.maevis_logo);

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));



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
        txtFldConPassword = (EditText) findViewById(R.id.txtFldSignUpConPassword);

        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        //btnLogin = (Button) findViewById(R.id.btnLogin);

        txtFldBirthdate.setOnClickListener(this);
        btnCreateAccount.setOnClickListener(this);
        btnCreateAccount.setOnTouchListener(this);
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
            txtFldConPassword.setText(AddHomeAddress.userConPassword);


        }

    }

    @Override
    public void onClick(View v) {
        if (v == btnCreateAccount) {
            createAccount();
        }

        if (v == txtFldBirthdate) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(SignUp.this, AlertDialog.THEME_HOLO_LIGHT,
                    new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int birthYear, int birthMonth, int birthDay) {
                    birthMonth = birthMonth + 1;
                    txtFldBirthdate.setText(birthDay + "/" + birthMonth + "/" + birthYear);
                }
            }, year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
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
            final String userConPassword = txtFldConPassword.getText().toString();

            i.putExtra("userName", userName);
            i.putExtra("userPassword", userPassword);
            i.putExtra("userConPassword", userConPassword);
            i.putExtra("userEmail", userEmail);
            i.putExtra("userFname", userFname);
            i.putExtra("userLname", userLname);
            i.putExtra("userBdate", userBdate);

            startActivity(i);
            finish();
        }
    }

    private void createAccount() {
        String email = txtFldEmail.getText().toString().trim();
        String username = txtFldUsername.getText().toString().trim();
        String password = txtFldPassword.getText().toString().trim();
        String conPassword = txtFldConPassword.getText().toString().trim();
        String firstName = txtFldFirstName.getText().toString().trim();
        String lastName = txtFldLastName.getText().toString().trim();
        String birthdate = txtFldBirthdate.getText().toString().trim();
        String address = txtFldAddress.getText().toString().trim();
        String userType = "Regular User";
        String userStatus = "Active";
        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        double homeLat = AddHomeAddress.userHomeLat;
        double homeLong = AddHomeAddress.userHomeLong;
        double currentLat = 10.316590;
        double currentLong = 123.897093;

        if(deviceToken.equals("")) {
            deviceToken = "NULL";
        }

        if(TextUtils.isEmpty(username)) {
            txtFldUsername.setError("Please input your username.");
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
            txtFldPassword.setError("Please input your password.");
            return;
        }

        if(TextUtils.isEmpty(email)) {
            txtFldEmail.setError("Please input your email.");
            return;
        }

        if(TextUtils.isEmpty(firstName)) {
            txtFldFirstName.setError("Please input your first name.");
            return;
        }

        if(TextUtils.isEmpty(lastName)) {
            txtFldLastName.setError("Please input your last name.");
            return;
        }

        if(TextUtils.isEmpty(birthdate)) {
            txtFldBirthdate.setError("Please input your birthdate.");
            return;
        }

        if(TextUtils.isEmpty(address)) {
            txtFldAddress.setError("Please locate your home address.");
            return;
        }

        if(username.length() < 8){
            txtFldUsername.setError("Username must have at least 8 characters");
            return;
        }

        if(isEmailValid(email) != true){
            txtFldEmail.setError("Please enter a valid email address.");
            return;
        }

        String regex = "(.)*(\\d)(.)*";
        Pattern pattern = Pattern.compile(regex);
        boolean containsNumber = pattern.matcher(password).matches();

        if(password.length() < 8 && containsNumber != true){
            txtFldPassword.setError("Passsword must have at least 8 characters with at least 1 digit");
            return;
        }

        if(!password.equals(conPassword)){
            txtFldConPassword.setError("Please confirm your password.");
            return;
        }


        final String userPhoto = "https://firebasestorage.googleapis.com/v0/b/maevis-ecd17.appspot.com/o/UserPhotos%2Fdefault_user.png?alt=media&token=338722ca-9d00-4dd8-bd4a-e3c3bffd3cfa";

        final UserModel userModel = new UserModel(address, birthdate, currentLat, currentLong, deviceToken, email, firstName, homeLat, homeLong, lastName, userPhoto, userStatus, userType, username);
        //FirebaseDatabaseManager.setNewUserModelTemp(userModel);

        progressDialog.setMessage("Registering User.");
        progressDialog.show();

        SessionManager.getFirebaseAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "Account registered.", Toast.LENGTH_SHORT).show();
                            addUserToFirebaseDatabase(userModel);

                            finish();
                            startActivity(new Intent(SignUp.this, EmailVerification.class));
                        } else {
                            Log.e("REG FAIL", "");
                            Toast.makeText(SignUp.this, "Registration Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //finish();
    }

    public static void addUserToFirebaseDatabase(UserModel userModel) {
        DatabaseReference newUser = FirebaseDatabaseManager.FirebaseUsers.child(SessionManager.getFirebaseAuth().getCurrentUser().getUid());
        newUser.setValue(userModel);

        float cLat = (float) userModel.getCurrentLat();
        float cLong= (float) userModel.getCurrentLong();
        float hLat = (float) userModel.getHomeLat();
        float hLong = (float) userModel.getHomeLong();

        SessionManager.createLoginSession(SessionManager.getFirebaseAuth().getCurrentUser().getUid(), userModel.getUsername(), userModel.getEmail(), userModel.getFirstName(), userModel.getLastName(), userModel.getBirthdate(), userModel.getAddress(), userModel.getUserStatus(), userModel.getUserType(), userModel.getDeviceToken(), cLat, cLong, hLat, hLong, userModel.getUserPhoto());
    }

    public static UserModel getUserModel() {
        return userModel;
    }

    public static void setUserModel(UserModel userModel) {
        SignUp.userModel = userModel;
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
            btnCreateAccount.setBackgroundResource(R.drawable.btn_rounded);
            btnCreateAccount.setTextColor(Color.WHITE);
        } else if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            btnCreateAccount.setBackgroundResource(R.drawable.btn_rounded1);
            btnCreateAccount.setTextColor(Color.BLACK);
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            //txtFldAddress.setText("");
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
