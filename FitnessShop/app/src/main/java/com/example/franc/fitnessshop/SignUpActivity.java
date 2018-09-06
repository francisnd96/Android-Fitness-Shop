package com.example.franc.fitnessshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.franc.fitnessshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatButton createAccount;
    private EditText editName, editEmail, editPassword;
    private TextView linkLogin;
    private ProgressDialog pd;
    private FirebaseAuth auth;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editEmail = (EditText) findViewById(R.id.input_email);
        editName = (EditText) findViewById(R.id.input_name);
        editPassword = (EditText) findViewById(R.id.input_password);
        createAccount = (AppCompatButton) findViewById(R.id.btn_signup);
        linkLogin = (TextView) findViewById(R.id.link_login);
        pd = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();

        //Handle click
        createAccount.setOnClickListener(this);

        //Handle click
        linkLogin.setOnClickListener(this);

    }

    //Registering a user
    public void registerUser(){

        //User email
        String email = editEmail.getText().toString().trim();

        //User password
        String password = editPassword.getText().toString().trim();

        //User name
        name = editName.getText().toString().trim();

        //Email verification
        if(email.length() == 0){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        //Password verification
        if(password.length() == 0){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        //Name verification
        if(editName.length() == 0){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        //Loading dialog message
        pd.setMessage("Registering...");
        pd.show();

        //create the user account
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    userProfile();

                    //Send verification email
                    sendEmailVerification();
                    finish();

                    //Progress to next activity
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }else{
                    try {
                        throw task.getException();

                        //Validation error messages
                    } catch(FirebaseAuthWeakPasswordException e) {
                        Toast.makeText(SignUpActivity.this, "Registration failed. " + "Your password must be 6 characters long", Toast.LENGTH_LONG).show();
                    } catch(FirebaseAuthInvalidCredentialsException e) {
                        Toast.makeText(SignUpActivity.this, "Registration failed. " + "Please enter a valid email address", Toast.LENGTH_LONG).show();
                    } catch(FirebaseAuthUserCollisionException e) {
                        Toast.makeText(SignUpActivity.this, "Registration failed. " + "Email address already in use. Please enter a new one", Toast.LENGTH_LONG).show();
                    } catch(Exception e) {
                        Toast.makeText(SignUpActivity.this, "Registration failed.", Toast.LENGTH_LONG).show();
                    }finally {
                        pd.dismiss();
                    }
                }
            }
        });
    }

    private void userProfile(){

        //Request current user information
        FirebaseUser user = auth.getCurrentUser();

        //Set users display name
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name).build();
        user.updateProfile(profileUpdates);
    }

    //Send email verification
    private void sendEmailVerification(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SignUpActivity.this, "Check email for verification", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if(v == createAccount){
            registerUser();
        }else{
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
