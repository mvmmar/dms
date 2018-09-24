package com.mad.dms.orders;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mad.dms.R;
import com.mad.dms.utils.FmtHelper;

public class OrderViewActivity extends AppCompatActivity {

    int orderId, orderPos;
    Order order;
    OrderDBHelper db;
    Intent replyIntent;

    public static final String VIEW_ORDER_REPLY_ID = "com.mad.dms.orders.ORDER_UPDATE_REPLY";
    public static final String VIEW_ORDER_REPLY_POS = "com.mad.dms.orders.ORDER_UPDATE_REPLY";

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
        Button acceptBtn = findViewById(R.id.orderViewAcceptButton);
        Button denyBtn = findViewById(R.id.orderViewDenyButton);

        String orderName, orderStatus, orderDate, orderDelivery;
        int statusColor, statusIcon;
        orderName = order.getName();
        orderDate = FmtHelper.formatOrderDate(order.getDate());
        orderDelivery = "--/--/--";

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
                if (db.acceptOrder(orderId) > -1) {
                    replyIntent.putExtra(VIEW_ORDER_REPLY_ID, orderId);
                    replyIntent.putExtra(VIEW_ORDER_REPLY_POS, orderPos);
                    setResult(RESULT_OK, replyIntent);
                    finish();
                }
            }
        });
    }
}
