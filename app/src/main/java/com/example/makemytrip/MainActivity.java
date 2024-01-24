package com.example.makemytrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import com.google.firebase.FirebaseApp;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button loginbtn,registerbtn;
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
        registerbtn=(Button) findViewById(R.id.registerbtn);

        loginbtn.setOnClickListener(view -> {
            // Get the entered username and password
            String username = usernameEdt.getText().toString();
            String password = passwordEdt.getText().toString();


            //email: prajval@gmail.com
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
                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent stm = new Intent(MainActivity.this, home.class);
                            stm.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(stm);
                        } else {
                            Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        registerbtn.setOnClickListener(view -> {
            Intent stm = new Intent(MainActivity.this, RegisterPage.class);
            startActivity(stm);
        });

        TextView title = findViewById(R.id.textView6);
        EditText username = findViewById(R.id.usernameedt);
        EditText pass = findViewById(R.id.passwordedt);
        Button login = findViewById(R.id.loginbtn);
        Animation a1= AnimationUtils.loadAnimation(this,R.anim.fadein);
        Animation a2= AnimationUtils.loadAnimation(this,R.anim.lefttoright);

        title.startAnimation(a1);
        username.startAnimation(a2);
        pass.startAnimation(a2);
        login.startAnimation(a2);





    }
}