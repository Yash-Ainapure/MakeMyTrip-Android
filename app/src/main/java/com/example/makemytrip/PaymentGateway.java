package com.example.makemytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PaymentGateway extends AppCompatActivity {

    TextView price,durationTextView,total,rooms,gst;
    Button pay;
    int totalAmount;
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
        setContentView(R.layout.activity_payment_gateway);

        price=findViewById(R.id.price);
        durationTextView=findViewById(R.id.duration);
        total=findViewById(R.id.total);
        rooms=findViewById(R.id.rooms);
        pay=findViewById(R.id.pay);
        gst=findViewById(R.id.gst);
        getSupportActionBar().setTitle("Confirm Booking");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Set your desired icon for the navigation drawer toggle
        }
        Intent intent = getIntent();
        HotelBookedInfo receivedBookedInfo = (HotelBookedInfo) intent.getSerializableExtra("hotelBookedInfo");
        Hotel hotel = (Hotel) intent.getParcelableExtra("hotel");
        if (receivedBookedInfo != null && hotel != null) {
            // Now you have the object in the receiving activity
            price.setText("₹ "+hotel.getPrice());
            rooms.setText(receivedBookedInfo.getNumberOfRooms()+ " Rooms");

            long duration = DateUtils.getDuration(receivedBookedInfo.getStartingDate(), receivedBookedInfo.getEndingDate());
            durationTextView.setText(duration + " days");
            gst.setText("₹ "+(hotel.getPrice()*duration*receivedBookedInfo.getNumberOfRooms()*0.18));
            totalAmount = (int) (hotel.getPrice()*duration*receivedBookedInfo.getNumberOfRooms()+(hotel.getPrice()*duration*receivedBookedInfo.getNumberOfRooms()*0.18));
            total.setText("₹ "+totalAmount);
        }


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PaymentGateway.this, DummyUPIPayment.class);
                intent.putExtra("totalAmount",totalAmount);
                intent.putExtra("hotelBookedInfo",receivedBookedInfo);
                intent.putExtra("hotel",hotel);
                startActivity(intent);
                finish(); // Finish the current activity
            }
        });
    }
}