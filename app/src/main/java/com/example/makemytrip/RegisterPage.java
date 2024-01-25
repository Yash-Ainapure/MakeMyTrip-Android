package com.example.makemytrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterPage extends AppCompatActivity {

    EditText email,password;
    TextView loginpage;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

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
        loginpage = findViewById(R.id.loginbtnreg);
        loginpage.setOnClickListener(view -> {
            Intent stm = new Intent(RegisterPage.this, MainActivity.class);
            startActivity(stm);
        });

        // Set Terms & Conditions text with colored links
        TextView tcTextView = findViewById(R.id.textView16);
        String tcText = getString(R.string.TC); // Assuming TC is defined in strings.xml

        // Create a SpannableStringBuilder to modify the text
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(tcText);

        // Find the start and end indices of the terms "Terms & Conditions" and "Privacy Policy"
        int startIndexTC = tcText.indexOf("Terms & Conditions");
        int endIndexTC = startIndexTC + "Terms & Conditions".length();
        int startIndexPP = tcText.indexOf("Privacy Policy");
        int endIndexPP = startIndexPP + "Privacy Policy".length();

        // Apply color to the specific parts of the text
        spannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.calbtn)),
                startIndexTC, endIndexTC, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.calbtn)),
                startIndexPP, endIndexPP, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD),
                startIndexTC, endIndexTC, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD),
                startIndexPP, endIndexPP, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Set the modified text to the TextView
        tcTextView.setText(spannableStringBuilder);
    }
}