package com.example.makemytrip;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ViewBookedFlights extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booked_flights);
        getSupportActionBar().setTitle("View Booked Flights");
    }
}