package com.example.makemytrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DummyUPIPayment extends AppCompatActivity {

    TextView totalAmountTextView;
    Button payButton;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_upipayment);

        totalAmountTextView = findViewById(R.id.totalAmount);
        payButton = findViewById(R.id.pay);

        mAuth = FirebaseAuth.getInstance();


        Intent intent = getIntent();
        int totalAmount = intent.getIntExtra("totalAmount", 0);
        HotelBookedInfo receivedBookedInfo = (HotelBookedInfo) intent.getSerializableExtra("hotelBookedInfo");
        Hotel hotel = (Hotel) intent.getParcelableExtra("hotel");

        Toast.makeText(this, "Total amount : "+totalAmount, Toast.LENGTH_SHORT).show();
        totalAmountTextView.setText("Total amount : "+totalAmount);

        payButton.setOnClickListener(view -> {
            Toast.makeText(this, "Payment done", Toast.LENGTH_SHORT).show();

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

                }else{
                    Toast.makeText(DummyUPIPayment.this, "Error: No user logged in", Toast.LENGTH_SHORT).show();
                }
        });
    }
}