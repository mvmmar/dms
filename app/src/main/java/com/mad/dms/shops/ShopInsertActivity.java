package com.mad.dms.shops;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mad.dms.R;

public class ShopInsertActivity extends AppCompatActivity {

    EditText sid, sname, scustomer, saddress, scontact;
    ShopDB shop_db;
    Button sInsert_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_insert);
        shop_db = new ShopDB(this);

        sname = (EditText) findViewById(R.id.sname);
        scustomer = (EditText) findViewById(R.id.escustomer);
        saddress = (EditText) findViewById(R.id.saddress);
        scontact = (EditText) findViewById(R.id.scontact);

        sInsert_btn = (Button) findViewById(R.id.sInsert_btn);


        sInsert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    if (!sname.getText().toString().isEmpty()) {
                        if (!scustomer.getText().toString().isEmpty()) {
                            if (!saddress.getText().toString().isEmpty()) {
                                if (!scontact.getText().toString().isEmpty()) {

                                    String shop_Name = sname.getText().toString();
                                    String shop_Customer = scustomer.getText().toString();
                                    String shop_Address = saddress.getText().toString();
                                    String shop_Contact = scontact.getText().toString();

                                    Shop shop = new Shop(shop_Name, shop_Customer, shop_Address, shop_Contact);
                                    addShop(shop);
                                } else
                                    Toast.makeText(ShopInsertActivity.this, "Contact  cannot be Empty!!!", Toast.LENGTH_LONG).show();
                            } else
                                Toast.makeText(ShopInsertActivity.this, "Address cannot be Empty!!!", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(ShopInsertActivity.this, "Customer Name cannot be Empty!!!", Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(ShopInsertActivity.this, "Shop Name cannot be Empty!!!", Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    Toast.makeText(ShopInsertActivity.this, "Invalid Input!!!", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    public void addShop(Shop shop) {

        if (shop_db.insertShop(shop)) {
            Toast.makeText(ShopInsertActivity.this, "Successfully Inserted!!!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ShopInsertActivity.this, ShopViewActivity.class);
            startActivity(intent);

        } else
            Toast.makeText(ShopInsertActivity.this, "Error while Inserting....", Toast.LENGTH_LONG).show();
        sname.setText("");
        scustomer.setText("");
        saddress.setText("");
        scontact.setText("");
    }

}
