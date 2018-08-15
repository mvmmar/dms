package com.mad.dms.admin;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mad.dms.R;
import com.mad.dms.salesRep.User;

public class AdminProfileSettings extends AppCompatActivity implements View.OnClickListener {
    private EditText pro_email;
    private EditText pro_password;
    private ProgressBar progressBar;
    private Button Update;
    private Button Edit;
    private String Email;
    private String Password;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_profile_settings);

        firebaseDatabase = FirebaseDatabase.getInstance();

        pro_email = findViewById(R.id.APS_email);
        pro_password = findViewById(R.id.APS_password);
        progressBar = findViewById(R.id.APS_progressBar);


        Edit = findViewById(R.id.APS_Edit);
        Update = findViewById(R.id.APS_Update);
        Edit.setOnClickListener(this);
        Update.setOnClickListener(this);

        pro_email.setEnabled(false);
        pro_password.setEnabled(false);


        databaseReference = firebaseDatabase.getReference("Admin");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User srp = dataSnapshot.getValue(User.class);

                if (!srp.getEmail().equals(null) && !srp.getPassword().equals(null)) {
                    pro_email.setText(srp.getEmail());
                    pro_password.setText(srp.getPassword());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast msg = Toast.makeText(AdminProfileSettings.this, databaseError.getCode(), Toast.LENGTH_SHORT);
                msg.show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.APS_Edit:
                pro_email.setEnabled(true);
                pro_password.setEnabled(true);
                Update.setVisibility(View.VISIBLE);
                break;
            case R.id.APS_Update:
                UpdateData();
                break;
        }
    }

    private void UpdateData() {
        progressBar.setVisibility(View.VISIBLE);

        Email = pro_email.getText().toString();
        Password = pro_password.getText().toString();

        User sr = new User(Email, Password);
        databaseReference.setValue(sr).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.INVISIBLE);

                if (task.isSuccessful()) {
                    pro_email.setEnabled(false);
                    pro_password.setEnabled(false);
                    Update.setVisibility(View.INVISIBLE);

                    Toast msg = Toast.makeText(AdminProfileSettings.this, "Profile SuccessFully Updated", Toast.LENGTH_SHORT);
                    msg.show();


                } else {
                    Toast msg = Toast.makeText(AdminProfileSettings.this, "Profile Update Failure", Toast.LENGTH_SHORT);
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
