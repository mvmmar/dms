package com.mad.dms.SignIn;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mad.dms.Classes.AdminData;
import com.mad.dms.DataBase.UserDBHelper;
import com.mad.dms.R;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {
    private UserDBHelper udb;
    private TextView submit, back;
    private EditText email_field;
    private ProgressDialog progressdialog;
    private String toEmail = null, body = null, resetPassword = null;
    private Session session = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        udb = new UserDBHelper(this);

        email_field = findViewById(R.id.FP_email);
        submit = findViewById(R.id.FP_submit);
        back = findViewById(R.id.FP_back);

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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void resetPassword() {
        toEmail = email_field.getText().toString();
        resetPassword = udb.GetUserByEmail(toEmail);

        if (!isNetworkAvailable()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setTitle("No Internet Connection");
            builder.setMessage("\nTurn on Wi-Fi OR Mobile Data")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();

        } else if (resetPassword == null) {
            Toast msg = Toast.makeText(ForgotPassword.this, "Enter your Registered Email ID. ", Toast.LENGTH_LONG);
            msg.show();
        } else {
            body = "Hello,\n" +
                    "Your Current Password for DMS --> " + resetPassword +
                    "\nIf you didn’t ask to get your password, you can ignore this email.\n\n" +
                    "Thanks,\n" +
                    "Your DMS team";

            String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
            Pattern p = Pattern.compile(regEx);
            Matcher m1 = p.matcher(toEmail);

            if (toEmail.isEmpty()) {
                Toast msg = Toast.makeText(ForgotPassword.this, "Email Field is Empty.\nEnter your Registered Email ID. ", Toast.LENGTH_LONG);
                msg.show();
            } else if (!m1.find()) {
                Toast msg = Toast.makeText(ForgotPassword.this, "Your Email Id is Invalid.", Toast.LENGTH_SHORT);
                msg.show();
            } else {
                if (toEmail == null) {
                    Toast.makeText(ForgotPassword.this, "User not found,\nPlease Enter Correct Email", Toast.LENGTH_SHORT).show();
                } else {
                    Properties props = new Properties();
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    props.put("mail.smtp.socketFactory.port", "465");
                    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.port", "465");
                    props.put("mail.smtp.starttls.enable", "true");
                    props.put("mail.smtp.socketFactory.fallback", "false");

                    session = Session.getInstance(props,
                            new javax.mail.Authenticator() {
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(AdminData.fromEmail, AdminData.password);
                                }
                            });

                    progressdialog = ProgressDialog.show(ForgotPassword.this, "", "Sending Password, Please wait...", true);

                    RetrevieFeedTask task = new RetrevieFeedTask();
                    task.execute();
                }
            }
        }
    }

    class RetrevieFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                MimeMessage msg = new MimeMessage(session);
                //set message headers
                msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
                msg.addHeader("format", "flowed");
                msg.addHeader("Content-Transfer-Encoding", "8bit");
                msg.setFrom(new InternetAddress(AdminData.fromEmail, "DMS"));
                msg.setReplyTo(InternetAddress.parse(AdminData.fromEmail, false));
                msg.setSubject(AdminData.Subject, "UTF-8");
                msg.setText(body, "UTF-8");
                msg.setSentDate(new Date());
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
                Transport.send(msg);

            } catch (UnsupportedEncodingException | MessagingException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressdialog.cancel();
            Toast.makeText(ForgotPassword.this, "Email Sent Successfully!!", Toast.LENGTH_SHORT).show();
        }
    }
}


