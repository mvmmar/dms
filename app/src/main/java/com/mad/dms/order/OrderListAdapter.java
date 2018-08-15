package com.mad.dms.order;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mad.dms.R;

public class OrderListAdapter extends ArrayAdapter<Order> {
    OrderListAdapter(@NonNull Context context, @NonNull Order[] orders) {
        super(context, R.layout.order_row, orders);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        // get the row
        View view = inflater.inflate(R.layout.order_row, parent, false);

        Order order = getItem(position);

        // check if order is null
        if (order == null) {
            order = new Order();
        }

        String name = order.name;
        String stat = order.status;
        String date = order.date;

        int statusColor, statusIcon;
        switch (stat) {
            case "Accepted": {
                statusColor = R.color.orderStatusAccepted;
                statusIcon = R.drawable.ic_status_accepted;
                break;
            }
            case "Pending": {
                statusColor = R.color.orderStatusPending;
                statusIcon = R.drawable.ic_status_pending;
                date = "--/--/--";
                break;
            }
            default: {
                stat = "Denied";
                statusColor = R.color.orderStatusDenied;
                statusIcon = R.drawable.ic_status_denied;
            }
        }

        TextView orderName = view.findViewById(R.id.orderName);
        TextView orderStat = view.findViewById(R.id.orderStatus);
        TextView orderDate = view.findViewById(R.id.orderDate);
        ImageView orderStatusIcon = view.findViewById(R.id.orderStatusIcon);
        // ImageView orderIcon = view.findViewById(R.id.orderIcon);

        // set order details
        orderName.setText(name);
        orderStat.setText(stat);
        orderDate.setText(date);

        // set status icon
        orderStatusIcon.setImageResource(statusIcon);
        // change icon colors
        statusColor = ContextCompat.getColor(getContext(), statusColor);
        orderStat.setTextColor(statusColor);
        orderStatusIcon.setColorFilter(statusColor, PorterDuff.Mode.MULTIPLY);

        // orderIcon.setImageResource(orderIconImg);
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        //     orderIcon.setBackground(new ColorDrawable(statusColor));
        // }

        return view;
    }
}
