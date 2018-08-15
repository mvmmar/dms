package com.mad.dms.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mad.dms.R;
import com.mad.dms.admin.AdminHome;
import com.mad.dms.salesRep.SalesRepHome;
import com.mad.dms.salesRep.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private Button loginbtn;
    private EditText email_field;
    private EditText password_field;
    private TextView forgotPassword;
    private CheckBox show_hide_password;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String admin_Email;
    private String admin_Password;
    private String getEmailId;
    private String getPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        databaseReference = firebaseDatabase.getReference("Admin");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User admin = dataSnapshot.getValue(User.class);
                if (!admin.getEmail().equals(null) && !admin.getPassword().equals(null)) {

                    admin_Email = admin.getEmail();
                    admin_Password = admin.getPassword();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast msg = Toast.makeText(Login.this, "Couldn't Connected to Server.", Toast.LENGTH_SHORT);
                msg.show();
            }
        });

        progressBar = findViewById(R.id.SI_progressBar);
        email_field = findViewById(R.id.login_email);
        password_field = findViewById(R.id.login_password);
        loginbtn = findViewById(R.id.loginBtn);
        forgotPassword = findViewById(R.id.forgot_password);
        show_hide_password = findViewById(R.id.show_hide_password);
        setListeners();


    }

    // Set Listeners
    private void setListeners() {
        loginbtn.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);

        // Set check listener over checkbox for showing and hiding password
        show_hide_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                // If it is checkec then show password else hide
                // password
                if (isChecked) {
                    show_hide_password.setText(R.string.hide_pwd);
                    password_field.setInputType(InputType.TYPE_CLASS_TEXT);
                    password_field.setTransformationMethod(HideReturnsTransformationMethod.getInstance());// show password
                } else {
                    show_hide_password.setText(R.string.show_pwd);
                    password_field.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password_field.setTransformationMethod(PasswordTransformationMethod.getInstance());// hide password
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                checkValidation();
//                Intent i = new Intent(Login.this, AdminHome.class);
//                startActivity(i);

                this.overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
                break;
            case R.id.forgot_password:
                Intent i1 = new Intent(Login.this, ForgotPassword.class);
                startActivity(i1);
                this.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    // Check Validation before login
    private void checkValidation() {
        // Get email id and password
        getEmailId = email_field.getText().toString();
        getPassword = password_field.getText().toString();

        // Check patter for email id
        String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
        Pattern p = Pattern.compile(regEx);
        Matcher m1 = p.matcher(getEmailId);


        if (getEmailId.isEmpty() || getPassword.isEmpty()) {
            Toast msg = Toast.makeText(Login.this, "Enter Both credentials.", Toast.LENGTH_SHORT);
            msg.show();
        } else if (!m1.find()) {
            Toast msg = Toast.makeText(Login.this, "Your Email Id is Invalid.", Toast.LENGTH_SHORT);
            msg.show();
        } else if (getPassword.length() < 8) {
            Toast msg = Toast.makeText(Login.this, "Minimum Password Length is 8", Toast.LENGTH_SHORT);
            msg.show();
        } else {
            progressBar.setVisibility(View.VISIBLE);

            if (getEmailId.equals(admin_Email) && getPassword.equals(admin_Password)) {
                Intent i = new Intent(Login.this, AdminHome.class);
                startActivity(i);

                Toast msg = Toast.makeText(Login.this, "Welcome Admin.", Toast.LENGTH_SHORT);
                msg.show();

                finish();
            }

            if (!admin_Email.equalsIgnoreCase(getEmailId)) {
                firebaseAuth.signInWithEmailAndPassword(getEmailId, getPassword)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.INVISIBLE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast msg = Toast.makeText(Login.this, "Login Successfull", Toast.LENGTH_SHORT);
                                    msg.show();

                                    Intent i = new Intent(Login.this, SalesRepHome.class);
                                    startActivity(i);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast msg = Toast.makeText(Login.this, "Invalid Email or Password", Toast.LENGTH_SHORT);
                                    msg.show();
                                }

                            }
                        });
            }
        }
    }
}
