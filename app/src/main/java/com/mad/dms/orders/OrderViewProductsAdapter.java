package com.mad.dms.orders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mad.dms.R;
import com.mad.dms.product.Product;

import java.util.ArrayList;

public class OrderViewProductsAdapter extends ArrayAdapter<Product> {

    public OrderViewProductsAdapter(@NonNull Context context, ArrayList<Product> products) {
        super(context, R.layout.product_custom_row, products);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //Now we are manually giving the operating the ArrayAdapter
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View cusView = inflater.inflate(R.layout.row_order_products, parent, false);

        //For array we've passed it works in a loop
        Product product = (Product) getItem(position);
        String proName = product.ProductName;
        String proCategory = product.Category;
        double proPrice = product.Price;

        TextView productName = cusView.findViewById(R.id.v_product_name);
        TextView productDes = cusView.findViewById(R.id.v_product_cat);
        TextView productPrice = cusView.findViewById(R.id.v_product_price);


        productName.setText(proName);
        productDes.setText(proCategory);
        productPrice.setText("Rs." + Double.toString(proPrice));

        return cusView;
    }
}
