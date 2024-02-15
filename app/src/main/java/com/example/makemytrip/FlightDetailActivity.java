package com.example.makemytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class FlightDetailActivity extends AppCompatActivity {

    private TextView fltNameTextView;
    private TextView fltDeptTextView;
    private TextView fltDestTextView;
    private TextView deptTimeTextView;
    private TextView destTimeTextView;
    private TextView airportTextView;
    private Button bookButton;
    Flight flight;
    @Override
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
        setContentView(R.layout.activity_flight_detail);

        fltNameTextView = findViewById(R.id.fltName);
        fltDeptTextView = findViewById(R.id.fltdept);
        fltDestTextView = findViewById(R.id.fltdest);
        deptTimeTextView = findViewById(R.id.deptTime);
        destTimeTextView = findViewById(R.id.destTime);
        airportTextView = findViewById(R.id.airport);
        bookButton = findViewById(R.id.bookFlight);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Set your desired icon for the navigation drawer toggle
        }
        getSupportActionBar().setTitle("Flights Details");
        Intent intent = getIntent();
        if (intent != null) {
            flight = intent.getParcelableExtra("flight");

            fltNameTextView.setText("Flight Name: " + flight.getFlightName());
            fltDeptTextView.setText("Departure City: " + flight.getDepartureCity());
            fltDestTextView.setText("Destination City: " + flight.getDestinationCity());
            deptTimeTextView.setText("Departure : " + flight.getDepartureTime());
            destTimeTextView.setText("Destination : " + flight.getDestinationTime());
            airportTextView.setText("Airport Name: " + flight.getAirline());
        }

        bookButton.setOnClickListener(v->{
            Intent intent1 = new Intent(FlightDetailActivity.this, DummyUPIPayment.class);
            intent1.putExtra("bookingType", "flight");  // Use intent1 here
            intent1.putExtra("flightBooking", flight);
            startActivity(intent1);

        });
    }
}