package com.mad.dms.orders;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mad.dms.R;
import com.mad.dms.utils.FmtHelper;

import java.sql.Date;
import java.util.Calendar;

public class OrderViewActivity extends AppCompatActivity {

    int orderId, orderPos;
    boolean pending;
    Order order;
    OrderDBHelper db;
    Intent replyIntent;

    public static final String VIEW_ORDER_REPLY_ID = "com.mad.dms.orders.ORDER_UPDATE_REPLY";
    public static final String VIEW_ORDER_REPLY_POS = "com.mad.dms.orders.ORDER_UPDATE_REPLY";

    public static final int VIEW_ORDER_DETAILS_REQUEST_CODE = 1;
    public static final String VIEW_ORDER_DETAILS_INTENT = "com.mad.dms.orders.ORDER_DETAILS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);

        db = new OrderDBHelper(this);
        Intent intent = getIntent();
        orderId = intent.getIntExtra(OrderMainActivity.VIEW_ORDER_EXTRA, -1);
        orderPos = intent.getIntExtra(OrderMainActivity.VIEW_ORDER_POS, -1);
        order = db.getOrder(orderId);

        replyIntent = new Intent();

        if (order == null) {
            setResult(RESULT_CANCELED, replyIntent);
            finish();
        }

        TextView nameText = findViewById(R.id.order_view_name);
        TextView dateText = findViewById(R.id.order_view_date);
        TextView deliText = findViewById(R.id.order_view_delivery);
        TextView statusText = findViewById(R.id.order_view_status);
        ImageView statusIconImg = findViewById(R.id.order_view_ic_status);

        pending = order.getStatus() == Order.ORDER_STATUS_PENDING;

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


        this.setTitle("Viewing: " + orderName);
        nameText.setText(orderName);
        dateText.setText(orderDate);
        deliText.setText(orderDelivery);
        statusText.setText(orderStatus);
        statusText.setTextColor(statusColor);
        statusIconImg.setImageResource(statusIcon);
        statusIconImg.setColorFilter(statusColor, PorterDuff.Mode.MULTIPLY);

        LinearLayout btnContainer;
        Button primaryBtn;
        if (pending) {
            btnContainer = findViewById(R.id.orderViewAcceptDeny);
//            btnContainer.setVisibility(View.VISIBLE);
            primaryBtn = findViewById(R.id.orderViewAcceptButton);
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

//            acceptBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (db.acceptOrder(orderId) > -1) {
//                        replyIntent.putExtra(VIEW_ORDER_REPLY_ID, orderId);
//                        replyIntent.putExtra(VIEW_ORDER_REPLY_POS, orderPos);
//                        setResult(RESULT_OK, replyIntent);
//                        finish();
//                    }
//                    showUpdateDialog(true);
//
//                }
//            });
        } else {
            btnContainer = findViewById(R.id.orderViewUpdate);
            primaryBtn = findViewById(R.id.orderViewUpdateButton);
        }
        primaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUpdateDialog(pending);
            }
        });

        btnContainer.setVisibility(View.VISIBLE);
    }

    private static EditText orderDateInput = null;

    private void showUpdateDialog(boolean accept) {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View view = inflater.inflate(R.layout.order_update_dialog, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OrderViewActivity.this);

        String positiveTxt = "Update";
        if (accept) {
            positiveTxt = "Accept";
        }

        dialogBuilder.setView(view);
        dialogBuilder
                .setCancelable(true)
                .setPositiveButton(positiveTxt, new DialogInterface.OnClickListener() {
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
//        final EditText orderInput = view.findViewById(R.id.order_update_date);
        orderDateInput = view.findViewById(R.id.order_update_date);;

        orderDateInput.setText("");
        String placeholderDate = FmtHelper.formatShortDate(order.getAcceptedDate());
        if (!accept && order.getAcceptedDate() != null) {
            orderDateInput.setText(placeholderDate);
        }

        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(orderDateInput.getText().toString())) {
                    Toast.makeText(OrderViewActivity.this, "Enter a name!", Toast.LENGTH_SHORT).show();
                } else {
//                    createOrder(orderInput.getText().toString());
                    acceptOrder(orderDateInput.getText().toString());
                    alertDialog.dismiss();
                }
            }
        });



        if (orderDateInput != null) {
            orderDateInput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDatePickerDialog(view);
                }
            });
        }
    }

    private void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void acceptOrder(String date) {
        try {
            System.out.println("ACCEPTORDER");
            order.setAcceptedDate(date);
            order.setStatus(Order.ORDER_STATUS_CONFIRMED);
            if (db.updateOrder(order) > 0) {
                System.out.println(order.toString());
                replyIntent.putExtra(VIEW_ORDER_REPLY_ID, orderId);
                replyIntent.putExtra(VIEW_ORDER_REPLY_POS, orderPos);
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateOrder(String name) {
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        public String selected;

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
            if (orderDateInput != null) {
                orderDateInput.setText(day + "/" + (month + 1) + "/" + year);
            }
        }
    }
}
