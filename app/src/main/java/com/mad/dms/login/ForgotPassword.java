package com.mad.dms.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.mad.dms.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    private TextView submit;
    private TextView back;
    private EditText email_field;
    private ProgressBar progressBar;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        progressBar = findViewById(R.id.FP_progressBar);

        email_field = findViewById(R.id.FP_email);
        submit = findViewById(R.id.FP_submit);
        back = findViewById(R.id.FP_back);
        firebaseAuth = FirebaseAuth.getInstance();

        submit.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.FP_submit:
                resetPassword();
                break;
            case R.id.FP_back:
                onBackPressed();
                break;
        }
    }

    private void resetPassword() {
        // Get email id and password
        String getEmailId = email_field.getText().toString();
        String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

        // Check patter for email id
        Pattern p = Pattern.compile(regEx);
        Matcher m1 = p.matcher(getEmailId);

        // Check for both field is empty or not
        if (getEmailId.isEmpty()) {
            Toast msg = Toast.makeText(ForgotPassword.this, "Email Field is Empty.\nEnter your Registered Email ID. ", Toast.LENGTH_LONG);
            msg.show();
        } else if (!m1.find()) {
            Toast msg = Toast.makeText(ForgotPassword.this, "Your Email Id is Invalid.", Toast.LENGTH_SHORT);
            msg.show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.sendPasswordResetEmail(getEmailId)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.INVISIBLE);

                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPassword.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ForgotPassword.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
