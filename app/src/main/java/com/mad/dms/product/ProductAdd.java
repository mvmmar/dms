package com.mad.dms.product;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mad.dms.R;

public class ProductAdd extends AppCompatActivity {

    EditText pName,pDescription,pQuantity,pPrice;
    Spinner spinner;
    Button btn_add;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_add);

        pName = findViewById(R.id.pName);
        pDescription = findViewById(R.id.pDescription);
        pQuantity = findViewById(R.id.pQuantity);
        pPrice = findViewById(R.id.pPrice);
        spinner = findViewById(R.id.spinner);
        btn_add = findViewById(R.id.btn_add);
        databaseHelper= new DatabaseHelper(this);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{

                    if(!pName.getText().toString().isEmpty()){
                        if(!spinner.getSelectedItem().toString().isEmpty()){
                            if(!pDescription.getText().toString().isEmpty()){
                                if (!pPrice.getText().toString().isEmpty()){
                                    if(!pQuantity.getText().toString().isEmpty()){

                                        String pro_Name=pName.getText().toString();
                                        String pro_Category=spinner.getSelectedItem().toString();

                                        if(!pro_Category.equals("Please select a Category")){
                                            String pro_Description=pDescription.getText().toString();
                                            double pro_Price=Double.parseDouble(pPrice.getText().toString());
                                            int pro_Qty=Integer.parseInt(pQuantity.getText().toString());

                                            Product product = new Product(pro_Name,pro_Category,pro_Description,pro_Price,pro_Qty);
                                            addData(product);

                                        }
                                        else
                                            Toast.makeText(ProductAdd.this,"Category should be Selected!!!",Toast.LENGTH_LONG).show();

                                    }
                                    else
                                        Toast.makeText(ProductAdd.this,"Quantity cannot be Empty!!!",Toast.LENGTH_LONG).show();
                                }
                                else
                                    Toast.makeText(ProductAdd.this,"Price cannot be Empty!!!",Toast.LENGTH_LONG).show();
                            }
                            else
                                Toast.makeText(ProductAdd.this,"Description cannot be Empty!!!",Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(ProductAdd.this,"Category should be Selected!!!",Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(ProductAdd.this,"Product Name cannot be Empty!!!",Toast.LENGTH_LONG).show();


                }
                catch (Exception e){
                    Toast.makeText(ProductAdd.this,"Invalid Numeric Input!!!",Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    public void addData(Product product){

        if(databaseHelper.insertProduct(product)){
            Toast.makeText(ProductAdd.this,"Successfully Inserted!!!",Toast.LENGTH_LONG).show();
            pName.setText("");
            pDescription.setText("");
            pPrice.setText("");
            pQuantity.setText("");
        }


        else
            Toast.makeText(ProductAdd.this,"Error while Inserting....",Toast.LENGTH_LONG).show();
    }
}

