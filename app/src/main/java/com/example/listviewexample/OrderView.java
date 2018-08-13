package com.example.listviewexample;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import org.w3c.dom.Text;

public class OrderView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);
        Intent intent = getIntent();
        Order order = intent.getParcelableExtra("Order");

        ConstraintLayout orderTitleBG = findViewById(R.id.orderViewTitleContainer);
        TextView orderTitleView = findViewById(R.id.orderViewTitle);
        TextView orderDateView = findViewById(R.id.orderViewDate);
        ImageView orderStatusIcon = findViewById(R.id.orderViewStatus);

        String orderName = order.name;
        String orderStatus = order.status;
        String orderDate = order.date;

        int statusColor, statusIcon;
        switch (orderStatus) {
            case "Accepted": {
                statusColor = R.color.orderStatusAccepted;
                statusIcon = R.drawable.ic_status_accepted;
                break;
            }
            case "Pending": {
                statusColor = R.color.orderStatusPending;
                statusIcon = R.drawable.ic_status_pending;
                break;
            }
            default: {
                statusColor = R.color.orderStatusDenied;
                statusIcon = R.drawable.ic_status_denied;
            }
        }


        orderTitleView.setText(orderName);
        orderDateView.setText(orderDate);
        orderStatusIcon.setImageResource(statusIcon);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            orderTitleBG.setBackground(new ColorDrawable(getResources().getColor(statusColor)));
        }


    }
}
