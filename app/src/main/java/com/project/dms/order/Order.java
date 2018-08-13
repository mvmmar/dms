package com.project.dms.order;

import android.os.Parcel;
import android.os.Parcelable;

// Use Parcelable to make it possible to pass objects between views
// Use this site to generate Parcelables from the class http://www.parcelabler.com/
public class Order implements Parcelable {
    public String name;
    public String status;
    public String date;

    Order() {
        this.name = "Order Name";
        this.status = "Status";
        this.date = "00/00/00";
    }

    Order(String name, String status, String date) {
        this.name = name;
        this.status = status;
        this.date = date;
    }

    protected Order(Parcel in) {
        name = in.readString();
        status = in.readString();
        date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(status);
        dest.writeString(date);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
