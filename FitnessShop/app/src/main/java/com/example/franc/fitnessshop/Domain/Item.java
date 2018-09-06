package com.example.franc.fitnessshop.Domain;

/**
 * Created by franc on 24/11/2017.
 */

//Domain class to store Items
public class Item {

    //Item details
    private String Name;
    private String Description;
    private String Image;
    private String Discount;
    private String CategoryId;
    private String Price;
    private String Stock;
    private String Rating;
    private String Count;

    public Item() {
    }

    public Item(String name, String description, String image, String discount, String categoryId, String price, String stock, String rating, String count) {
        this.Name = name;
        this.Description = description;
        this.Image = image;
        this.Discount = discount;
        this.CategoryId = categoryId;
        this.Price = price;
        this.Stock = stock;
        this.Rating = rating;
        this.Count = count;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getStock() {
        return Stock;
    }

    public void setStock(String stock) {
        Stock = stock;
    }
}
