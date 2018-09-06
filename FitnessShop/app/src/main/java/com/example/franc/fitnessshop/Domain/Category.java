package com.example.franc.fitnessshop.Domain;

/**
 * Created by franc on 11/11/2017.
 */

//Domain class to maintain categories
public class Category {

    //Category name
    private String Name;

    //Category image URL
    private String Image;

    //Empty Constructor (necessary for FirebaseRecyclerAdapter to bind db info)
    public Category() {
    }

    //Constructor
    public Category(String name, String image) {
        this.Image = image;
        this.Name = name;
    }

    //Getters and Setters
    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }
}
