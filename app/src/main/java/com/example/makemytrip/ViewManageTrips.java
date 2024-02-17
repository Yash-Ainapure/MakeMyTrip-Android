package com.example.makemytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class ViewManageTrips extends AppCompatActivity {
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // Handle the back button
            case android.R.id.home:
                onBackPressed(); // This will call the default back button behavior
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_manage_trips);
        getSupportActionBar().setTitle("View & Manage Trips");
        LinearLayout viewBookedHotels = findViewById(R.id.linearLayout6);
        LinearLayout viewBookedFlights = findViewById(R.id.linearLayout7);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Set your desired icon for the navigation drawer toggle
        }
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