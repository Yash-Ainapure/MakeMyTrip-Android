package com.example.makemytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class FlightPayment extends AppCompatActivity {

    TextView price,classTextview,total,rooms,gst;
    Button pay;
    int totalAmount;
    int noOfPeople;
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
        setContentView(R.layout.activity_flight_payment);

        Intent intent= getIntent();
        Flight flight = intent.getParcelableExtra("flightBooking");
        String BookingType= intent.getStringExtra("BookingType");


        noOfPeople = intent.getIntExtra("numberOfPeople", 1);

        String flightClass=intent.getStringExtra("class");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Set your desired icon for the navigation drawer toggle
        }

        price=findViewById(R.id.price);
        classTextview=findViewById(R.id.flightclass);
        total=findViewById(R.id.total);
        rooms=findViewById(R.id.rooms);
        pay=findViewById(R.id.pay);
        gst=findViewById(R.id.gst);
        getSupportActionBar().setTitle("Confirm Flight Booking");

        price.setText("₹ "+flight.getFlightPrice());
        rooms.setText(noOfPeople+ " People");

        classTextview.setText(flightClass);

        // Adjust total amount based on flight class
        double classMultiplier = 1.0; // Default multiplier for economy class

        if ("First Class".equals(flightClass)) {
            classMultiplier = 1.5; // Adjust multiplier for first class
        } else if ("Business".equals(flightClass)) {
            classMultiplier = 1.2; // Adjust multiplier for business class
        }

        double gstAmount = flight.getFlightPrice() * noOfPeople * 0.18;
        double totalAmount = flight.getFlightPrice() * noOfPeople * classMultiplier;
        double totalAmountWithGST = totalAmount + gstAmount;
        gst.setText("₹ " + gstAmount);
        total.setText("₹ " + totalAmountWithGST + "\n (including GST)");

        pay.setOnClickListener(v -> {
            Intent intent1 = new Intent(FlightPayment.this, DummyUPIPayment.class);
            intent1.putExtra("totalAmount", (int) totalAmountWithGST );
            intent1.putExtra("bookingType", "flight");
            intent1.putExtra("flightBooking", flight);
            startActivity(intent1);
        });

    }
}