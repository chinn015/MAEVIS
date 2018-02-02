package com.user.maevis;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPass extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "ForgotPass";
    private static final String REQUIRED = "Required";
    private FirebaseAuth auth;
    private EditText emailAddress;
    private Button sendRequest;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        auth = FirebaseAuth.getInstance();
        emailAddress = findViewById(R.id.emailAdd);
        sendRequest = findViewById(R.id.sendRequest);
        progressDialog = new ProgressDialog(this, R.style.AlertDialogStyle); //instantiate a progress diaglog

        sendRequest.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == sendRequest){
            resetPass();
        }
    }

    private void showAccountNotFoundDialog(final String username) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Reset Password Link Sent");
        builder.setMessage("Please check the inbox of " + username + " and click on the link to reset your password.");
        builder.setInverseBackgroundForced(true);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        android.app.AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void resetPass(){

        final String emailAdd = emailAddress.getText().toString().trim();

        if (TextUtils.isEmpty(emailAdd)){
            emailAddress.setError(REQUIRED);
            return;
        }

        if(isEmailValid(emailAdd) != true){
            emailAddress.setError("Please enter a valid email address.");
            return;
        }

        progressDialog.setMessage("Sending reset password email.");
        progressDialog.show();

        auth.sendPasswordResetEmail(emailAdd)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            showAccountNotFoundDialog(emailAdd);
                        }
                    }
                });
    }
}
