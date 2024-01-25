package com.example.makemytrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PaymentGateway extends AppCompatActivity {

    TextView price,durationTextView,total,rooms,gst;
    Button pay;
    int totalAmount;
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

        Intent intent = getIntent();
        HotelBookedInfo receivedBookedInfo = (HotelBookedInfo) intent.getSerializableExtra("hotelBookedInfo");
        Hotel hotel = (Hotel) intent.getParcelableExtra("hotel");
        if (receivedBookedInfo != null && hotel != null) {
            // Now you have the object in the receiving activity
            price.setText("Price: "+hotel.getPrice());
            rooms.setText("Rooms: "+receivedBookedInfo.getNumberOfRooms());

            long duration = DateUtils.getDuration(receivedBookedInfo.getStartingDate(), receivedBookedInfo.getEndingDate());
            durationTextView.setText("Duration: "+duration);
            gst.setText("GST: "+(hotel.getPrice()*duration*receivedBookedInfo.getNumberOfRooms()*0.18));
            totalAmount = (int) (hotel.getPrice()*duration*receivedBookedInfo.getNumberOfRooms()+(hotel.getPrice()*duration*receivedBookedInfo.getNumberOfRooms()*0.18));
            total.setText("Total: "+totalAmount);
        }


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PaymentGateway.this, "payment done", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PaymentGateway.this, DummyUPIPayment.class);
                intent.putExtra("totalAmount",totalAmount);
                intent.putExtra("hotelBookedInfo",receivedBookedInfo);
                intent.putExtra("hotel",hotel);
                startActivity(intent);
            }
        });
    }
}