package com.example.makemytrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

public class ViewManageTrips extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_manage_trips);
        getSupportActionBar().setTitle("View & Manage Trips");
        LinearLayout viewBookedHotels = findViewById(R.id.linearLayout6);
        LinearLayout viewBookedFlights = findViewById(R.id.linearLayout7);

        viewBookedHotels.setOnClickListener(v -> {
            // Open the ViewBookedHotels activity
            Intent intent = new Intent(ViewManageTrips.this, ViewBookedHotels.class);
            startActivity(intent);
        });

        viewBookedFlights.setOnClickListener(v -> {
            // Open the ViewBookedFlights activity
            Intent intent = new Intent(ViewManageTrips.this, ViewBookedFlights.class);
            startActivity(intent);
        });
    }
}