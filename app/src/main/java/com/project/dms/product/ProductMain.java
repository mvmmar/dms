package com.project.dms.product;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ProductMain extends AppCompatActivity {

    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.project.dms.R.layout.activity_product_main);

        myDialog = new Dialog(this);

        Product[] products = {
                new Product("Product Name","product Description",150.00,0),
                new Product("Product Name","product Description",250.00,5),
                new Product("Product Name","product Description",350.00,10),
                new Product("Product Name","product Description",500.00,15),
        };

        final ListView custom_listView=findViewById(com.project.dms.R.id.productListView);

        ArrayAdapter cus_arrayAdapter = new ProductCustomAdapter(this,products);
        custom_listView.setAdapter(cus_arrayAdapter);

        custom_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView txtClose;
                Button updateBtn;
                myDialog.setContentView(com.project.dms.R.layout.activity_pop_up_product_manipulate);
                txtClose =(TextView) myDialog.findViewById(com.project.dms.R.id.txtClose);
                updateBtn=(Button)myDialog.findViewById(com.project.dms.R.id.updateBtn);
                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ProductMain.this,ProductUpdate.class);
                        startActivity(intent);
                    }
                });
                txtClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();

            }
        });

    }

    public void showPopup(View v){
        //String selected = (String) custom_listView.getItemAtPosition(i);
        //Toast toast=Toast.makeText(ProductMain.this,selected,Toast.LENGTH_LONG);
        //toast.setGravity(Gravity.TOP,0,0);
        //toast.show();
    }

    public void addBtnClick(View view){
        Intent intent = new Intent(this,ProductInsert.class);
        startActivity(intent);
    }

}
