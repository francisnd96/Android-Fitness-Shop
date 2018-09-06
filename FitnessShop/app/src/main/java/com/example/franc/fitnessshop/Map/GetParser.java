package com.example.franc.fitnessshop.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by franc on 01/02/2018.
 */

//Parser to parse the JSON data retrieved from the Google location service
public class GetParser {

    //Retrieves JSON object and convert to hashmap
    private HashMap<String,String> getLocation(JSONObject jsonData){
        HashMap<String,String> myMap = new HashMap<>();
        String name = "";
        String area = "";
        String lat = "";
        String lon = "";
        String ref = "";

        try {
            if (!jsonData.isNull("name")) {
                //Retrieve location name
                name = jsonData.getString("name");
            }
            if (!jsonData.isNull("vicinity")) {
                //Retrieve location vicinity
                area = jsonData.getString("vicinity");
            }

            //Retrieve location latitude
            lat = jsonData.getJSONObject("geometry").getJSONObject("location").getString("lat");

            //Retrieve location longitude
            lon = jsonData.getJSONObject("geometry").getJSONObject("location").getString("lng");

            //Retrieve location reference
            ref = jsonData.getString("reference");

            myMap.put("place_name", name);
            myMap.put("vicinity", area);
            myMap.put("lat", lat);
            myMap.put("lng", lon);
            myMap.put("reference", ref);


        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        //return hashmap
        return myMap;
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray){

        //Retrieve length of JSON object containing the results
        int count = jsonArray.length();

        //List of hashmaps to store places
        List<HashMap<String, String>> placelist = new ArrayList<>();

        HashMap<String, String> placeMap = null;

        //Iterate through JSON array to add places to placelist
        for(int i = 0; i<count;i++){
            try {

                //calls getLocation method
                placeMap = getLocation((JSONObject) jsonArray.get(i));
                placelist.add(placeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placelist;
    }

    //Method called in GetData to parse JSON Location data
    public List<HashMap<String, String>> parse(String jsonData){

        //Used to store a section of JSON data from a JSON object
        JSONArray jsonArray = null;

        JSONObject jsonObject;


        try {
            //Convert String to a JSON object
            jsonObject = new JSONObject(jsonData);

            //Retrieve the part of the JSON data that contains the result
            jsonArray = jsonObject.getJSONArray("results");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Return that JSON data to the getPlaces method
        return getPlaces(jsonArray);
    }
}
