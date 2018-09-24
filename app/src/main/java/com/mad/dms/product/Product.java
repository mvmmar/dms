package com.mad.dms.product;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {

    public int ProductId;
    public String Category;
    public String ProductName;
    public String Description;
    public double Price;
    public int Quantity;

    public Product() {
    }

    public Product(String productName, String category,String description, double price, int quantity) {
        ProductName = productName;
        Category=category;
        Description = description;
        Price = price;
        Quantity = quantity;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    protected Product(Parcel in) {
        ProductId = in.readInt();
        Category = in.readString();
        ProductName = in.readString();
        Description = in.readString();
        Price = in.readDouble();
        Quantity = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ProductId);
        dest.writeString(Category);
        dest.writeString(ProductName);
        dest.writeString(Description);
        dest.writeDouble(Price);
        dest.writeInt(Quantity);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
