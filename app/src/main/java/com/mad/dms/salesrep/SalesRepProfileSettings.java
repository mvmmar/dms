package com.mad.dms.salesrep;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mad.dms.admindb.UserDBHelper;
import com.mad.dms.R;
import com.mad.dms.signin.Login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SalesRepProfileSettings extends AppCompatActivity implements View.OnClickListener {
    private EditText pro_name, pro_email, pro_phone,pro_password;
    private Button Update, Edit;
    private String Name, Email, PhoneNo, Password;
    private UserDBHelper udb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sr_profile_settings);

        udb = new UserDBHelper(this);

        pro_name = findViewById(R.id.RPS_name);
        pro_email = findViewById(R.id.RPS_email);
        pro_phone = findViewById(R.id.RPS_phoneNo);
        pro_password = findViewById(R.id.RPS_password);

        Edit = findViewById(R.id.RPS_Edit);
        Update = findViewById(R.id.RPS_Update);
        Edit.setOnClickListener(this);
        Update.setOnClickListener(this);

        pro_name.setEnabled(false);
        pro_email.setEnabled(false);
        pro_phone.setEnabled(false);
        pro_password.setEnabled(false);

        Cursor cursor = udb.getSalesRepData(Login.sessionEmail);
        if (cursor.moveToNext()) {
            pro_name.setText(cursor.getString(1));
            pro_email.setText(cursor.getString(2));
            pro_password.setText(cursor.getString(3));
            pro_phone.setText(cursor.getString(4));

        }
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

        Name = pro_name.getText().toString();
        Email = pro_email.getText().toString();
        PhoneNo = pro_phone.getText().toString();
        Password = pro_password.getText().toString();

        //name pattern
        String r1 = "^[a-zA-Z]{2,30}$";
        Pattern p1 = Pattern.compile(r1);
        Matcher m1 = p1.matcher(Name);

        //email pattern
        String r2 = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
        Pattern p2 = Pattern.compile(r2);
        Matcher m2 = p2.matcher(Email);

        //phoneNo pattern
        String r3 = "^[0-9]{10}+$";
        Pattern p3 = Pattern.compile(r3);
        Matcher m3 = p3.matcher(PhoneNo);

        //password pattern
        String r4 = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{7,15}$";
        Pattern p4 = Pattern.compile(r4);
        Matcher m4 = p4.matcher(Password);

        if (Name.isEmpty() || Email.isEmpty() || PhoneNo.isEmpty() || Password.isEmpty()) {
            Toast msg = Toast.makeText(SalesRepProfileSettings.this, "Empty Input Field", Toast.LENGTH_SHORT);
            msg.show();
        } else if (!m1.find()) {
            Toast msg = Toast.makeText(SalesRepProfileSettings.this, "Fullname Field is Invalid.", Toast.LENGTH_SHORT);
            msg.show();
        } else if (!m2.find()) {
            Toast msg = Toast.makeText(SalesRepProfileSettings.this, "Email Field is Invalid.", Toast.LENGTH_SHORT);
            msg.show();
        } else if (!m3.find()) {
            Toast msg = Toast.makeText(SalesRepProfileSettings.this, "Phone Number Field is Invalid.", Toast.LENGTH_SHORT);
            msg.show();
        } else if (!m4.find()) {
            Toast msg = Toast.makeText(SalesRepProfileSettings.this, "Password required at least 1 UpperCase,1 Lowercase,1 Digit and 1 Symbol", Toast.LENGTH_LONG);
            msg.show();
        } else if (Password.length() < 8) {
            Toast msg = Toast.makeText(SalesRepProfileSettings.this, "Minimum Password Length is 8", Toast.LENGTH_SHORT);
            msg.show();
        } else {
            boolean result = udb.UpdateSalesRep(Login.sessionEmail,Name, Email,PhoneNo,Password);

            if (result == true) {
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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }
}
