package com.example.franc.fitnessshop.Map;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by franc on 01/02/2018.
 */

public class GetData extends AsyncTask<Object,String,String> {

    String data;
    GoogleMap map;
    String url;

    //this code is called when you call execute in MapsActivty
    @Override
    protected String doInBackground(Object[] objects) {
        map = (GoogleMap)objects[0];
        url = (String)objects[1];

        // Create an object of GetURL
        GetURL getURL = new GetURL();

        try {
            //Gets the location data by sending the url request via HttpConnection
            data = getURL.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    //When doInBackground is finished
    @Override
    protected void onPostExecute(String s) {

        //Create a List of hashmaps to store location data
        List<HashMap<String, String>> nearbyPlaceList;

        //Create an object of GetParser
        GetParser parser = new GetParser();

        //This will parse the location data retrieved in the doInBackground method
        nearbyPlaceList = parser.parse(s);

        //Call this method
        showNearbyPlaces(nearbyPlaceList);
    }

    //Method called in onPostExecute to place markers on map
    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList){

        //iterate through parsed Location data and place a Google marker on each latitude/longitude given
        for(int i = 0; i < nearbyPlaceList.size(); i++){
            MarkerOptions markerOptions = new MarkerOptions();

            //Retrieve first item in parsed list and place in hashmap
            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);

            //Retrieve location name
            String placeName = googlePlace.get("place_name");

            //Retrieve location vicinity
            String vicinity = googlePlace.get("vicinity");

            //Retrieve location latitude
            double lat = Double.parseDouble( googlePlace.get("lat"));

            //Retrieve location longitude
            double lng = Double.parseDouble( googlePlace.get("lng"));

            //Put a hue blue marker at the latitude/longitude of the location
            LatLng latLng = new LatLng( lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : "+ vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            map.addMarker(markerOptions);

            //Animate camera to that marker
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.zoomTo(10));
        }
    }
}
