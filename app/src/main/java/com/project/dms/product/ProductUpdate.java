package com.project.dms.product;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class product_update extends AppCompatActivity {

    EditText pName,pDescription,pQuantity,pPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.project.dms.R.layout.activity_product_update);

        pName=(EditText)findViewById(com.project.dms.R.id.pName);
        pDescription=(EditText)findViewById(com.project.dms.R.id.pDescription);
        pQuantity=(EditText)findViewById(com.project.dms.R.id.pQuantity);
        pPrice=(EditText)findViewById(com.project.dms.R.id.pPrice);
    }

    public void productUpdateBtn(View view){
        pName.setText("");
        pDescription.setText("");
        pQuantity.setText("");
        pPrice.setText("");
        Toast.makeText(this,"Product Successfully Updated!!!",Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this,ProductMain.class);
        startActivity(intent);
    }
}
