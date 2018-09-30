package com.mad.dms.product;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mad.dms.R;

import java.util.ArrayList;

public class ProductCustomAdapter extends ArrayAdapter {

    public ProductCustomAdapter(@NonNull Context context, ArrayList<Product> products) {
        super(context, R.layout.product_custom_row, products);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //Now we are manually giving the operating the ArrayAdapter
        LayoutInflater inflater= LayoutInflater.from(getContext());
        View cusView = inflater.inflate(R.layout.product_custom_row,parent,false);

        //For array we've passed it works in a loop
        Product product=(Product) getItem(position);
        String proName = product.ProductName;
        String proCategory= product.Category;
        String proDescription=product.Description;
        double proPrice = product.Price;
        int proQty = product.Quantity;

        TextView productName = (TextView) cusView.findViewById(R.id.proName);
        TextView productDes = (TextView) cusView.findViewById(R.id.proDescription);
        TextView productPrice = (TextView) cusView.findViewById(R.id.proPrice);
        TextView productQuantity = (TextView) cusView.findViewById(R.id.proQuantity);
        ImageView productIcon=(ImageView)cusView.findViewById(R.id.productIcon);

        switch (proCategory){
            case "Food":
                productIcon.setImageResource(R.drawable.breakfast);
                break;

            case "Cosmatic":
                productIcon.setImageResource(R.drawable.cosmetics);
                break;

            case "Stationary":
                productIcon.setImageResource(R.drawable.stationary);
                break;
        }

        productName.setText(proName);
        productDes.setText(proDescription);
        productPrice.setText("Rs."+Double.toString(proPrice));
        if(proQty<10)
            productQuantity.setTextColor(Color.RED);
        productQuantity.setText("Qty: "+Integer.toString(proQty));

        //ImageView image =(ImageView)cusView.findViewById(R.id.image);
        //image.setImageResource(R.drawable.ic_launcher_foreground);

        return cusView;

    }
}
