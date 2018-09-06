package com.example.franc.fitnessshop.Domain;

import java.util.List;

/**
 * Created by franc on 05/03/2018.
 */

//Domain class to maintain delivery details
public class Delivery {

    //Date and time of the delivery
    private String name;

    //Address to deliver to
    private String address1stLine;

    //Postcode to deliver to
    private String postcode;

    //Total price of delivery
    private String total;

    //Items in the delivery
    private List<Order> order;

    //Empty Constructor
    public Delivery() {
    }

    //Constructor
    public Delivery(String name,String address1stLine, String postcode, String total, List<Order> order) {
        this.name = name;
        this.address1stLine = address1stLine;
        this.postcode = postcode;
        this.total = total;
        this.order = order;
    }

    //Getters and Setters
    public String getAddress1stLine() {
        return address1stLine;
    }

    public void setAddress1stLine(String address1stLine) {
        this.address1stLine = address1stLine;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getOrder() {
        return order;
    }

    public void setOrder(List<Order> order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
