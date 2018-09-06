package com.example.franc.fitnessshop;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    AppCompatButton joinNow;
    TextView signIn, help;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        joinNow = (AppCompatButton) findViewById(R.id.btn_joinNow);
        signIn = (TextView) findViewById(R.id.link_signin);
        help = (TextView) findViewById(R.id.btn_help);

        joinNow.setOnClickListener(this);
        signIn.setOnClickListener(this);
        help.setOnClickListener(this);
    }

    public void help() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle("About Us");
        builder.setMessage("Shop is all about giving you the best brands at exclusive prices. We have experience " +
                "in selling fitness and lifestyle products online. Members enjoy access to all our sales. \n \nIf you still have questions please call " +
                "us onn 07401021846 from Monday - Friday 6am - 7pm and on Saturday from 8am - 7pm");
        builder.setCancelable(true);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog a1 = builder.create();
        a1.show();
    }

    @Override
    public void onClick(View v) {
        if(v == joinNow) {
            finish();
            startActivity(new Intent(this, SignUpActivity.class));
        }
        if(v == signIn) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        if(v == help){
            help();
        }
    }
}
