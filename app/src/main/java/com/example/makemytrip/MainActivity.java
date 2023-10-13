package com.example.makemytrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    Button loginbtn;
    EditText usernameEdt,passwordEdt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameEdt=(EditText) findViewById(R.id.usernameedt);
        passwordEdt=(EditText) findViewById(R.id.passwordedt);
        loginbtn=(Button) findViewById(R.id.loginbtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the entered username and password
                String enteredUsername = usernameEdt.getText().toString();
                String enteredPassword = passwordEdt.getText().toString();

                // Check if the entered credentials are valid
                if (enteredUsername.equals("harsh") && enteredPassword.equals("123456")) {
                    // If valid, start the next activity
                    Intent stm = new Intent(MainActivity.this, home.class);
                    startActivity(stm);
                } else {
                    // If invalid, show a message or handle it as needed
                    // For simplicity, you can display a toast message
                    // indicating that the login failed
                    // You may want to use a more secure authentication method
                    // in a real-world scenario
                    Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}