package com.mad.dms.salesRep;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mad.dms.R;

public class SalesRepProfileSettings extends AppCompatActivity implements View.OnClickListener {
    private EditText pro_name;
    private EditText pro_email;
    private EditText pro_phone;
    private EditText pro_password;
    private ProgressBar progressBar;
    private Button Update;
    private Button Edit;
    private String Fullname;
    private String Email;
    private String PhoneNo;
    private String Password;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_profile_settings);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        pro_name = findViewById(R.id.RPS_name);
        pro_email = findViewById(R.id.RPS_email);
        pro_phone = findViewById(R.id.RPS_phoneNo);
        pro_password = findViewById(R.id.RPS_password);
        progressBar = findViewById(R.id.RPS_progressBar);


        Edit = findViewById(R.id.RPS_Edit);
        Update = findViewById(R.id.RPS_Update);
        Edit.setOnClickListener(this);
        Update.setOnClickListener(this);

        pro_name.setEnabled(false);
        pro_email.setEnabled(false);
        pro_phone.setEnabled(false);
        pro_password.setEnabled(false);


        databaseReference = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User srp = dataSnapshot.getValue(User.class);

                if (!srp.getEmail().equals(null) && !srp.getFullname().equals(null) && !srp.getPhoneNo().equals(null) && !srp.getPassword().equals(null)) {
                    pro_name.setText(srp.getFullname());
                    pro_email.setText(srp.getEmail());
                    pro_phone.setText(srp.getPhoneNo());
                    pro_password.setText(srp.getPassword());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast msg = Toast.makeText(SalesRepProfileSettings.this, databaseError.getCode(), Toast.LENGTH_SHORT);
                msg.show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RPS_Edit:
                pro_name.setEnabled(true);
                pro_email.setEnabled(true);
                pro_phone.setEnabled(true);
                pro_password.setEnabled(true);
                Update.setVisibility(View.VISIBLE);
                break;
            case R.id.RPS_Update:
                UpdateData();
                break;
        }
    }

    private void UpdateData() {
        progressBar.setVisibility(View.VISIBLE);

        Fullname = pro_name.getText().toString();
        Email = pro_email.getText().toString();
        PhoneNo = pro_phone.getText().toString();
        Password = pro_password.getText().toString();

        User sr = new User(Fullname, Email, PhoneNo, Password);
        databaseReference.setValue(sr).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.INVISIBLE);

                if (task.isSuccessful()) {
                    firebaseUser.updateEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()) {
                                Toast msg = Toast.makeText(SalesRepProfileSettings.this, "Profile Update Failure", Toast.LENGTH_SHORT);
                                msg.show();
                            }
                        }
                    });
                    firebaseUser.updatePassword(Password).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()) {
                                Toast msg = Toast.makeText(SalesRepProfileSettings.this, "Profile Update Failure", Toast.LENGTH_SHORT);
                                msg.show();
                            }
                        }
                    });;

                    pro_name.setEnabled(false);
                    pro_email.setEnabled(false);
                    pro_phone.setEnabled(false);
                    pro_password.setEnabled(false);
                    Update.setVisibility(View.INVISIBLE);


                    Toast msg = Toast.makeText(SalesRepProfileSettings.this, "Profile SuccessFully Updated", Toast.LENGTH_SHORT);
                    msg.show();


                } else {
                    Toast msg = Toast.makeText(SalesRepProfileSettings.this, "Profile Update Failure", Toast.LENGTH_SHORT);
                    msg.show();
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }
}
