package com.mad.dms.salesRep;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mad.dms.admin.AdminHome;
import com.mad.dms.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddSalesRep extends AppCompatActivity implements View.OnClickListener {
    private Button srAddbtn;
    private EditText fullname_field;
    private EditText email_field;
    private EditText phoneNo_field;
    private EditText password_field;
    private EditText con_password_field;
    private ProgressBar progressBar;
    private String getFulname;
    private String getEmailId;
    private String getPhoneNo;
    private String getPassword;
    private String getConPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_sales_rep);
        this.setTitle("Add Sales Rep");

        firebaseAuth = FirebaseAuth.getInstance();
        fullname_field = findViewById(R.id.SU_Fullname);
        email_field = findViewById(R.id.SU_Email);
        phoneNo_field = findViewById(R.id.SU_Phoneno);
        password_field = findViewById(R.id.SU_Password);
        con_password_field = findViewById(R.id.SU_ConPassword);
        progressBar = findViewById(R.id.SR_progressBar);
        srAddbtn = findViewById(R.id.addSRBtn);

        srAddbtn.setOnClickListener(this);

    }

    // Check Validation before login
    private void checkValidation() {
        getFulname = fullname_field.getText().toString();
        getEmailId = email_field.getText().toString();
        getPhoneNo = phoneNo_field.getText().toString();
        getPassword = password_field.getText().toString();
        getConPassword = con_password_field.getText().toString();

        //name pattern
        String r1 = "^[a-zA-z]+$";
        Pattern p1 = Pattern.compile(r1);
        Matcher m1 = p1.matcher(getFulname);

        //email pattern
        String r2 = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
        Pattern p2 = Pattern.compile(r2);
        Matcher m2 = p2.matcher(getEmailId);

        //phoneNo pattern
        String r3 = "^[0-9]{10}+$";
        Pattern p3 = Pattern.compile(r3);
        Matcher m3 = p3.matcher(getPhoneNo);

        //password pattern
        String r4 = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{7,15}$";
        Pattern p4 = Pattern.compile(r4);
        Matcher m4 = p4.matcher(getPassword);

        // Check for both field is empty or not
        if (getFulname.isEmpty() || getEmailId.isEmpty() || getPhoneNo.isEmpty() || getPassword.isEmpty() || getConPassword.isEmpty()) {
            Toast msg = Toast.makeText(AddSalesRep.this, "Empty Input Field", Toast.LENGTH_SHORT);
            msg.show();
        } else if (!m1.find()) {
            Toast msg = Toast.makeText(AddSalesRep.this, "Fullname Field is Invalid.", Toast.LENGTH_SHORT);
            msg.show();
        } else if (!m2.find()) {
            Toast msg = Toast.makeText(AddSalesRep.this, "Email Field is Invalid.", Toast.LENGTH_SHORT);
            msg.show();
        } else if (!m3.find()) {
            Toast msg = Toast.makeText(AddSalesRep.this, "Phone Number Field is Invalid.", Toast.LENGTH_SHORT);
            msg.show();
        } else if (!m4.find()) {
            Toast msg = Toast.makeText(AddSalesRep.this, "Password required at least 1 UpperCase,1 Lowercase,1 Digit and 1 Symbol", Toast.LENGTH_LONG);
            msg.show();
        } else if (getPassword.length() < 7) {
            Toast msg = Toast.makeText(AddSalesRep.this, "Minimum Password Length is 7", Toast.LENGTH_SHORT);
            msg.show();
        } else if (!getConPassword.equals(getPassword)) {
            Toast msg = Toast.makeText(AddSalesRep.this, "Password Does Not Match", Toast.LENGTH_SHORT);
            msg.show();
        } else {
            progressBar.setVisibility(View.VISIBLE);

            firebaseAuth.createUserWithEmailAndPassword(getEmailId, getPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.INVISIBLE);

                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        sendUserData();
                        firebaseAuth.signOut();

                        Toast msg = Toast.makeText(AddSalesRep.this, "Registration SuccessFull.", Toast.LENGTH_SHORT);
                        msg.show();

                        Intent i1 = new Intent(AddSalesRep.this, AdminHome.class);
                        startActivity(i1);


                    } else {
                        // If sign in fails, display a message to the user.
                        Toast msg = Toast.makeText(AddSalesRep.this, "Registration Failure", Toast.LENGTH_SHORT);
                        msg.show();
                    }
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addSRBtn:
                checkValidation();
                break;
        }
    }

    private void sendUserData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid());
        User sr = new User(getFulname, getEmailId, getPhoneNo, getPassword);
        myRef.setValue(sr);
    }


}
