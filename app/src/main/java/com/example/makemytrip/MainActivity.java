package com.example.makemytrip;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button loginbtn;
    TextView registerbtn;
    EditText usernameEdt, passwordEdt;
    FirebaseAuth mAuth;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        usernameEdt = findViewById(R.id.usernameedt);
        passwordEdt = findViewById(R.id.passwordedt);
        loginbtn = findViewById(R.id.loginbtn);
        registerbtn = findViewById(R.id.registerbtnlogin);

        // Initial state: Disable login button
        loginbtn.setEnabled(false);

        // TextChangedListener to enable/disable the login button based on input
        usernameEdt.addTextChangedListener(new InputTextWatcher());
        passwordEdt.addTextChangedListener(new InputTextWatcher());

        loginbtn.setOnClickListener(view -> {
            String username = usernameEdt.getText().toString();
            String password = passwordEdt.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(MainActivity.this, task -> {
                        if (task.isSuccessful()) {
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
        Animation a1 = AnimationUtils.loadAnimation(this, R.anim.fadein);
        Animation a2 = AnimationUtils.loadAnimation(this, R.anim.lefttoright);

        title.startAnimation(a1);
        username.startAnimation(a2);
        pass.startAnimation(a2);
        login.startAnimation(a2);

        // Button click effect
        loginbtn.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Button pressed, scale down
                    scaleButton(loginbtn, 0.95f);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    // Button released or touch canceled, scale back to the original size
                    scaleButton(loginbtn, 1f);
                    break;
            }
            return false;
        });
    }

    private void scaleButton(Button button, float scale) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(button, "scaleX", scale);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(button, "scaleY", scale);
        scaleDownX.setDuration(150);
        scaleDownY.setDuration(150);
        scaleDownX.start();
        scaleDownY.start();
    }

    // TextWatcher to enable/disable the login button based on input
    private class InputTextWatcher implements android.text.TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // Enable the login button only if both username and password are not empty
            loginbtn.setEnabled(!usernameEdt.getText().toString().isEmpty() && !passwordEdt.getText().toString().isEmpty());
        }
    }
}
