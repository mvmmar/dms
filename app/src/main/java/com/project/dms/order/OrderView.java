package com.project.dms.order;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.project.dms.R;
import com.project.dms.product.Product;

public class OrderView extends AppCompatActivity {

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);

        Intent intent = getIntent();
        Order order = intent.getParcelableExtra("Order");
        this.setTitle(order.name);

        Product[] products = {
                new Product("Product","product Description",1.23,2),
                new Product("Product","product Description",4.49,1),
                new Product("Product","product Description",9.99,4),
                new Product("Product","product Description",19.99,1),
                new Product("Product","product Description",1.25,3),
                new Product("Product","product Description",1.30,1),
                new Product("Product","product Description",3.99,2),
                new Product("Product","product Description",2.49,1),
        };

        double productsTotal = 0;
        double tax = 0.1;
        double totalTax = 0;

        if (products != null) {
            ListAdapter orderAdapter = new OrderViewProductAdapter(this, products);
            ListView orderList = findViewById(R.id.orderViewProductsList);
            orderList.setAdapter(orderAdapter);

            for (Product product: products) {
                productsTotal += product.getQuantity() + product.getPrice();
            }
        }
        totalTax = tax * productsTotal;

        TextView orderDate = findViewById(R.id.orderDeliveryDate);
        TextView orderStatus = findViewById(R.id.orderViewStatus);
        ImageView orderStatusIcon = findViewById(R.id.orderViewStatusIcon);

        int statusColor, statusIcon;
        switch (order.status) {
            case "Accepted": {
                statusColor = R.color.orderStatusAccepted;
                statusIcon = R.drawable.ic_status_accepted;
                break;
            }
            case "Pending": {
                statusColor = R.color.orderStatusPending;
                statusIcon = R.drawable.ic_status_pending;
                order.date = "--/--/--";
                LinearLayout acceptButton = findViewById(R.id.orderViewAcceptDeny);
                acceptButton.setVisibility(View.VISIBLE);
                break;
            }
            default: {
                statusColor = R.color.orderStatusDenied;
                statusIcon = R.drawable.ic_status_denied;
            }
        }

        statusColor = ContextCompat.getColor(this, statusColor);
        orderDate.setText(order.date);
        orderStatus.setText(order.status);
        orderStatus.setTextColor(statusColor);
        orderStatusIcon.setImageResource(statusIcon);
        orderStatusIcon.setColorFilter(statusColor, PorterDuff.Mode.MULTIPLY);

        TextView orderSubtotalValue = findViewById(R.id.subTotalValue);
        TextView orderTaxesValue = findViewById(R.id.orderTaxesValue);
        TextView orderTotal = findViewById(R.id.orderTotalValue);

        orderSubtotalValue.setText(String.format("$%.2f", productsTotal));
        orderTaxesValue.setText(String.format("$%.2f", totalTax));
        orderTotal.setText(String.format("$%.2f", (productsTotal + totalTax)));
    }
}
