package com.example.franc.fitnessshop;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.smooch.core.Settings;
import io.smooch.core.Smooch;
import io.smooch.core.SmoochCallback;
import io.smooch.ui.ConversationActivity;

public class ChatActivity extends Application {

    //Smooch.io App token to access online services
    String APP_TOKEN = "5a7e0ed274efab0021a9b980";


    @Override
    public void onCreate() {
        super.onCreate();


        //Initiate conversation actviity
        Smooch.init(this, new Settings(APP_TOKEN), new SmoochCallback() {
            @Override
            public void run(Response response) {

            }
        });


    }
}
