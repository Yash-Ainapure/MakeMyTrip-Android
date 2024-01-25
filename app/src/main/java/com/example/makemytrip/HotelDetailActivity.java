package com.example.makemytrip;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
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

    NumberPicker numberPickerRooms;
    private String selectedDates="1/02/2024";
    private int Price=0;
    Button buttonDatePicker, buttonBookHotel,buttonDatePicker2;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    TextView textViewHotelName, textViewHotelAddress;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);

        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("Hotel Details");
        // Retrieve hotel details from intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("hotel")) {
            Hotel hotel = intent.getParcelableExtra("hotel");

            Price=hotel.getPrice();
            // Now you have the 'hotel' object, you can display its details in the activity
            TextView textViewHotelName = findViewById(R.id.textViewHotelName);
            TextView textViewHotelAddress = findViewById(R.id.textViewHotelAddress);
            ImageView imageViewHotel = findViewById(R.id.imageViewHotel);

            ImageView otherImage1=findViewById(R.id.otherImage1);
            ImageView otherImage2=findViewById(R.id.otherImage2);
            if (hotel.getOtherImages() != null && hotel.getOtherImages().size() > 0) {
                Picasso.get().load(hotel.getOtherImages().get(0)).into(otherImage1);
                Picasso.get().load(hotel.getOtherImages().get(1)).into(otherImage2);
            }


            textViewHotelName.setText(hotel.getName());
            textViewHotelAddress.setText(hotel.getAddress());


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

        numberPickerRooms.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setBackgroundResource(R.drawable.np_number_picker_pressed);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.setBackgroundResource(R.drawable.np_number_picker_default);
            }
            return false;
        });

        numberPickerRooms.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                v.setBackgroundResource(R.drawable.np_number_picker_focused);
            } else {
                v.setBackgroundResource(R.drawable.np_number_picker_default);
            }
        });

        //choose dates functionality
        buttonDatePicker = findViewById(R.id.buttonDatePicker);
        buttonDatePicker2 = findViewById(R.id.buttonDatePicker2);
        buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(buttonDatePicker);
            }
        });
        buttonDatePicker2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(buttonDatePicker2);
            }
        });


        buttonBookHotel = findViewById(R.id.buttonBookHotel);
        textViewHotelName = findViewById(R.id.textViewHotelName);
        textViewHotelAddress = findViewById(R.id.textViewHotelAddress);
//        TextView textViewSelectedDates = findViewById(R.id.textViewSelectedDates);
        buttonBookHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buttonDatePicker.getText().toString().equals("Choose starting date") || buttonDatePicker2.getText().toString().equals("Choose ending date")){
                    Toast.makeText(HotelDetailActivity.this, "choose the duration", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(HotelDetailActivity.this, "payment gateway", Toast.LENGTH_SHORT).show();
                Intent intent = getIntent();
                String ImageUrl="demo2";
                if (intent != null && intent.hasExtra("hotel")) {
                    Hotel hotel = intent.getParcelableExtra("hotel");
                    ImageUrl=hotel.getImageUrl();
                    Intent intent1 = new Intent(HotelDetailActivity.this, PaymentGateway.class);
                    HotelBookedInfo hotelBookedInfo = new HotelBookedInfo(textViewHotelName.getText().toString(),textViewHotelAddress.getText().toString(),numberPickerRooms.getValue()
                           ,buttonDatePicker.getText().toString(),buttonDatePicker2.getText().toString(),ImageUrl);
                    intent1.putExtra("hotel",hotel);
                    intent1.putExtra("hotelBookedInfo",hotelBookedInfo);
                    startActivity(intent1);
                }

                //save this hotel booked details under logged in user in firebase realtime database
                //get email of person logged in
                //get hotel name, address, image url, dates, rooms, days staying
                //save this data under user email in firebase realtime database
//                FirebaseUser user = mAuth.getCurrentUser();
//                if(user!=null){
//                    String LoggedUserEmail = user.getUid();
//                    databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(LoggedUserEmail).child("bookedHotels");
//                    String UserId=databaseReference.push().getKey();
//                    Date selectedDate = new Date(selectedDates);
//                    HotelBookedInfo hotelBookedInfo = new HotelBookedInfo(textViewHotelName.getText().toString(),textViewHotelAddress.getText().toString(),numberPickerRooms.getValue()
//                            ,buttonDatePicker.getText().toString(),buttonDatePicker2.getText().toString(),ImageUrl);
//                    databaseReference.child(UserId).setValue(hotelBookedInfo);
//
//                }else{
//                    Toast.makeText(HotelDetailActivity.this, "Error: No user logged in", Toast.LENGTH_SHORT).show();
//                }

            }
        });


        buttonBookHotel.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Button pressed, scale down
                    scaleButton(buttonBookHotel, 0.95f);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    // Button released or touch canceled, scale back to the original size
                    scaleButton(buttonBookHotel, 1f);
                    break;
            }
            return false;
        });

    }

    private void showDatePickerDialog(Button buttonDatePickerx) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Handle the selected date
                selectedDates = dayOfMonth + "/" + (month + 1) + "/" + year;
                //buttonDatePicker.setText(selectedDates);
                buttonDatePickerx.setText(selectedDates);
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

    private void scaleButton(Button button, float scale) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(button, "scaleX", scale);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(button, "scaleY", scale);
        scaleDownX.setDuration(150);
        scaleDownY.setDuration(150);
        scaleDownX.start();
        scaleDownY.start();
    }

}