package com.example.makemytrip;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class HotelDetailActivity extends AppCompatActivity {

    NumberPicker numberPickerRooms,numberPickerDays;
    private String selectedDates;
    Button buttonDatePicker;
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

        //set min and max values to rooms picker
        numberPickerRooms = findViewById(R.id.numberPickerRooms);
        numberPickerRooms.setMinValue(1);
        numberPickerRooms.setMaxValue(10); // Set your desired maximum value here
        numberPickerRooms.setValue(1); // Set an initial value

        //set min and max values to days staying picker
        numberPickerDays = findViewById(R.id.numberPickerDays);
        numberPickerDays.setMinValue(1);
        numberPickerDays.setMaxValue(10); // Set your desired maximum value here
        numberPickerDays.setValue(1); // Set an initial value

        //choose dates functionality
        buttonDatePicker = findViewById(R.id.buttonDatePicker);
        buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Handle the selected date
                selectedDates = dayOfMonth + "/" + (month + 1) + "/" + year;
                // Update the UI or perform any other actions based on the selected date
                // For example, you can display the selected date in a TextView
                TextView textViewSelectedDates = findViewById(R.id.textViewSelectedDates);
                textViewSelectedDates.setText(selectedDates);
            }
        };

        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show the DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                HotelDetailActivity.this,
                dateSetListener,
                year, month, day
        );
        datePickerDialog.show();
    }

}
