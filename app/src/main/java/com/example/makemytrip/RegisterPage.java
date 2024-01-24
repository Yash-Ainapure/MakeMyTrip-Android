package com.example.makemytrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterPage extends AppCompatActivity {

    EditText email,password;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        register = findViewById(R.id.registerbtn);

        register.setOnClickListener(v -> {
            String emailid = email.getText().toString();
            String pass = password.getText().toString();
            if(emailid.isEmpty()){
                email.setError("Please enter email id");
                email.requestFocus();
            }
            else if(pass.isEmpty()){
                password.setError("Please enter password");
                password.requestFocus();
            }
            else if(emailid.isEmpty() && pass.isEmpty()){
                email.setError("Please enter email id");
                email.requestFocus();
                password.setError("Please enter password");
                password.requestFocus();
            }
            else if(!(emailid.isEmpty() && pass.isEmpty())){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailid,pass).addOnCompleteListener(RegisterPage.this, task -> {
                    if(!task.isSuccessful()){
                        Toast.makeText(RegisterPage.this,"SignUp Unsuccessful, Please Try Again",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        startActivity(new Intent(RegisterPage.this, MainActivity.class));
                        Toast.makeText(this, "registered successfully,now you can login with the credentials", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                Toast.makeText(RegisterPage.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}