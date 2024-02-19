package com.example.makemytrip;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterPage extends AppCompatActivity {

    EditText email,password,confirmpassword,firstName,lastName,mobileNo;
    TextView loginpage;
    Button register;
    DatabaseReference databaseReference;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        confirmpassword = findViewById(R.id.ConfirmPassword);
        firstName = findViewById(R.id.RegFirstName);
        lastName = findViewById(R.id.RegLastName);
        mobileNo = findViewById(R.id.RegPhoneNo);
        register = findViewById(R.id.registerbtn);

        register.setOnClickListener(v -> {
            String emailid = email.getText().toString();
            String pass = password.getText().toString();
            String confirmpass = confirmpassword.getText().toString();
            String fname = firstName.getText().toString();
            String lname = lastName.getText().toString();
            String mobno = mobileNo.getText().toString();
            String selectedCountry = "India";
            if(fname.isEmpty()){
                firstName.setError("Please enter first name");
                firstName.requestFocus();
            }
            else if(lname.isEmpty()){
                lastName.setError("Please enter last name");
                lastName.requestFocus();
            }
            else if(mobno.isEmpty()){
                mobileNo.setError("Please enter mobile number");
                mobileNo.requestFocus();
            }
            else if(emailid.isEmpty() && pass.isEmpty() && confirmpass.isEmpty() && fname.isEmpty() && lname.isEmpty() && mobno.isEmpty()){
                email.setError("Please enter email id");
                email.requestFocus();
                password.setError("Please enter password");
                password.requestFocus();
                confirmpassword.setError("Please enter confirm password");
                confirmpassword.requestFocus();
                firstName.setError("Please enter first name");
                firstName.requestFocus();
                lastName.setError("Please enter last name");
                lastName.requestFocus();
                mobileNo.setError("Please enter mobile number");
                mobileNo.requestFocus();
            }
            else if(!pass.equals(confirmpass)){
                Toast.makeText(RegisterPage.this,"Password and Confirm Password are not same",Toast.LENGTH_SHORT).show();
            }
            else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailid,pass).addOnCompleteListener(RegisterPage.this, task -> {
                    if(!task.isSuccessful()){
                        Toast.makeText(RegisterPage.this,"SignUp Unsuccessful, Please Try Again",Toast.LENGTH_SHORT).show();
                    }
                    else{

                        saveAdditionalUserInfo(task.getResult().getUser().getUid(),fname, lname, mobno,emailid, selectedCountry);

                        startActivity(new Intent(RegisterPage.this, MainActivity.class));
                        Toast.makeText(this, "registered successfully,now you can login with the credentials", Toast.LENGTH_SHORT).show();
                    }
                });

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


        // Button click effect
        register.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Button pressed, scale down
                    scaleButton(register, 0.95f);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    // Button released or touch canceled, scale back to the original size
                    scaleButton(register, 1f);
                    break;
            }
            return false;
        });
    }
    private void saveAdditionalUserInfo(String userId, String firstName, String lastName, String phoneNumber, String email,String selectedCountry) {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("userInfo");
        UserInfo userInfo = new UserInfo(email,firstName,lastName,phoneNumber,userId ,selectedCountry);
        databaseReference.setValue(userInfo);


//        DatabaseReference userRef = usersRef.child(userId);
//        userRef.child("firstName").setValue(firstName);
//        userRef.child("lastName").setValue(lastName);
//        userRef.child("phoneNumber").setValue(phoneNumber);
    }
    private void scaleButton(Button button, float scale) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(button, "scaleX", scale);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(button, "scaleY", scale);
        scaleDownX.setDuration(150);
        scaleDownY.setDuration(150);
        scaleDownX.start();
        scaleDownY.start();
    }
}