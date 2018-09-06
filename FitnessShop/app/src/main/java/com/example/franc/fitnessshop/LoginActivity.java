package com.example.franc.fitnessshop;

import android.app.ProgressDialog;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    EditText editEmail, editPassword;
    AppCompatButton btnLogin;
    TextView signUp, forgotPass;
    private ProgressDialog progDialog;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        //Check if user has already logged in
        if(auth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }

        editEmail = (EditText) findViewById(R.id.input_email);
        editPassword = (EditText) findViewById(R.id.input_password);
        btnLogin = (AppCompatButton) findViewById(R.id.btn_login);
        signUp = (TextView) findViewById(R.id.link_signup);
        forgotPass = (TextView) findViewById(R.id.link_forgotpass);



        progDialog = new ProgressDialog(this);

        btnLogin.setOnClickListener(this);
        signUp.setOnClickListener(this);
        forgotPass.setOnClickListener(this);
    }


    private void userLogin(){
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        //If email is empty
        if(email.length() == 0){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        //If password is empty
        if(password.length() == 0){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        //Progress dialog
        progDialog.setMessage("Logging in");
        progDialog.show();

        //Sign user in
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    progDialog.dismiss();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                }else{
                    progDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Login failed. " + "Please enter a valid login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    //Handle click
    @Override
    public void onClick(View v) {
        if(v == btnLogin) {
            userLogin();
        }
        if(v == signUp ) {
            finish();
            startActivity(new Intent(this, SignUpActivity.class));
        }
        if(v == forgotPass){
            finish();
            startActivity(new Intent(this, PassResetActivity.class));
        }

    }
}
