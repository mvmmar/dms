package com.mad.dms.shops;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mad.dms.R;

import java.util.ArrayList;
import java.util.Collections;

public class ShopViewAdapter extends ArrayAdapter {

    public ShopViewAdapter(@NonNull Context context, ArrayList<Shop> shops) {
        super(context, R.layout.list_layout, shops);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View convrtView = inflater.inflate(R.layout.list_layout, parent, false);

        Shop shop = (Shop) getItem(position);
        String S_name = shop.shopname;
        String S_customer = shop.shopcustomer;
        String S_address = shop.shopaddress;
        String S_contact = shop.shopcontact;

        // Lookup view for data population
        TextView vcustomername = (TextView) convrtView.findViewById(R.id.cn);
        TextView vshopname = (TextView) convrtView.findViewById(R.id.sn);
        TextView vshopaddress = (TextView) convrtView.findViewById(R.id.sa);
        TextView vshopcontact = (TextView) convrtView.findViewById(R.id.sc);

        // Populate the data into the template view using the data object
        vshopname.setText(S_name);
        vcustomername.setText(S_customer);
        vshopaddress.setText(S_address);
        vshopcontact.setText(S_contact);

        // Return the completed view to render on screen
        return convrtView;
    }
}
