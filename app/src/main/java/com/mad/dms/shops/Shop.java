package com.mad.dms.shops;


import android.os.Parcel;
import android.os.Parcelable;

public class Shop implements Parcelable {
    public int shopid;
    public String shopname;
    public String shopcustomer;
    public String shopaddress;
    public String shopcontact;

    public Shop() {
    }

    public Shop(String shopname, String shopcustomer, String shopaddress, String shopcontact) {
        this.shopname = shopname;
        this.shopcustomer = shopcustomer;
        this.shopaddress = shopaddress;
        this.shopcontact = shopcontact;
    }


    protected Shop(Parcel in) {
        shopid = in.readInt();
        shopname = in.readString();
        shopcustomer = in.readString();
        shopaddress = in.readString();
        shopcontact = in.readString();
    }

    public static final Creator<Shop> CREATOR = new Creator<Shop>() {
        @Override
        public Shop createFromParcel(Parcel in) {
            return new Shop(in);
        }

        @Override
        public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };

    public int getShopid() {
        return shopid;
    }

    public void setShopid(int shopid) {
        this.shopid = shopid;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getShopcustomer() {
        return shopcustomer;
    }

    public void setShopcustomer(String shopcustomer) {
        this.shopcustomer = shopcustomer;
    }

    public String getShopaddress() {
        return shopaddress;
    }

    public void setShopaddress(String shopaddress) {
        this.shopaddress = shopaddress;
    }

    public String getShopcontact() {
        return shopcontact;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(shopid);
        parcel.writeString(shopname);
        parcel.writeString(shopcustomer);
        parcel.writeString(shopaddress);
        parcel.writeString(shopcontact);
    }
}
