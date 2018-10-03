package com.mad.dms.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mad.dms.admindb.UserDBHelper;
import com.mad.dms.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddSalesRep extends AppCompatActivity {
    private Button srAddbtn;
    private EditText name_field, email_field, phoneNo_field, password_field, con_password_field;
    private UserDBHelper udb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_sales_rep);

        udb = new UserDBHelper(this);

        name_field = findViewById(R.id.SU_Name);
        email_field = findViewById(R.id.SU_Email);
        phoneNo_field = findViewById(R.id.SU_Phoneno);
        password_field = findViewById(R.id.SU_Password);
        con_password_field = findViewById(R.id.SU_ConPassword);

        srAddbtn = findViewById(R.id.addSRBtn);
        addSalesRep();
    }


    private void addSalesRep() {
        srAddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getName = name_field.getText().toString();
                String getEmailId = email_field.getText().toString();
                String getPhoneNo = phoneNo_field.getText().toString();
                String getPassword = password_field.getText().toString();
                String getConPassword = con_password_field.getText().toString();

                boolean isEmailAvailable = udb.checkUserEmail(getEmailId);
                //name pattern
                String r1 = "^[a-zA-Z]{2,30}$";
                Pattern p1 = Pattern.compile(r1);
                Matcher m1 = p1.matcher(getName);

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
                if (getName.isEmpty() || getEmailId.isEmpty() || getPhoneNo.isEmpty() || getPassword.isEmpty() || getConPassword.isEmpty()) {
                    Toast msg = Toast.makeText(AddSalesRep.this, "Empty Input Field", Toast.LENGTH_SHORT);
                    msg.show();
                } else if (!m1.find()) {
                    Toast msg = Toast.makeText(AddSalesRep.this, "Name Field is Invalid.", Toast.LENGTH_SHORT);
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
                } else if (getPassword.length() < 8) {
                    Toast msg = Toast.makeText(AddSalesRep.this, "Minimum Password Length is 8", Toast.LENGTH_SHORT);
                    msg.show();
                } else if (!getConPassword.equals(getPassword)) {
                    Toast msg = Toast.makeText(AddSalesRep.this, "Password Does Not Match", Toast.LENGTH_SHORT);
                    msg.show();
                } else if (isEmailAvailable == false) {
                    Toast msg = Toast.makeText(AddSalesRep.this, "Email is Used Already !!!", Toast.LENGTH_SHORT);
                    msg.show();
                } else {

                    boolean result = udb.addInfo(getName, getEmailId, getPhoneNo, getPassword);

                    if (result == true) {
                        Toast msg = Toast.makeText(AddSalesRep.this, "Sales-Rep Added SuccessFully.", Toast.LENGTH_SHORT);
                        msg.show();
                    } else {
                        Toast msg = Toast.makeText(AddSalesRep.this, "Sales-Rep Registration Failure", Toast.LENGTH_SHORT);
                        msg.show();
                    }

                    Intent i1 = new Intent(AddSalesRep.this, ViewSalesRep.class);
                    startActivity(i1);

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
    }


}
