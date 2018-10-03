package com.mad.dms.shops;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mad.dms.R;

public class ShopEditActivity extends AppCompatActivity {

    EditText esname, escustomer, esaddress, escontact;
    ShopDB shop_db;
    Button sEdit_btn, sDelete_btn;
    Shop shop_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_edit);

        shop_db = new ShopDB(this);

        esname = (EditText) findViewById(R.id.esname);
        escustomer = (EditText) findViewById(R.id.escustomer);
        esaddress = (EditText) findViewById(R.id.esaddress);
        escontact = (EditText) findViewById(R.id.escontact);

        sEdit_btn = (Button) findViewById(R.id.sEdit_btn);
        sDelete_btn = (Button) findViewById(R.id.sDelete_btn);

        Intent intent = getIntent();
        shop_details = intent.getParcelableExtra("shopdetails");

        esname.setText(shop_details.getShopname());
        escustomer.setText(shop_details.getShopcustomer());
        escontact.setText(shop_details.getShopcontact());
        esaddress.setText(shop_details.getShopaddress());

        sEdit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (!esname.getText().toString().isEmpty()) {
                        if (!escustomer.getText().toString().isEmpty()) {
                            if (!esaddress.getText().toString().isEmpty()) {
                                if (!escontact.getText().toString().isEmpty()) {

                                    String eshop_Name = esname.getText().toString();
                                    String eshop_Customer = escustomer.getText().toString();
                                    String eshop_Address = esaddress.getText().toString();
                                    String eshop_Contact = escontact.getText().toString();

                                    Shop shop_ob = new Shop(eshop_Name, eshop_Customer, eshop_Address, eshop_Contact);
                                    shop_ob.setShopid(shop_details.getShopid());
                                    editShop(shop_ob);

                                    Intent intent = new Intent(ShopEditActivity.this, ShopViewActivity.class);
                                    startActivity(intent);

                                } else
                                    Toast.makeText(ShopEditActivity.this, "Contact  cannot be Empty!!!", Toast.LENGTH_LONG).show();
                            } else
                                Toast.makeText(ShopEditActivity.this, "Address cannot be Empty!!!", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(ShopEditActivity.this, "Customer Name cannot be Empty!!!", Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(ShopEditActivity.this, "Shop Name cannot be Empty!!!", Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    Toast.makeText(ShopEditActivity.this, "Invalid Input!!!" + e, Toast.LENGTH_LONG).show();
                }


            }
        });

        sDelete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shop_db.deleteShop(shop_details.getShopid());

                Toast.makeText(ShopEditActivity.this, "Shop Details Successfully Deleted!!!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(ShopEditActivity.this, ShopViewActivity.class);
                startActivity(intent);
            }
        });

    }

    public void editShop(Shop shop) {

        if (shop_db.updateShop(shop)) {
            Toast.makeText(ShopEditActivity.this, "Successfully Updated!!!", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(ShopEditActivity.this, "Error while Updating....", Toast.LENGTH_LONG).show();
    }

}
