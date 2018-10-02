package com.mad.dms.signin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.mad.dms.admin.AdminHome;
import com.mad.dms.adminclasses.User;
import com.mad.dms.admindb.UserDBHelper;
import com.mad.dms.R;
import com.mad.dms.salesrep.SalesRepHome;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private Button loginbtn;
    private EditText email_field, password_field;
    private TextView forgotPassword;
    private CheckBox show_hide_password;
    private String admin_Email, admin_Password, EmailId, Password, Position;
    public static String sessionEmail;
    private UserDBHelper udb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        udb = new UserDBHelper(this);

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

        show_hide_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton button, boolean isChecked) {
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
                checkLogin();
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


    private void checkLogin() {
        // Get email id and password
        EmailId = email_field.getText().toString();
        Password = password_field.getText().toString();

        // Check patter for email id
        String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
        Pattern p = Pattern.compile(regEx);
        Matcher m1 = p.matcher(EmailId);


        if (EmailId.isEmpty() || Password.isEmpty()) {
            Toast msg = Toast.makeText(Login.this, "Enter Both credentials.", Toast.LENGTH_SHORT);
            msg.show();
        } else if (!m1.find()) {
            Toast msg = Toast.makeText(Login.this, "Your Email Id is Invalid.", Toast.LENGTH_SHORT);
            msg.show();
        } else if (Password.length() < 8) {
            Toast msg = Toast.makeText(Login.this, "Invalid Password Length", Toast.LENGTH_SHORT);
            msg.show();
        } else {
            User user = udb.getAdminData();
            Position = user.getPosition();
            admin_Email = user.getEmail();
            admin_Password = user.getPassword();
            String user_password = udb.GetUserByEmail(EmailId);

            if (Position.equalsIgnoreCase("Admin") && EmailId.equals(admin_Email) && Password.equals(admin_Password)) {
                Intent i = new Intent(Login.this, AdminHome.class);
                startActivity(i);

                Toast msg = Toast.makeText(Login.this, "Login Successfull !!!.", Toast.LENGTH_SHORT);
                msg.show();

                finish();

            } else if (!EmailId.equals(admin_Email) && Password.equals(user_password)) {
                sessionEmail = EmailId;
                Toast msg = Toast.makeText(Login.this, "Login Successfull !!!", Toast.LENGTH_SHORT);
                msg.show();

                Intent i = new Intent(Login.this, SalesRepHome.class);
                startActivity(i);
                finish();

            } else {
                Toast msg = Toast.makeText(Login.this, "Invalid Email or Password", Toast.LENGTH_SHORT);
                msg.show();
            }


        }
    }
}



