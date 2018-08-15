package com.mad.dms.order;

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

public class OrderViewProductAdapter extends ArrayAdapter<Product> {
    OrderViewProductAdapter(@NonNull Context context, @NonNull Product[] products) {
        super(context, R.layout.order_view_product_row, products);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        // get the row
        View view = inflater.inflate(R.layout.order_view_product_row, parent, false);
        Product product = getItem(position);

        TextView productQty = view.findViewById(R.id.orderViewProductQty);
        TextView productPrice = view.findViewById(R.id.orderViewProductPrice);
        TextView productName = view.findViewById(R.id.orderViewProductName);

        productQty.setText(Integer.toString(product.getQuantity()));
        productName.setText(product.getProductName());
        productPrice.setText(String.format("$%.2f", (product.getPrice() * product.getQuantity())));

        return view;
    }
}
