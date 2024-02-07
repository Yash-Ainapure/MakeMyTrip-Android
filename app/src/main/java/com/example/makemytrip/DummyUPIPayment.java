package com.example.makemytrip;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DummyUPIPayment extends AppCompatActivity {

    TextView totalAmountTextView;
    Button payButton,One,Two,Three,Four,Five,Six,Seven,Eight,Nine,Zero;
    ImageButton Clrbtn;
    EditText upiPasswordEditText;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    private final StringBuilder passwordBuilder = new StringBuilder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_upipayment);

        totalAmountTextView = findViewById(R.id.totalAmount);
        upiPasswordEditText = findViewById(R.id.pinpass);
        payButton = findViewById(R.id.pay);
        One = findViewById(R.id.one);
        Two = findViewById(R.id.two);
        Three = findViewById(R.id.three);
        Four = findViewById(R.id.four);
        Five = findViewById(R.id.five);
        Six = findViewById(R.id.six);
        Seven = findViewById(R.id.seven);
        Eight = findViewById(R.id.eight);
        Nine = findViewById(R.id.nine);
        Zero = findViewById(R.id.zero);
        Clrbtn = findViewById(R.id.clrbtn);
        // Set click listeners for numeric buttons
        One.setOnClickListener(v -> appendValueToPassword("1"));
        Two.setOnClickListener(v -> appendValueToPassword("2"));
        Three.setOnClickListener(v -> appendValueToPassword("3"));
        Four.setOnClickListener(v -> appendValueToPassword("4"));
        Five.setOnClickListener(v -> appendValueToPassword("5"));
        Six.setOnClickListener(v -> appendValueToPassword("6"));
        Seven.setOnClickListener(v -> appendValueToPassword("7"));
        Eight.setOnClickListener(v -> appendValueToPassword("8"));
        Nine.setOnClickListener(v -> appendValueToPassword("9"));
        Zero.setOnClickListener(v -> appendValueToPassword("0"));
        Clrbtn.setOnClickListener(v -> deleteLastCharacter());

        // Set click listener for pay button
        payButton.setOnClickListener(v -> {
            // Implement your payment logic here using the password
            String password = upiPasswordEditText.getText().toString();
            // Do something with the password, e.g., initiate payment
        });
        mAuth = FirebaseAuth.getInstance();


        Intent intent = getIntent();
        int totalAmount = intent.getIntExtra("totalAmount", 0);
        HotelBookedInfo receivedBookedInfo = (HotelBookedInfo) intent.getSerializableExtra("hotelBookedInfo");
        Hotel hotel = (Hotel) intent.getParcelableExtra("hotel");

        Toast.makeText(this, "Total amount : "+totalAmount, Toast.LENGTH_SHORT).show();
        totalAmountTextView.setText("₹ "+totalAmount);

        payButton.setOnClickListener(view -> {


            FirebaseUser user = mAuth.getCurrentUser();
                if(user!=null){
                    String LoggedUserEmail = user.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(LoggedUserEmail).child("bookedHotels");
                    String UserId=databaseReference.push().getKey();
                    //Date selectedDate = new Date(selectedDates);
//                    HotelBookedInfo hotelBookedInfo = new HotelBookedInfo(textViewHotelName.getText().toString(),textViewHotelAddress.getText().toString(),numberPickerRooms.getValue()
//                            ,buttonDatePicker.getText().toString(),buttonDatePicker2.getText().toString(),ImageUrl);
                    HotelBookedInfo hotelBookedInfo = new HotelBookedInfo(hotel.getName(),hotel.getAddress(),receivedBookedInfo.getNumberOfRooms()
                            ,receivedBookedInfo.getStartingDate(),receivedBookedInfo.getEndingDate(),hotel.getImageUrl());
                    databaseReference.child(UserId).setValue(hotelBookedInfo);
                    // Inside your DummyUPIPayment activity
                    String enteredPassword = upiPasswordEditText.getText().toString();

                    // Check if the entered password is correct
                    if ("123456".equals(enteredPassword)) {
                        // Password is correct, proceed with the intent
                        Intent intent1 = new Intent(DummyUPIPayment.this, ViewManageTrips.class);
                        startActivity(intent1);
                        finish(); // Finish the current activity
                    } else {
                        // Incorrect password, show a toast
                        Toast.makeText(DummyUPIPayment.this, "Incorrect PIN. Please try again.", Toast.LENGTH_SHORT).show();
                        // Optionally, you can clear the entered password
                        passwordBuilder.setLength(0);
                        updatePasswordEditText();
                    }

                }else{
                    Toast.makeText(DummyUPIPayment.this, "Error: No user logged in", Toast.LENGTH_SHORT).show();
                }
        });
    }

    private void appendValueToPassword(String value) {
        if (passwordBuilder.length() < 6) {
            passwordBuilder.append(value);
            updatePasswordEditText();
        }
    }

    private void deleteLastCharacter() {
        if (passwordBuilder.length() > 0) {
            passwordBuilder.deleteCharAt(passwordBuilder.length() - 1);
            updatePasswordEditText();
        }
    }

    private void updatePasswordEditText() {
        upiPasswordEditText.setText(passwordBuilder.toString());
    }
}