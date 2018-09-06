package com.example.franc.fitnessshop;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.franc.fitnessshop.Map.GetData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

//Implementing all these interfaces gives you the methods that you need to override
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{


    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    public static final int REQUEST_LOCATION_CODE = 99;
    int PROXIMITY_RADIUS = 10000;
    double latitude,longitude;
    private PendingResult<LocationSettingsResult> result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        bulidGoogleApiClient();

        //check whether location permissions are turned on
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkLocationPermission();
        }

        // Obtain the SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //this sets a callback and retrives the map when it has been loaded and is ready to be used, once this is triggered, the onMapReady method will run
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //the map fragment is stored in a GoogleMap variable called mMap
        mMap = googleMap;

        //Automatically generated boilerplate code that checks if permission has been granted to use location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            //This enables the my location feature, that button in the top right corner that shows a users location
            //Only works if permitted
            mMap.setMyLocationEnabled(true);
            mMap.animateCamera(CameraUpdateFactory.zoomBy(15));
        }
    }

    //Creating a GoogleApiClient
    //You need a GoogleApiClient to access any of the Google APIs in the Google Play services library
    protected synchronized void bulidGoogleApiClient() {

        //the builder helps us to specify which API's we want to use
        client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();

        //connecting that Client
        client.connect();
    }

    //from LocationListener
    //Called every time the phone's location changes
    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude() , location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.zoomBy(15));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        //Stop listening for location changes after the client has closed
        if(client != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
        }
    }

    //Handle click
    public void onClick(View v){

        //Create object array
        Object dataTransfer[] = new Object[2];

        //Create GetData object
        GetData getNearbyPlacesData = new GetData();

        switch(v.getId()){
            case R.id.gyms:

                //Clear the map of any markers
                mMap.clear();

                //Search term
                String gym = "gym";

                //Retrieve Google Map API url to search for 'gym' at this latitude/longitude
                String url = getUrl(latitude, longitude, gym);

                //Add to object array
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                //Place markers on map via this method
                getNearbyPlacesData.execute(dataTransfer);

                Toast.makeText(MapsActivity.this, "Showing Nearby Gyms", Toast.LENGTH_SHORT).show();
                break;
            case R.id.stores:
                mMap.clear();
                String stores = "health";
                url = getUrl(latitude, longitude, stores);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(MapsActivity.this, "Showing Nearby Heatlth Food Stores", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private String getUrl(double latitude , double longitude , String nearbyPlace){

        //StringBuilder to build GoogleMaps API url with correct app credentials
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyA1WJArUT9AoGhh-KTOCPZ6wZtCCt_aC-Y");

        return googlePlaceUrl.toString();
    }

    //method comes from the ConnectionCallbacks interface
    //runs when the GoogleApiClient has connected
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        //makes a request for Location
        locationRequest = new LocationRequest();

        //this will make requests for the users current location every 1000 milliseconds
        locationRequest.setInterval(100);

        //this gives location detail to about 100 meter accuracy, not too fine so that battery is not wasted
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        //Display permission dialog to use location service
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        result = LocationServices.SettingsApi.checkLocationSettings(client,builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()){
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(MapsActivity.this,REQUEST_LOCATION_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED){

            //this actually gets the location using the client and the request then passes it onto the listener which calls the onLocationChanged method
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }


    public boolean checkLocationPermission(){

        //Checks if app permission has been given in the manifest
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED ){

            //Checks if Permission Rationale should be given to the user
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                //Request app permissions
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }else{
                //Request app permissions
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            return false;
        } else
            return true;
    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    //From onConnectionFailedListener interface
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}