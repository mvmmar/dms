package com.mad.dms.product;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mad.dms.R;

import java.util.ArrayList;

public class ProductView extends AppCompatActivity {


    DatabaseHelper databaseHelper;
    ArrayList<Product> productList;
    ListView listView;
    Product product;
    Cursor data;
    int numRows;

    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

        databaseHelper = new DatabaseHelper(this);
        myDialog = new Dialog(this);

        productList = new ArrayList<>();
        final ListView custom_listView = findViewById(R.id.productListView);
        data=databaseHelper.displayProducts();
        numRows=data.getCount();

        if(numRows==0)
            Toast.makeText(ProductView.this,"No Products to Display!",Toast.LENGTH_LONG).show();
        else {
            while (data.moveToNext()) {
                product = new Product(data.getString(1), data.getString(2), data.getString(3), data.getDouble(4), data.getInt(5));
                product.setProductId(data.getInt(0));
                productList.add(product);
            }

            ArrayAdapter cus_arrayAdapter = new ProductCustomAdapter(this, productList);
            custom_listView.setAdapter(cus_arrayAdapter);

/*            custom_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                    Product product = (Product) adapterView.getItemAtPosition(pos);

                    Intent intent = new Intent(ProductView.this, ProductUpdate.class);
                    intent.putExtra("Product", product);
                    startActivity(intent);
                }
            });

            */

            custom_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                    final Product product = (Product) adapterView.getItemAtPosition(pos);

                    myDialog.setContentView(R.layout.activity_pop_up_product_manipulate);

                    final EditText qty;
                    TextView txtClose,managePro;
                    final Button btnSub,btnAdd,addStock;
                    final ProgressBar progressBar2;

                    progressBar2 = (ProgressBar)myDialog.findViewById(R.id.progressBar2);
                    progressBar2.setVisibility(View.INVISIBLE);
                    txtClose =(TextView) myDialog.findViewById(R.id.txtClose);
                    btnSub=(Button)myDialog.findViewById(R.id.btnSub);
                    btnAdd=(Button)myDialog.findViewById(R.id.btnAdd);
                    addStock=(Button)myDialog.findViewById(R.id.addStock);
                    qty=(EditText)myDialog.findViewById(R.id.qty);
                    managePro=(TextView)myDialog.findViewById(R.id.managePro);

                    int amount;

                    btnSub.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!qty.getText().toString().isEmpty()){

                                try{
                                    int amount = Integer.parseInt(qty.getText().toString());
                                    amount--;
                                    qty.setText(Integer.toString(amount));
                                }catch (Exception e){
                                    qty.setText("0");
                                }
                            }
                            else
                                qty.setText("0");

                        }
                    });

                    btnAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!qty.getText().toString().isEmpty()){

                                try{
                                    int amount = Integer.parseInt(qty.getText().toString());
                                    amount++;
                                    qty.setText(Integer.toString(amount));
                                }catch (Exception e){
                                    qty.setText("0");
                                }
                            }
                            else
                                qty.setText("0");

                        }
                    });

                    addStock.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addStock.setVisibility(View.INVISIBLE);
                            progressBar2.setVisibility(View.VISIBLE);
                            int amount = Integer.parseInt(qty.getText().toString());
                            product.setQuantity(product.Quantity+amount);
                            databaseHelper.updateProduct(product);
                            Toast.makeText(ProductView.this,"Stock Successfully Added!!!",Toast.LENGTH_LONG).show();
                            myDialog.dismiss();
                        }
                    });

                    txtClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            myDialog.dismiss();
                        }
                    });

                    managePro.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ProductView.this,ProductUpdate.class);
                            intent.putExtra("Product", product);
                            startActivity(intent);
                        }
                    });


                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    myDialog.show();

                }

            });
        }




    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseHelper = new DatabaseHelper(this);

        productList = new ArrayList<>();
        data=databaseHelper.displayProducts();
        numRows=data.getCount();

        if(numRows==0)
            Toast.makeText(ProductView.this,"No Products to Display!",Toast.LENGTH_LONG).show();
        else {
            while (data.moveToNext()) {
                product = new Product(data.getString(1), data.getString(2), data.getString(3), data.getDouble(4), data.getInt(5));
                product.setProductId(data.getInt(0));
                productList.add(product);
            }

            ArrayAdapter cus_arrayAdapter = new ProductCustomAdapter(this, productList);
            final ListView custom_listView = findViewById(R.id.productListView);
            custom_listView.setAdapter(cus_arrayAdapter);

        }



    }

    public void setOnClick(View view){

        Intent intent = new Intent(this,ProductAdd.class);
        startActivity(intent);
    }
}
