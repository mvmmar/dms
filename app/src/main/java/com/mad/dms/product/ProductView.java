package com.mad.dms.product;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    EditText editText;

    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

        databaseHelper = new DatabaseHelper(this);
        editText  =(EditText)findViewById(R.id.editText);
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
                    TextView txtClose,managePro,productName;
                    final Button btnSub,btnAdd,addStock;
                    final ProgressBar progressBar2;
                    ImageView image = (ImageView)myDialog.findViewById(R.id.image);
                    String proCategory = product.getCategory();

                    switch (proCategory){
                        case "Food":
                            image.setImageResource(R.drawable.breakfast);
                            break;

                        case "Cosmatic":
                            image.setImageResource(R.drawable.cosmetics);
                            break;

                        case "Stationary":
                            image.setImageResource(R.drawable.stationary);
                            break;
                    }

                    progressBar2 = (ProgressBar)myDialog.findViewById(R.id.progressBar2);
                    progressBar2.setVisibility(View.INVISIBLE);
                    txtClose =(TextView) myDialog.findViewById(R.id.txtClose);
                    productName=(TextView) myDialog.findViewById(R.id.productName);
                    productName.setText(product.getProductName());
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
                            try{
                                int amount = Integer.parseInt(qty.getText().toString());
                                if(product.Quantity+amount<0){
                                    Toast.makeText(ProductView.this,"Invalid update amount!!!",Toast.LENGTH_LONG).show();
                                    addStock.setVisibility(View.VISIBLE);
                                    progressBar2.setVisibility(View.INVISIBLE);
                                }
                                else {
                                    product.setQuantity(product.Quantity+amount);
                                    databaseHelper.updateProduct(product);
                                    Toast.makeText(ProductView.this,"Stock Successfully Updated!!!",Toast.LENGTH_LONG).show();
                                    loadProducts();
                                    myDialog.dismiss();
                                }

                            }
                            catch (Exception e){
                                Toast.makeText(ProductView.this,"Invalid Input!",Toast.LENGTH_LONG).show();
                                qty.setText("0");
                                addStock.setVisibility(View.VISIBLE);
                                progressBar2.setVisibility(View.INVISIBLE);
                            }



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

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int textLength=charSequence.length();
                ArrayList<Product> tempArrayList = new ArrayList<>();
                if(charSequence.toString().equals("!")){
                    for(Product p:productList){
                        if(p.getQuantity()<10){
                            tempArrayList.add(p);
                        }
                    }
                }
                else{
                    for(Product p:productList){
                        if(textLength<=p.getProductName().length()){
                            if(p.getProductName().toLowerCase().contains(charSequence.toString().toLowerCase())){
                                tempArrayList.add(p);
                            }
                        }
                    }
                }
                if(tempArrayList.size()==0){
                    Toast.makeText(ProductView.this,"No Match Found!!!",Toast.LENGTH_SHORT).show();
                }else {
                    ArrayAdapter cus_arrayAdapter = new ProductCustomAdapter(getApplicationContext(), tempArrayList);
                    final ListView custom_listView = findViewById(R.id.productListView);
                    custom_listView.setAdapter(cus_arrayAdapter);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });





    }

    private void loadProducts(){

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

            custom_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                    final Product product = (Product) adapterView.getItemAtPosition(pos);

                    myDialog.setContentView(R.layout.activity_pop_up_product_manipulate);

                    final EditText qty;
                    TextView txtClose,managePro,productName;
                    final Button btnSub,btnAdd,addStock;
                    final ProgressBar progressBar2;
                    ImageView image = (ImageView)myDialog.findViewById(R.id.image);
                    String proCategory = product.getCategory();

                    switch (proCategory){
                        case "Food":
                            image.setImageResource(R.drawable.breakfast);
                            break;

                        case "Cosmatic":
                            image.setImageResource(R.drawable.cosmetics);
                            break;

                        case "Stationary":
                            image.setImageResource(R.drawable.stationary);
                            break;
                    }

                    progressBar2 = (ProgressBar)myDialog.findViewById(R.id.progressBar2);
                    progressBar2.setVisibility(View.INVISIBLE);
                    txtClose =(TextView) myDialog.findViewById(R.id.txtClose);
                    productName=(TextView) myDialog.findViewById(R.id.productName);
                    productName.setText(product.getProductName());
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
                            try{
                                int amount = Integer.parseInt(qty.getText().toString());
                                if(product.Quantity+amount<0) {
                                    Toast.makeText(ProductView.this, "Invalid update amount!!!", Toast.LENGTH_LONG).show();
                                    addStock.setVisibility(View.VISIBLE);
                                    progressBar2.setVisibility(View.INVISIBLE);
                                }
                                else {
                                    product.setQuantity(product.Quantity+amount);
                                    databaseHelper.updateProduct(product);
                                    Toast.makeText(ProductView.this,"Stock Successfully Updated!!!",Toast.LENGTH_LONG).show();
                                    loadProducts();
                                    myDialog.dismiss();
                                }
                            }
                            catch (Exception e){
                                Toast.makeText(ProductView.this,"Invalid Input!",Toast.LENGTH_LONG).show();
                                qty.setText("0");
                                addStock.setVisibility(View.VISIBLE);
                                progressBar2.setVisibility(View.INVISIBLE);
                            }
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

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int textLength=charSequence.length();
                ArrayList<Product> tempArrayList = new ArrayList<>();
                if(charSequence.toString().equals("!")){
                    for(Product p:productList){
                        if(p.getQuantity()<10){
                            tempArrayList.add(p);
                        }
                    }
                }
                else{
                    for(Product p:productList){
                        if(textLength<=p.getProductName().length()){
                            if(p.getProductName().toLowerCase().contains(charSequence.toString().toLowerCase())){
                                tempArrayList.add(p);
                            }
                        }
                    }
                }
                if(tempArrayList.size()==0){
                    Toast.makeText(ProductView.this,"No Match Found!!!",Toast.LENGTH_SHORT).show();
                }else {
                    ArrayAdapter cus_arrayAdapter = new ProductCustomAdapter(getApplicationContext(), tempArrayList);
                    final ListView custom_listView = findViewById(R.id.productListView);
                    custom_listView.setAdapter(cus_arrayAdapter);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        loadProducts();

    }

    public void setOnClick(View view){

        Intent intent = new Intent(this,ProductAdd.class);
        startActivity(intent);
    }
}
