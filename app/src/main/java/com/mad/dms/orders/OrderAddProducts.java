package com.mad.dms.orders;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.mad.dms.R;
import com.mad.dms.product.DatabaseHelper;

import java.util.ArrayList;

public class OrderAddProducts extends AppCompatActivity {

    ArrayList<OrderProducts> products;
    OrderDBHelper db;
    OrderProductAdapter adapter = null;
    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_add_products);

        Intent intent = getIntent();
        orderId = intent.getIntExtra(OrderViewActivity.VIEW_ORDER_ADD_PRODUCTS, -1);

        db = new OrderDBHelper(this);
        products = db.getAllOrderProducts();
        adapter = new OrderProductAdapter(this, R.layout.row_order_add_product, products);
        ListView listView = findViewById(R.id.order_select_list_view);
        listView.setAdapter(adapter);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                // When clicked, show a toast with the TextView text
//                OrderProducts order = (OrderProducts) parent.getItemAtPosition(position);
//                Toast.makeText(getApplicationContext(),
//                        "Clicked on Row: " + order.getProductName(),
//                        Toast.LENGTH_LONG).show();
//            }
//        });

        Button myButton = findViewById(R.id.order_select_button);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ArrayList<OrderProducts> prodList = adapter.productList;
                ArrayList<OrderProducts> selectedProducts = new ArrayList<>();
                for (int i = 0; i < prodList.size(); i++) {
                    OrderProducts prod = prodList.get(i);
                    if (prod.getSelected()) {
                        selectedProducts.add(prod);
                    }
                }

                if (selectedProducts.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "No products selected!", Toast.LENGTH_SHORT).show();
                } else {
                    if (db.insertProducts(orderId, selectedProducts) > -1) {
                        Toast.makeText(getApplicationContext(), "INSERTED", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private class OrderProductAdapter extends ArrayAdapter<OrderProducts> {

        private ArrayList<OrderProducts> productList;

        OrderProductAdapter(Context context, int textViewResourceId, ArrayList<OrderProducts> productList) {
            super(context, textViewResourceId, productList);
            this.productList = new ArrayList<>();
            this.productList.addAll(productList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.row_order_add_product, null);

                holder = new ViewHolder();
                holder.name = convertView.findViewById(R.id.product_name);
                holder.price = convertView.findViewById(R.id.product_price);
                holder.id = convertView.findViewById(R.id.product_id);
                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        OrderProducts product = (OrderProducts) cb.getTag();
                        product.setSelected(cb.isChecked());
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            OrderProducts product = productList.get(position);
            holder.name.setText(product.getProductName());
            holder.name.setChecked(product.getSelected());
            holder.name.setTag(product);
            holder.price.setText(String.format("Rs.%.2f", product.getPrice()));
            holder.id.setText(product.getCategory());

            return convertView;

        }

        private class ViewHolder {
            CheckBox name;
            TextView price;
            TextView id;
        }

    }
}
