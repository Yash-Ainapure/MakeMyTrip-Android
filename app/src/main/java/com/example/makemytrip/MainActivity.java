package com.example.makemytrip;

import androidx.appcompat.app.AppCompatActivity;

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
                String userName=usernameEdt.getText().toString();
                String pass=passwordEdt.getText().toString();
                if(userName.length()<6 || pass.length()<6){
                    Toast.makeText(MainActivity.this, "username and password length must be greater than 6", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "username : "+userName+" password : "+pass, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}