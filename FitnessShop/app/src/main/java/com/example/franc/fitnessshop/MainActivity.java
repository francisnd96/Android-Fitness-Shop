package com.example.franc.fitnessshop;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    AppCompatButton joinNow;
    TextView signIn, help;
    Uri uri;
    private VideoView mVideoView;
    int counter = 0;
    private int stopPosition;
    private FirebaseAuth auth;

    //Pause the background video
    @Override
    protected void onPause() {
        super.onPause();

    }


    //Resume the background video
    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.seekTo(stopPosition);
        mVideoView.start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();



        //Retrieve Background videos
        Field[] fields = R.raw.class.getFields();

        //Put background video names in a list
        final List<String> bgVids = new ArrayList<String>();
        for(Field field: fields){
            bgVids.add(field.getName());
        }

        mVideoView = (VideoView) findViewById(R.id.bgVideoView);

        //Retrieve the URI to the first video and play it
        uri = Uri.parse("android.resource://"+getPackageName()+"/raw/" + bgVids.get(0));
        mVideoView.setVideoURI(uri);

        //When the first video is complete, go on to the next one
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                counter++;
                mediaPlayer.setVolume(0,0);
                uri =  Uri.parse("android.resource://"+getPackageName()+"/raw/" + bgVids.get(counter));
                mVideoView.setVideoURI(uri);
                mVideoView.start();


                if(counter == bgVids.size()-1){
                    counter = 0;
                }else{

                }
            }
        });



        joinNow = (AppCompatButton) findViewById(R.id.btn_joinNow);
        signIn = (TextView) findViewById(R.id.link_signin);
        help = (TextView) findViewById(R.id.btn_help);



        joinNow.setOnClickListener(this);
        signIn.setOnClickListener(this);
        help.setOnClickListener(this);

    }

    //Help button
    public void help() {

        //Create dialog box
        AlertDialog.Builder builder;

        //Set required information
        builder = new AlertDialog.Builder(this);
        builder.setTitle("About Us");
        builder.setMessage("Shop is all about giving you the best brands at exclusive prices. We have experience " +
                "in selling fitness and lifestyle products online. Members enjoy access to all our sales. \n \nIf you still have questions please call " +
                "us on 07401021846 from Monday - Friday 6am - 7pm and on Saturday from 8am - 7pm");
        builder.setCancelable(true);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog a1 = builder.create();

        //Show dialog
        a1.show();
    }

    //Handle clicks
    @Override
    public void onClick(View v) {
        if(v == joinNow) {
            finish();
            startActivity(new Intent(this, SignUpActivity.class));
        }
        if(v == signIn) {
            stopPosition = mVideoView.getCurrentPosition();
            mVideoView.pause();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        if(v == help){
            help();
        }

    }

}
