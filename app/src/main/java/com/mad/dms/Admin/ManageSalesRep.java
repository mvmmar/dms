package com.mad.dms.Admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mad.dms.DataBase.UserDBHelper;
import com.mad.dms.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManageSalesRep extends AppCompatActivity implements View.OnClickListener {
    private EditText manage_name, manage_email, manage_phone, manage_password;
    private Button Update, Delete;
    private String Name, Email, PhoneNo, Password;
    private static int position;
    private UserDBHelper udb;


    public static void setPosition(int pos) {
        position = pos;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_sales_rep);

        udb = new UserDBHelper(this);

        manage_name = findViewById(R.id.MSR_name);
        manage_email = findViewById(R.id.MSR_email);
        manage_phone = findViewById(R.id.MSR_phoneNo);
        manage_password = findViewById(R.id.MSR_password);

        Delete = findViewById(R.id.MSR_Delete);
        Update = findViewById(R.id.MSR_Update);
        Delete.setOnClickListener(this);
        Update.setOnClickListener(this);

        Intent ret = getIntent();
        Email = ret.getStringExtra("passEmail");
        Password = ret.getStringExtra("passPassword");
        Name = ret.getStringExtra("passName");
        PhoneNo = ret.getStringExtra("passPhone");

        setTextField();

    }

    private void setTextField() {
        manage_name.setText(Name);
        manage_email.setText(Email);
        manage_phone.setText(PhoneNo);
        manage_password.setText(Password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.MSR_Delete:
                DeleteData();
                break;
            case R.id.MSR_Update:
                UpdateData();
                break;
        }
    }

    private void DeleteData() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ManageSalesRep.this);
        builder.setMessage("Are you sure you want to delete Sales rep?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String ID = ViewSalesRep.userList.get(position).getId();
                        Integer result = udb.DeleteInfo(ID);

                        if (result > 0) {
                            Toast msg = Toast.makeText(ManageSalesRep.this, "SalesRep Deleted Successfullu!!!", Toast.LENGTH_SHORT);
                            msg.show();
                        } else {
                            Toast msg = Toast.makeText(ManageSalesRep.this, "SalesRep Delete Failure!!!", Toast.LENGTH_SHORT);
                            msg.show();
                        }

                        Intent i = new Intent(ManageSalesRep.this, ViewSalesRep.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.right_enter, R.anim.left_out);

                        finish();

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

    private void UpdateData() {
        Name = manage_name.getText().toString();
        Email = manage_email.getText().toString();
        PhoneNo = manage_phone.getText().toString();
        Password = manage_password.getText().toString();

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

        // Check for both field is empty or not
        if (Name.isEmpty() || Email.isEmpty() || PhoneNo.isEmpty() || Password.isEmpty()) {
            Toast msg = Toast.makeText(ManageSalesRep.this, "Empty Input Field", Toast.LENGTH_SHORT);
            msg.show();
        } else if (!m1.find()) {
            Toast msg = Toast.makeText(ManageSalesRep.this, "Fullname Field is Invalid.", Toast.LENGTH_SHORT);
            msg.show();
        } else if (!m2.find()) {
            Toast msg = Toast.makeText(ManageSalesRep.this, "Email Field is Invalid.", Toast.LENGTH_SHORT);
            msg.show();
        } else if (!m3.find()) {
            Toast msg = Toast.makeText(ManageSalesRep.this, "Phone Number Field is Invalid.", Toast.LENGTH_SHORT);
            msg.show();
        } else if (!m4.find()) {
            Toast msg = Toast.makeText(ManageSalesRep.this, "Password required at least 1 UpperCase,1 Lowercase,1 Digit and 1 Symbol", Toast.LENGTH_LONG);
            msg.show();
        } else if (Password.length() < 8) {
            Toast msg = Toast.makeText(ManageSalesRep.this, "Minimum Password Length is 8", Toast.LENGTH_SHORT);
            msg.show();
        } else {
            String ID = ViewSalesRep.userList.get(position).getId();
            boolean result = udb.UpdateInfo(ID,Name,Email,PhoneNo,Password);

            if (result = true) {
                Toast msg = Toast.makeText(ManageSalesRep.this, "SuccessFully Updated", Toast.LENGTH_SHORT);
                msg.show();
            } else {
                Toast msg = Toast.makeText(ManageSalesRep.this, "Update Failure", Toast.LENGTH_SHORT);
                msg.show();
            }

            Intent i = new Intent(ManageSalesRep.this, ViewSalesRep.class);
            startActivity(i);
            overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ManageSalesRep.this, ViewSalesRep.class);
        startActivity(i);
        this.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
    }
}
