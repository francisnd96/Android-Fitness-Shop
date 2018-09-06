package com.example.franc.fitnessshop.Map;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by franc on 01/02/2018.
 */

//Class to send request to GooglePlaces API to receive JSON location data as a string
public class GetURL {

    public String readUrl(String myUrl) throws IOException
    {
        String data = "";

        //Used to input information
        InputStream iStream = null;

        //Creates an internet connection
        HttpURLConnection conn = null;

        try {

            //Create a url object
            URL url = new URL(myUrl);

            //Send HTTP request
            conn=(HttpURLConnection) url.openConnection();
            conn.connect();

            //Retrieve response into stringbuffer
            iStream = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();

            String line = "";

            while((line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();
            br.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            iStream.close();
            conn.disconnect();
        }


        return data;
    }
}
