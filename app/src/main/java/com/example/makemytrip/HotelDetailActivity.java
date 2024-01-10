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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

public class HotelDetailActivity extends AppCompatActivity {

    NumberPicker numberPickerRooms,numberPickerDays;
    private String selectedDates;
    Button buttonDatePicker, buttonBookHotel;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    TextView textViewHotelName, textViewHotelAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);

        mAuth = FirebaseAuth.getInstance();

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

            Picasso.get().load(hotel.getImageUrl()).into(imageViewHotel);
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


        buttonBookHotel = findViewById(R.id.buttonBookHotel);
        textViewHotelName = findViewById(R.id.textViewHotelName);
        textViewHotelAddress = findViewById(R.id.textViewHotelAddress);
//        TextView textViewSelectedDates = findViewById(R.id.textViewSelectedDates);
        buttonBookHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HotelDetailActivity.this, "Hotel Booked", Toast.LENGTH_SHORT).show();
                Intent intent = getIntent();
                String ImageUrl="demo2";
                if (intent != null && intent.hasExtra("hotel")) {
                    Hotel hotel = intent.getParcelableExtra("hotel");
                    ImageUrl=hotel.getImageUrl();
                }
                //save this hotel booked details under logged in user in firebase realtime database
                //get email of person logged in
                //get hotel name, address, image url, dates, rooms, days staying
                //save this data under user email in firebase realtime database
                FirebaseUser user = mAuth.getCurrentUser();
                if(user!=null){
                    String LoggedUserEmail = user.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(LoggedUserEmail).child("bookedHotels");
                    String UserId=databaseReference.push().getKey();
                    Date selectedDate = new Date(selectedDates);
                    HotelBookedInfo hotelBookedInfo = new HotelBookedInfo(textViewHotelName.getText().toString(),textViewHotelAddress.getText().toString(),numberPickerRooms.getValue(),numberPickerDays.getValue(),selectedDates,ImageUrl);
                    databaseReference.child(UserId).setValue(hotelBookedInfo);

                }else{
                    Toast.makeText(HotelDetailActivity.this, "Error: No user logged in", Toast.LENGTH_SHORT).show();
                }

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
//                TextView textViewSelectedDates = findViewById(R.id.textViewSelectedDates);
//                textViewSelectedDates.setText(selectedDates);
                buttonDatePicker.setText(selectedDates);
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