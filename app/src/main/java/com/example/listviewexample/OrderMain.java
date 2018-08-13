package com.example.listviewexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import static android.app.ProgressDialog.show;

public class OrderMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_main);

        Order[] orders = {
                new Order("Order Name", "Pending", "22/06/18"),
                new Order("Order Name 2", "Accepted", "10/08/18"),
                new Order("Order Name 3", "Accepted", "24/08/18"),
                new Order("Order Name 4", "Accepted", "24/08/18"),
                new Order("Order Name 5", "Pending", "24/08/18"),
                new Order("Order Name 6", "Accepted", "24/08/18"),
                new Order("Order Name 7", "Denied", "24/08/18"),
                new Order("Order Name 8", "Accepted", "05/09/18"),
                new Order("Order Name 9", "Accepted", "02/09/18"),
                new Order("Order Name 10", "Pending", "01/09/18"),
                new Order("Order Name 11", "Pending", "26/08/18"),
                new Order("Order Name 12", "Accepted", "25/08/18"),
                new Order("Order Name 13", "Denied", "27/08/18"),
                new Order("Order Name 15", "Accepted", "29/08/18"),
                new Order("Long Order Name Long Order Name", "Denied", "24/08/18"),
        };

        ListAdapter orderAdapter = new OrderListAdapter(this, orders);
        ListView orderList = findViewById(R.id.orderListView);
        orderList.setAdapter(orderAdapter);

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
