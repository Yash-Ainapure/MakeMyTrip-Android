package com.example.makemytrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class FlightPayment extends AppCompatActivity {

    TextView price,classTextview,total,rooms,gst;
    Button pay;
    int totalAmount;
    int noOfPeople;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_payment);

        Intent intent= getIntent();
        Flight flight = intent.getParcelableExtra("flightBooking");
        String BookingType= intent.getStringExtra("BookingType");

        noOfPeople = intent.getIntExtra("numberOfPeople", 1);

        String flightClass=intent.getStringExtra("class");


        price=findViewById(R.id.price);
        classTextview=findViewById(R.id.flightclass);
        total=findViewById(R.id.total);
        rooms=findViewById(R.id.rooms);
        pay=findViewById(R.id.pay);
        gst=findViewById(R.id.gst);
        getSupportActionBar().setTitle("Confirm Booking");

        price.setText("₹ "+flight.getFlightPrice());
        rooms.setText(noOfPeople+ " People");

        classTextview.setText(flightClass);
        gst.setText("₹ "+(flight.getFlightPrice()*noOfPeople*0.18));
        totalAmount = (int) (flight.getFlightPrice()*noOfPeople);
        total.setText("₹ "+totalAmount);

        pay.setOnClickListener(v -> {
            Intent intent1 = new Intent(FlightPayment.this, DummyUPIPayment.class);
            intent1.putExtra("totalAmount",totalAmount);
            intent1.putExtra("bookingType","flight");
            intent1.putExtra("flightBooking",flight);
            startActivity(intent1);
        });

    }
}