package com.example.user.project;

public class Product {

    public int ProductId;
    public String ProductName;
    public String Description;
    public double Price;
    public int Quantity;

    public Product() {
    }

    public Product(String productName, String description, double price, int quantity) {
        ProductName = productName;
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
}
