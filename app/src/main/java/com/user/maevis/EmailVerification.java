package com.user.maevis;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import com.user.maevis.models.FirebaseDatabaseManager;
import com.user.maevis.session.SessionManager;

public class EmailVerification extends AppCompatActivity implements View.OnClickListener{

    private TextView timeLeft;
    private Button resendEmail;
    private Button btnProceed;
    private Button logoutButton;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;

    private ProgressBar progressBar;
    private MyCountDownTimer myCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        timeLeft = findViewById(R.id.timeLeft);
        resendEmail = findViewById(R.id.resendEmail);
        btnProceed = (Button) findViewById(R.id.proceedButton);
        logoutButton = findViewById(R.id.logoutButton);
        progressBar = findViewById(R.id.progressBar);

        progressDialog = new ProgressDialog(this, R.style.AlertDialogStyle); //instantiate a progress diaglog

        resendEmail.setOnClickListener(this);
        btnProceed.setOnClickListener(this);
        logoutButton.setOnClickListener(this);

        resend();
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            timeLeft.setText(String.valueOf(millisUntilFinished / 1000 + "s"));
            int progress = (int) (millisUntilFinished/600);
            progressBar.setProgress(progress);

            final FirebaseUser user = SessionManager.getFirebaseAuth().getCurrentUser();

            user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(user.isEmailVerified()) {
                        myCountDownTimer.cancel();
                        myCountDownTimer = null;

                        btnProceed.setEnabled(true);
                    }
                }
            });
        }

        @Override
        public void onFinish() {
            timeLeft.setText("0s");
            progressBar.setProgress(0);
            resendEmail.setEnabled(true);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_email_verification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            SessionManager.clearSession();
            SessionManager.getFirebaseAuth().signOut();

            if(Login.getmGoogleSignInClient() != null) {
                Login.getmGoogleSignInClient().signOut()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...
                            }
                        });
                Toast.makeText(EmailVerification.this, "[Google Connected] Logged out.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(EmailVerification.this, "[Google not connected] Logged out.", Toast.LENGTH_LONG).show();
                //Log.v("GOOGLE", "GOOGLE API CLIENT NOT CONNECTED.");
            }

            finish();
            startActivity(new Intent(getApplicationContext(), Login.class));

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view == resendEmail){
            resend();

             return;
        }

        if(view == btnProceed) {
            final FirebaseUser user = SessionManager.getFirebaseAuth().getCurrentUser();

            progressDialog.setMessage("Processing.");
            progressDialog.show();

            user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressDialog.dismiss();

                    if(user.isEmailVerified()) {
                        Toast.makeText(EmailVerification.this, "Registration complete!", Toast.LENGTH_LONG).show();
                        //SignUp.addUserToFirebaseDatabase(FirebaseDatabaseManager.getNewUserModelTemp());
                        finish();
                        startActivity(new Intent(EmailVerification.this, Sidebar_HomePage.class));
                    } else {
                        Toast.makeText(EmailVerification.this, "Email: "+user.getEmail(), Toast.LENGTH_LONG).show();
                        showEmailNotVerifiedDialog();
                    }
                }
            });

            return;
        }

        if(view == logoutButton){
            myCountDownTimer.cancel();
            myCountDownTimer = null;
            SessionManager.clearSession();
            SessionManager.getFirebaseAuth().signOut();

            if(Login.getmGoogleSignInClient() != null) {
                Login.getmGoogleSignInClient().signOut()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...
                            }
                        });
                Toast.makeText(EmailVerification.this, "[Google Connected] Logged out.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(EmailVerification.this, "[Google not connected] Logged out.", Toast.LENGTH_LONG).show();
                //Log.v("GOOGLE", "GOOGLE API CLIENT NOT CONNECTED.");
            }

            finish();
            startActivity(new Intent(EmailVerification.this, LoadingScreeen.class));

            return;
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

        progressBar.setProgress(100);
        myCountDownTimer = new MyCountDownTimer(60000, 1000);
        myCountDownTimer.start();
        resendEmail.setEnabled(false);
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
