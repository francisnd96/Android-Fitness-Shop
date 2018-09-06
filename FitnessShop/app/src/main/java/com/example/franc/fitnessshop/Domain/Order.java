package com.example.franc.fitnessshop.Domain;

/**
 * Created by franc on 22/02/2018.
 */

public class Order {

    private String ProductID;
    private String Quantity;
    private String Price;
    private String Discount;
    private String ProductName;
    private String User;


    public Order() {
    }

    public Order(String productID, String quantity, String price, String discount, String productName, String user) {
        ProductID = productID;
        Price = price;
        Quantity = quantity;
        ProductName = productName;
        Discount = discount;
        User = user;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
