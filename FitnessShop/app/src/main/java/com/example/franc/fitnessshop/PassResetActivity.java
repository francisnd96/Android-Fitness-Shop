package com.example.franc.fitnessshop;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PassResetActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editEmail;
    AppCompatButton cont;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_reset);

        editEmail = (EditText) findViewById(R.id.input_email);
        cont = (AppCompatButton)findViewById(R.id.btn_continue);

        auth = FirebaseAuth.getInstance();

        cont.setOnClickListener(this);
    }

    private void sendPassReset() {
        auth.sendPasswordResetEmail(editEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(PassResetActivity.this, "Reset email sent",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(PassResetActivity.this, "That email does not exist. Please sign up",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == cont){
            sendPassReset();
        }
    }


}
