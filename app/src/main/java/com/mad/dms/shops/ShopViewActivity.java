package com.mad.dms.shops;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.mad.dms.R;

import java.util.ArrayList;

public class ShopViewActivity extends AppCompatActivity {
    ListView listview;
    ArrayList<Shop> shopArrayList;
    Shop shop;
    Cursor cursor;
    ShopDB shop_db;

    Button sAdd_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_view);

        shop_db = new ShopDB(this);

        shopArrayList = new ArrayList<>();
        listview = (ListView) findViewById(R.id.shoplist);
        cursor = shop_db.getshopdata();
        int numRows = cursor.getCount();

        if (numRows == 0)
            Toast.makeText(ShopViewActivity.this, "No Shops to Display!", Toast.LENGTH_LONG).show();
        else {
            while (cursor.moveToNext()) {
                shop = new Shop(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                shop.setShopid(cursor.getInt(0));
                shopArrayList.add(shop);
            }

            ArrayAdapter custom_obj = new ShopViewAdapter(this, shopArrayList);
            listview.setAdapter(custom_obj);

        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View vie, int i, long l) {
                Shop shop = (Shop) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(ShopViewActivity.this, ShopEditActivity.class);
                intent.putExtra("shopdetails", (Parcelable) shop);
                startActivity(intent);
            }

        });

        sAdd_btn = (Button) findViewById(R.id.sAdd_btn);

        sAdd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShopViewActivity.this, ShopInsertActivity.class);
                startActivity(intent);
            }
        });
    }

}
