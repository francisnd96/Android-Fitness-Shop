package com.example.franc.fitnessshop.Databases;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.franc.fitnessshop.Domain.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by franc on 22/02/2018.
 */

//SQLiteAssetHelper helps us to connect our database file to our Android project
public class Database extends SQLiteAssetHelper {

    //The name of the database we will access
    private static final String DB_NAME = "fitnessShop2.db";

    //Version of the database
    private static final int DB_VER = 1;

    //Database class constructor
    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    //Method to retrieve Shopping Trolley
    public List<Order> getShoppingCart(String userId){

        //Retrieve Database
        SQLiteDatabase db = getReadableDatabase();

        //Used to build SQL queries
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        //A string array with the names of the fields we will be accessing in the database
        String[] select = {"ProductID","Quantity","Price","Discount","ProductName","User"};

        //The name of the SQL table
        String table = "ShoppingCart";

        //Set the query to operate on this table
        qb.setTables(table);

        //Cursor used to iterate through results of query
        //This query will select all the items detailed in the string array for a particular user
        Cursor cursor = qb.query(db,select,"User=?",new String[]{userId},null,null,null);

        //Create list to house each individual item the user has put in his cart
        final List<Order> result = new ArrayList<>();

        //Iterate through query results and create new Order objects with the results
        if(cursor.moveToFirst()){
            do{
                result.add(new Order(cursor.getString(cursor.getColumnIndex("ProductID")),
                        cursor.getString(cursor.getColumnIndex("Quantity")),
                        cursor.getString(cursor.getColumnIndex("Price")),
                        cursor.getString(cursor.getColumnIndex("Discount")),
                        cursor.getString(cursor.getColumnIndex("ProductName")),
                        cursor.getString(cursor.getColumnIndex("User"))));
            }while(cursor.moveToNext());
        }

        //Return list
        return result;

    }

    //Method used to add items to cart
    public void putInCart(Order order){

        //Retrieve readable database
        SQLiteDatabase db = getReadableDatabase();

        //SQL Query to insert item into database
        String query = String.format("Insert or replace into ShoppingCart(ProductID,Quantity,Price,Discount,ProductName,User) values " +
                "('%s','%s','%s','%s','%s','%s');",order.getProductID(),order.getQuantity(),order.getPrice(),order.getDiscount(),order.getProductName(),order.getUser());

        //Execute the query
        db.execSQL(query);
    }

    //Method used to empty cart
    public void emptyCart(String user){

        //Retrieve readable database
        SQLiteDatabase db = getReadableDatabase();

        //SQL query to remove from database
        String query = String.format("delete from ShoppingCart where User = '%s'",user);

        //Execute the query
        db.execSQL(query);
    }
}
