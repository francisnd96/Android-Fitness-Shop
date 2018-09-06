package com.example.franc.fitnessshop.Domain;

/**
 * Created by franc on 24/11/2017.
 */

public class Item {
    private String Name;
    private String Description;
    private String Image;
    private String Discount;
    private String CategoryId;
    private String Price;

    public Item() {
    }

    public Item(String name, String description, String image, String discount, String categoryId, String price) {
        this.Name = name;
        this.Description = description;
        this.Image = image;
        this.Discount = discount;
        this.CategoryId = categoryId;
        this.Price = price;
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
}
