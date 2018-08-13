package com.project.dms.product;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ProductCustomAdapter extends ArrayAdapter {

    public ProductCustomAdapter(@NonNull Context context, Product[] products) {
        super(context,com.project.dms.R.layout.product_custom_row, products);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //Now we are manually giving the operating the ArrayAdapter
        LayoutInflater inflater= LayoutInflater.from(getContext());
        View cusView = inflater.inflate(com.project.dms.R.layout.product_custom_row,parent,false);

        //For array we've passed it works in a loop
        Product product=(Product) getItem(position);
        String proName = product.ProductName;
        String proDescription=product.Description;
        double proPrice = product.Price;
        int proQty = product.Quantity;

        TextView productName = (TextView) cusView.findViewById(com.project.dms.R.id.proName);
        TextView productDes = (TextView) cusView.findViewById(com.project.dms.R.id.proDescription);
        TextView productPrice = (TextView) cusView.findViewById(com.project.dms.R.id.proPrice);
        TextView productQuantity = (TextView) cusView.findViewById(com.project.dms.R.id.proQuantity);

        productName.setText(proName);
        productDes.setText(proDescription);
        productPrice.setText(Double.toString(proPrice));
        productQuantity.setText(Integer.toString(proQty));

        //ImageView image =(ImageView)cusView.findViewById(R.id.image);
        //image.setImageResource(R.drawable.ic_launcher_foreground);

        return cusView;

    }
}
