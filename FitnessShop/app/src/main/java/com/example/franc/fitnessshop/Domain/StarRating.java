package com.example.franc.fitnessshop.Domain;

/**
 * Created by franc on 06/04/2018.
 */

public class StarRating {
    String userId;
    String id;
    String rating;
    String comment;
    String displayName;

    public StarRating(String userId, String id, String rating, String comment, String displayName) {
        this.userId = userId;
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.displayName = displayName;
    }

    public StarRating() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}


