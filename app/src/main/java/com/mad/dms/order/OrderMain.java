package com.mad.dms.order;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mad.dms.R;

public class OrderMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_main);

        Order[] orders = {
                new Order("New Order", "Pending", "-"),
                new Order("New Order 2", "Pending", "-"),
                new Order("Accepted Order", "Accepted", "24/08/18"),
                new Order("Denied Order", "Denied", ""),
                new Order("Accepted Order 2", "Accepted", "12/08/18"),
                new Order("Accepted Order 3", "Accepted", "10/08/18"),
                new Order("Denied Order 2", "Denied", ""),
                new Order("Accepted Order 4", "Accepted", "08/08/18"),
        };

        ListAdapter orderAdapter = new OrderListAdapter(this, orders);
        ListView orderList = findViewById(R.id.orderListView);
        orderList.setAdapter(orderAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add new order", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        orderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Order order = (Order) adapterView.getItemAtPosition(pos);
                Intent intent = new Intent(OrderMain.this, OrderView.class);
                intent.putExtra("Order", order);
                startActivity(intent);
                //Toast.makeText(MainActivity.this, order.name, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
