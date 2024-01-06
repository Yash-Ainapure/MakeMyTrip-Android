package com.example.makemytrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class HotelDetailActivity extends AppCompatActivity {

    NumberPicker numberPickerRooms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);

        // Retrieve hotel details from intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("hotel")) {
            Hotel hotel = intent.getParcelableExtra("hotel");

            // Now you have the 'hotel' object, you can display its details in the activity
            TextView textViewHotelName = findViewById(R.id.textViewHotelName);
            TextView textViewHotelAddress = findViewById(R.id.textViewHotelAddress);
            ImageView imageViewHotel = findViewById(R.id.imageViewHotel);

            textViewHotelName.setText(hotel.getName());
            textViewHotelAddress.setText(hotel.getAddress());

            // You may want to use a library like Picasso or Glide for image loading
            // For simplicity, assuming 'getImageUrl()' returns a valid image URL

            //Picasso.get().load(hotel.getImageUrl()).into(imageViewHotel);
        } else {
            // Handle the case where no hotel details are provided
            Toast.makeText(this, "Error: No hotel details found", Toast.LENGTH_SHORT).show();
            finish(); // Finish the activity if there's an error
        }

        numberPickerRooms = findViewById(R.id.numberPickerRooms);
        numberPickerRooms.setMinValue(1);
        numberPickerRooms.setMaxValue(10); // Set your desired maximum value here
        numberPickerRooms.setValue(1); // Set an initial value
    }
}
