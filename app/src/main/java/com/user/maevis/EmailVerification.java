package com.user.maevis;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.user.maevis.session.SessionManager;

public class EmailVerification extends AppCompatActivity implements View.OnClickListener{

    private TextView timeLeft;
    private Button resendEmail;
    private Button btnProceed;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        timeLeft = findViewById(R.id.timeLeft);
        resendEmail = findViewById(R.id.resendEmail);
        btnProceed = (Button) findViewById(R.id.proceedButton);

        progressDialog = new ProgressDialog(this, R.style.AlertDialogStyle); //instantiate a progress diaglog

        resendEmail.setOnClickListener(this);

        Timer();
    }

    private void Timer(){

        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                timeLeft.setText(l / 1000+"");
            }

            @Override
            public void onFinish() {
                timeLeft.setText(0+"");
                resendEmail.setEnabled(true);
            }
        }.start();
    }

    @Override
    public void onClick(View view) {
        if (view == resendEmail){
            resend();

             return;
        }

        if(view == btnProceed) {
            FirebaseUser user = SessionManager.getFirebaseAuth().getCurrentUser();

            if(user.isEmailVerified()) {
                Toast.makeText(this, "Registration complete!", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(this, "Email is not verified. Please verify your email to complete your registration.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void resend(){
        final FirebaseUser user = SessionManager.getFirebaseAuth().getCurrentUser();

        progressDialog.setMessage("Sending email verification.");
        progressDialog.show();

        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(EmailVerification.this,
                            "Verification email sent to " + user.getEmail(),
                            Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(EmailVerification.this,
                            "Failed to send verification email.",
                            Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }
        });
    }

    private void showEmailNotVerifiedDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Email not verified");
        builder.setMessage("Your email is not yet verified. Please check your email and click the verification link to complete your registration.");
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
