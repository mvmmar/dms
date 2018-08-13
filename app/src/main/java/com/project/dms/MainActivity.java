package com.project.dms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.project.dms.order.OrderMain;
import com.project.dms.product.ProductMain;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void setOnClick(View view) {
        int id = view.getId();

        Class menu;
        switch (id) {
            case R.id.main_view_orders:
                menu = OrderMain.class;
                break;
            case R.id.main_view_products:
                menu = ProductMain.class;
                break;
            default:
                menu = ProductMain.class;
        }

        Intent intent = new Intent(MainActivity.this, menu);
        startActivity(intent);
    }
}
