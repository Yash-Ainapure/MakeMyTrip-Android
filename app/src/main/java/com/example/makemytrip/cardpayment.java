package com.example.makemytrip;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class cardpayment extends AppCompatActivity {
    // Declare these variables at the top of your class
    private LinearLayout processingAlertLayout;
    private LinearLayout paymentSuccessfulLayout;
    private MediaPlayer mediaPlayer;
    private Button doneButton;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardpayment);


        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
// Initialize the views in onCreate or wherever appropriate
        processingAlertLayout = findViewById(R.id.processing_alert);
        paymentSuccessfulLayout = findViewById(R.id.payment_successful);
        doneButton = findViewById(R.id.doneButton);

   mAuth = FirebaseAuth.getInstance();

// Inside the block where you initiate the payment processing
// For example, after the user clicks the "Proceed to Pay" button
// Show processing alert
        processingAlertLayout.setVisibility(View.VISIBLE);

// Delayed visibility change after 3-4 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Hide processing alert
                processingAlertLayout.setVisibility(View.GONE);

                // Show payment success layout
                paymentSuccessfulLayout.setVisibility(View.VISIBLE);
                mediaPlayer = MediaPlayer.create(cardpayment.this, R.raw.gpsound); // Replace with your music file in the "res/raw" folder
                mediaPlayer.start();





            }
        }, 8000); // Adjust the delay time as needed (in milliseconds)
        Intent intent = getIntent();
        int totalAmount = intent.getIntExtra("totalAmount", 0);
        HotelBookedInfo receivedBookedInfo = (HotelBookedInfo) intent.getSerializableExtra("hotelBookedInfo");
        Hotel hotel = (Hotel) intent.getParcelableExtra("hotel");
// Set an onClickListener for the "Done" button
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processBooking(intent, receivedBookedInfo, hotel);
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                // Redirect to home page (replace HomeActivity.class with your actual home activity)
                Intent intent = new Intent(cardpayment.this, ViewManageTrips.class);
                startActivity(intent);
                finish(); // Optional: finish the current activity if needed
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

        } else {
            Toast.makeText(cardpayment.this, "Error: No user logged in", Toast.LENGTH_SHORT).show();
        }
    }
}