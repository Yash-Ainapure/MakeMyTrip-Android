package com.example.makemytrip;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    //lhatog Vanabies
   AlertDialog.Builder builderDialog;
   AlertDialog dialog;

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
            String enteredPassword = upiPasswordEditText.getText().toString();

            if ("123456".equals(enteredPassword)) {
                // Payment successful, show success dialog
                showAlertDialog(R.layout.payment_successful, intent ,receivedBookedInfo ,hotel);
            }
             else {
                builderDialog = new AlertDialog.Builder(DummyUPIPayment.this);
                View layoutView = getLayoutInflater().inflate(R.layout.payment_failed, null);
                Button okButton = layoutView.findViewById(R.id.ok);
                builderDialog.setView(layoutView);
                dialog = builderDialog.create();
                dialog.show();
                okButton.setOnClickListener(view1 -> {
                    dialog.dismiss();
                    passwordBuilder.setLength(0);
                    updatePasswordEditText();
                });

            }

        });
    }

    private void showAlertDialog(int layout, Intent intent ,HotelBookedInfo receivedBookedInfo ,Hotel hotel) {
        builderDialog = new AlertDialog.Builder(DummyUPIPayment.this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        Button okButton = layoutView.findViewById(R.id.ok);
        builderDialog.setView(layoutView);
        dialog = builderDialog.create();
        dialog.show();
        okButton.setOnClickListener(view -> {
            dialog.dismiss();
            if (intent != null) {
                // Process booking and navigate to home
                processBooking(intent, receivedBookedInfo, hotel);
            } else {
                // Optionally handle other cases or just close the activity

            }
        });
    }

    private void processBooking(Intent intent, HotelBookedInfo receivedBookedInfo, Hotel hotel) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String loggedUserEmail = user.getUid();
            String bookingType = intent.getStringExtra("bookingType");
            if (bookingType != null) {
                if (bookingType.equals("hotel")) {
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(loggedUserEmail).child("bookedHotels");
                    String userId = databaseReference.push().getKey();
                    HotelBookedInfo hotelBookedInfo = new HotelBookedInfo(hotel.getName(), hotel.getAddress(), receivedBookedInfo.getNumberOfRooms()
                            , receivedBookedInfo.getStartingDate(), receivedBookedInfo.getEndingDate(), hotel.getImageUrl());
                    databaseReference.child(userId).setValue(hotelBookedInfo);
                    Toast.makeText(this, "Hotel booked successfully", Toast.LENGTH_LONG).show();
                } else if (bookingType.equals("flight")) {
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(loggedUserEmail).child("bookedFlights");
                    String userId = databaseReference.push().getKey();
                    Flight flight = intent.getParcelableExtra("flightBooking");
                    databaseReference.child(userId).setValue(flight);
                    Toast.makeText(this, "Flight booked successfully", Toast.LENGTH_LONG).show();
                }
            }
            Intent homeIntent = new Intent(DummyUPIPayment.this, home.class);
            startActivity(homeIntent);
            finish();
        } else {
            Toast.makeText(DummyUPIPayment.this, "Error: No user logged in", Toast.LENGTH_SHORT).show();
        }
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