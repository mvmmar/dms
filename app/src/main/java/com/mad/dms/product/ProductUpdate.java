package com.mad.dms.product;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mad.dms.R;

public class ProductUpdate extends AppCompatActivity {

    EditText pName, pDescription, pQuantity, pPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_update);

        pName = (EditText) findViewById(R.id.pName);
        pDescription = (EditText) findViewById(R.id.pDescription);
        pQuantity = (EditText) findViewById(R.id.pQuantity);
        pPrice = (EditText) findViewById(R.id.pPrice);
    }

    public void productUpdateBtn(View view) {
        pName.setText("");
        pDescription.setText("");
        pQuantity.setText("");
        pPrice.setText("");
        Toast.makeText(this, "Product Successfully Updated!!!", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, ProductMain.class);
        startActivity(intent);
    }
}
