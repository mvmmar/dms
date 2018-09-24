package com.mad.dms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.mad.dms.R;
import com.mad.dms.orders.OrderMainActivity;
import com.mad.dms.product.ProductView;

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
                menu = OrderMainActivity.class;
                break;
            case R.id.main_view_products:
                menu = ProductView.class;
                break;
            default:
                menu = ProductView.class;
        }

        Intent intent = new Intent(MainActivity.this, menu);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
