package com.mad.dms.orders;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mad.dms.R;
import com.mad.dms.adminclasses.User;
import com.mad.dms.admindb.UserDBHelper;
import com.mad.dms.signin.Login;
import com.mad.dms.utils.FmtHelper;
import com.mad.dms.utils.InputValidator;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class OrderViewActivity extends AppCompatActivity {

    private int orderId;
    private int orderPos;
    private boolean pending;
    private boolean isAdmin;
    private User user;
    private Order order;
    private OrderDBHelper db;
    private UserDBHelper udb;
    private Intent replyIntent;

    private static final String VIEW_ORDER_REPLY_ID = "com.mad.dms.orders.ORDER_UPDATE_REPLY";
    public static final String VIEW_ORDER_REPLY_POS = "com.mad.dms.orders.ORDER_UPDATE_REPLY";

    public static final int VIEW_ORDER_DETAILS_REQUEST_CODE = 1;
    public static final String VIEW_ORDER_DETAILS_INTENT = "com.mad.dms.orders.ORDER_DETAILS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);

        db = new OrderDBHelper(this);
        udb = new UserDBHelper(this);

        Intent intent = getIntent();
        orderId = intent.getIntExtra(OrderMainActivity.VIEW_ORDER_EXTRA, -1);
        orderPos = intent.getIntExtra(OrderMainActivity.VIEW_ORDER_POS, -1);
        order = db.getOrder(orderId);
        replyIntent = new Intent();

        if (order == null) {
            setResult(RESULT_CANCELED, replyIntent);
            finish();
        }

        pending = order.getStatus() == Order.ORDER_STATUS_PENDING;
        isAdmin = Login.getSessionAdmin();

        System.out.println("ORDER USER: " + order.getUserId());
        if (order.getUserId() > 0) {
            user = udb.getUser(order.getUserId());
            if (user == null) {
                user = new User("Admin", "Admin", "dms@gmail.com", "", "0123456789");
            }
        } else {
            user = new User("Admin", "Admin", "dms@gmail.com", "", "0123456789");
        }

        setOrderDetails();
        setRepDetails();

        if (isAdmin) {
            Button acceptBtn = findViewById(R.id.orderViewAcceptButton);
            Button denyBtn = findViewById(R.id.orderViewDenyButton);
            denyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (db.denyOrder(orderId) > -1) {
                        replyIntent.putExtra(VIEW_ORDER_REPLY_ID, orderId);
                        replyIntent.putExtra(VIEW_ORDER_REPLY_POS, orderPos);
                        setResult(RESULT_OK, replyIntent);
                        finish();
                    }
                }
            });
            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    acceptOrder();
                }
            });
        } else {
            findViewById(R.id.orderViewAcceptDeny).setVisibility(View.GONE);
        }
    }

    private void setRepDetails() {
        TextView nameText = findViewById(R.id.order_view_rep_name);
        TextView phoneText = findViewById(R.id.order_view_rep_phone);
        TextView mailText = findViewById(R.id.order_view_rep_mail);

        nameText.setText(user.getName());
        phoneText.setText(user.getPhoneNo());
        mailText.setText(user.getEmail());
    }

    private void setOrderDetails() {
        TextView nameText = findViewById(R.id.order_view_name);
        TextView dateText = findViewById(R.id.order_view_date);
        TextView deliText = findViewById(R.id.order_view_delivery);
        TextView statusText = findViewById(R.id.order_view_status);
        ImageView statusIconImg = findViewById(R.id.order_view_ic_status);

        String orderName, orderStatus, orderDate, orderDelivery;
        int statusColor, statusIcon;
        orderName = order.getName();
        orderDate = FmtHelper.formatOrderDate(order.getDate());
        orderDelivery = FmtHelper.formatOrderDate(order.getAcceptedDate());

        switch (order.getStatus()) {
            case Order.ORDER_STATUS_CONFIRMED:
                orderStatus = "Confirmed";
                statusColor = R.color.orderStatusAccepted;
                statusIcon = R.drawable.order_ic_status_accepted;
                break;
            case Order.ORDER_STATUS_PENDING:
                orderStatus = "Pending";
                statusColor = R.color.orderStatusPending;
                statusIcon = R.drawable.order_ic_status_pending;
                break;
            default:
                orderStatus = "Denied";
                statusColor = R.color.orderStatusDenied;
                statusIcon = R.drawable.order_ic_status_denied;
        }

        statusColor = ContextCompat.getColor(this, statusColor);

        this.setTitle("Order - " + orderName);
        nameText.setText(orderName);
        dateText.setText(orderDate);
        deliText.setText(orderDelivery);
        statusText.setText(orderStatus);
        statusText.setTextColor(statusColor);
        statusIconImg.setImageResource(statusIcon);
        statusIconImg.setColorFilter(statusColor, PorterDuff.Mode.MULTIPLY);
    }

    private static EditText orderDateStr = null;

    private void showUpdateDialog() {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View view = inflater.inflate(R.layout.order_add_dialog, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OrderViewActivity.this);
        dialogBuilder.setView(view);
        dialogBuilder
                .setCancelable(true)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogBox, int i) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogBox, int i) {
                        dialogBox.cancel();
                    }
                });

        final AlertDialog alertDialog = dialogBuilder.create();
        final EditText orderInput = view.findViewById(R.id.order_add_note_input);
        orderDateStr = view.findViewById(R.id.order_add_date_input);

        orderInput.setText(order.getName());
        orderDateStr.setText(order.getFmtAcceptedDate());

        orderDateStr.setFocusable(false);
        orderDateStr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameStr = orderInput.getText().toString();
                String dateStr = orderDateStr.getText().toString();
                String errorText = InputValidator.validateNewOrder(nameStr, dateStr);

                if (errorText.equalsIgnoreCase(InputValidator.VALID)) {
                    Date deliveryDate = FmtHelper.parseShortDate(dateStr);
                    updateOrder(nameStr, deliveryDate);
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(OrderViewActivity.this, errorText, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        public String selected;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            if (orderDateStr != null) {
                String dateStr = day + "/" + (month + 1) + "/" + year;
                orderDateStr.setText(dateStr);
            }
        }
    }

    private void endIntent() {
        replyIntent.putExtra(VIEW_ORDER_REPLY_ID, orderId);
        replyIntent.putExtra(VIEW_ORDER_REPLY_POS, orderPos);
        setResult(RESULT_OK, replyIntent);
        finish();
    }

    private void updateOrder(String name, Date date) {
        try {
            order.setName(name);
            order.setAcceptedDate(date);
            if (db.updateOrder(order) > 0) {
                endIntent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void acceptOrder() {
        if (db.acceptOrder(orderId) > -1) {
            endIntent();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isAdmin || pending) {
            getMenuInflater().inflate(R.menu.order_view, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_order_edit:
                showUpdateDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
