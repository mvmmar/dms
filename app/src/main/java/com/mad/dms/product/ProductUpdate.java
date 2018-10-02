package com.mad.dms.product;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.mad.dms.R;

public class ProductUpdate extends AppCompatActivity {

    EditText pName,pDescription,pQuantity,pPrice;
    ProgressBar progressBar;
    Button btnUpdate,btnDelete;
    Spinner spinner;
    DatabaseHelper databaseHelper;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_update);

        databaseHelper=new DatabaseHelper(this);

        Intent intent = getIntent();
        product = intent.getParcelableExtra("Product");

        pName = findViewById(R.id.pName);
        pDescription = findViewById(R.id.pDescription);
        pQuantity = findViewById(R.id.pQuantity);
        pPrice = findViewById(R.id.pPrice);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);
        spinner = findViewById(R.id.spinner);

        pName.setText(product.getProductName());
        String category = product.getCategory();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.category,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if(category!=null){
            int spinnerPos=adapter.getPosition(category);
            spinner.setSelection(spinnerPos);
        }
        pDescription.setText(product.getDescription());
        pQuantity.setText(Integer.toString(product.getQuantity()));
        pPrice.setText(Double.toString(product.getPrice()));

        pName.setEnabled(false);
        spinner.setEnabled(false);
        pDescription.setEnabled(false);
        pQuantity.setEnabled(false);
        pPrice.setEnabled(false);


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnUpdate.getText().toString().equals("EDIT")){
                    btnUpdate.setText("UPDATE");
                    pName.setEnabled(true);
                    spinner.setEnabled(true);
                    pDescription.setEnabled(true);
                    pQuantity.setEnabled(true);
                    pPrice.setEnabled(true);
                }
                else{

                    try{

                        if(!pName.getText().toString().isEmpty()){
                            if(!spinner.getSelectedItem().toString().equals("Please select a Category")){
                                if(!pDescription.getText().toString().isEmpty()){
                                    if (!pPrice.getText().toString().isEmpty()){
                                        if(!pQuantity.getText().toString().isEmpty()){

                                            btnUpdate.setVisibility(View.INVISIBLE);
                                            btnDelete.setVisibility(View.INVISIBLE);
                                            progressBar.setVisibility(View.VISIBLE);

                                            String pro_Name=pName.getText().toString();
                                            String pro_Category=spinner.getSelectedItem().toString();
                                            String pro_Description=pDescription.getText().toString();
                                            double pro_Price=Double.parseDouble(pPrice.getText().toString());
                                            int pro_Qty=Integer.parseInt(pQuantity.getText().toString());

                                            Product product1 = new Product(pro_Name,pro_Category,pro_Description,pro_Price,pro_Qty);
                                            product1.setProductId(product.getProductId());
                                            updateData(product1);

                                            Intent intent = new Intent(ProductUpdate.this,ProductView.class);
                                            startActivity(intent);
                                        }
                                        else
                                            Toast.makeText(ProductUpdate.this,"Quantity cannot be Empty!!!",Toast.LENGTH_LONG).show();
                                    }
                                    else
                                        Toast.makeText(ProductUpdate.this,"Price cannot be Empty!!!",Toast.LENGTH_LONG).show();
                                }
                                else
                                    Toast.makeText(ProductUpdate.this,"Description cannot be Empty!!!",Toast.LENGTH_LONG).show();
                            }
                            else
                                Toast.makeText(ProductUpdate.this,"Category should be Selected!!!",Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(ProductUpdate.this,"Product Name cannot be Empty!!!",Toast.LENGTH_LONG).show();


                    }
                    catch (Exception e){
                        Toast.makeText(ProductUpdate.this,"Invalid Numeric Input!!!",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnUpdate.setVisibility(View.INVISIBLE);
                btnDelete.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                databaseHelper.deleteProduct(product.getProductId());

                pName.setText("");
                pDescription.setText("");
                pQuantity.setText("");
                pPrice.setText("");
                Toast.makeText(ProductUpdate.this,"Product Successfully Deleted!!!",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(ProductUpdate.this,ProductView.class);
                startActivity(intent);
            }
        });
    }


    public void updateData(Product product){

        if(databaseHelper.updateProduct(product)){
            Toast.makeText(ProductUpdate.this,"Successfully Updated!!!",Toast.LENGTH_LONG).show();
            pName.setText("");
            pDescription.setText("");
            pPrice.setText("");
            pQuantity.setText("");
        }


        else
            Toast.makeText(ProductUpdate.this,"Error while Updating....",Toast.LENGTH_LONG).show();
    }

}
