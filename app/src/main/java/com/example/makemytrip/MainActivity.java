package com.example.makemytrip;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import com.google.firebase.FirebaseApp;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    Button loginbtn;
    EditText usernameEdt,passwordEdt;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialise firebase
        FirebaseApp.initializeApp(this);
        mAuth=FirebaseAuth.getInstance();

        usernameEdt=(EditText) findViewById(R.id.usernameedt);
        passwordEdt=(EditText) findViewById(R.id.passwordedt);
        loginbtn=(Button) findViewById(R.id.loginbtn);

        loginbtn.setOnClickListener(view -> {
            // Get the entered username and password
            String username = usernameEdt.getText().toString();
            String password = passwordEdt.getText().toString();


            //email: harsh@gmail.com
            //password : 123456


            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }
            // Sign in with email and password
            mAuth.signInWithEmailAndPassword(username,password)
                    .addOnCompleteListener(MainActivity.this, task -> {
                        if (task.isSuccessful()) {
                            // If valid, start the next activity
                            Intent stm = new Intent(MainActivity.this, home.class);
                            startActivity(stm);
                        } else {
                            Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}