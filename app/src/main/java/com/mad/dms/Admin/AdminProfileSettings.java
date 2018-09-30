package com.mad.dms.Admin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mad.dms.Classes.User;
import com.mad.dms.DataBase.UserDBHelper;
import com.mad.dms.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminProfileSettings extends AppCompatActivity implements View.OnClickListener {
    private EditText pro_email, pro_password;
    private Button Update, Edit;
    private String Email, Password;
    private UserDBHelper udb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_profile_settings);

        udb = new UserDBHelper(this);

        pro_email = findViewById(R.id.APS_email);
        pro_password = findViewById(R.id.APS_password);

        Edit = findViewById(R.id.APS_Edit);
        Update = findViewById(R.id.APS_Update);
        Edit.setOnClickListener(this);
        Update.setOnClickListener(this);

        pro_email.setEnabled(false);
        pro_password.setEnabled(false);

        User cursor = udb.getAdminData();
        pro_email.setText(cursor.getEmail());
        pro_password.setText(cursor.getPassword());


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

        Email = pro_email.getText().toString();
        Password = pro_password.getText().toString();

        //email pattern
        String r1 = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
        Pattern p1 = Pattern.compile(r1);
        Matcher m1 = p1.matcher(Email);

        //password pattern
        String r2 = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{7,15}$";
        Pattern p2 = Pattern.compile(r2);
        Matcher m2 = p2.matcher(Password);

        // Check for both field is empty or not
        if (Email.isEmpty() || Password.isEmpty()) {
            Toast msg = Toast.makeText(AdminProfileSettings.this, "Empty Input Field", Toast.LENGTH_SHORT);
            msg.show();
        } else if (!m1.find()) {
            Toast msg = Toast.makeText(AdminProfileSettings.this, "Email Field is Invalid.", Toast.LENGTH_SHORT);
            msg.show();
        } else if (!m2.find()) {
            Toast msg = Toast.makeText(AdminProfileSettings.this, "Password required at least 1 UpperCase,1 Lowercase,1 Digit and 1 Symbol", Toast.LENGTH_LONG);
            msg.show();
        } else if (Password.length() < 8) {
            Toast msg = Toast.makeText(AdminProfileSettings.this, "Minimum Password Length is 8", Toast.LENGTH_SHORT);
            msg.show();
        } else {

            boolean result = udb.UpdateAdmin("1", Email, Password);

            if (result == true) {
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }
}
