package com.project.dms.product;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.project.dms.R;

public class ProductInsert extends AppCompatActivity {

    EditText pName,pDescription,pQuantity,pPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_insert);

        pName=(EditText)findViewById(R.id.pName);
        pDescription=(EditText)findViewById(R.id.pDescription);
        pQuantity=(EditText)findViewById(R.id.pQuantity);
        pPrice=(EditText)findViewById(R.id.pPrice);

    }

    public void productInsertBtn(View view){
        pName.setText("");
        pDescription.setText("");
        pQuantity.setText("");
        pPrice.setText("");
        Toast.makeText(this,"Product Successfully Inserted!!!",Toast.LENGTH_LONG).show();
    }
}
