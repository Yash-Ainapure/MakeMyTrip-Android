package com.example.makemytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FlightDetailActivity extends AppCompatActivity {

    private TextView fltNameTextView;
    private TextView fltDeptTextView , fltDeptTextView1;
    private TextView fltDestTextView , fltDestTextView1;
    private TextView deptTimeTextView;
    private int numberOfTravelers = 1;
    private TextView destTimeTextView;
    private TextView airportTextView , destAirportTextView,durationTextView ,duration1;
    private Button bookButton;
   private ImageView flightImage;

    private TextView numberOfTravelersTextView;
    Flight flight;
    NumberPicker numberPickerPeople;
    private Spinner flightSpinner;

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
        fltDeptTextView1 = findViewById(R.id.fltdept1);
        fltDestTextView1 = findViewById(R.id.fltdest1);
        deptTimeTextView = findViewById(R.id.deptTime);
        destTimeTextView = findViewById(R.id.destTime);
        airportTextView = findViewById(R.id.depairport);
        destAirportTextView = findViewById(R.id.destairport);
        durationTextView = findViewById(R.id.fltduration);
        duration1 = findViewById(R.id.fltduration1);
        bookButton = findViewById(R.id.bookFlight);
        flightImage = findViewById(R.id.imageViewCompanyLogo);
        numberOfTravelersTextView = findViewById(R.id.noOfTravellers);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Set your desired icon for the navigation drawer toggle
        }
        getSupportActionBar().setTitle("Flights Details");
        Intent intent = getIntent();
        if (intent != null) {
            flight = intent.getParcelableExtra("flight");

            fltNameTextView.setText(flight.getFlightName());
            fltDeptTextView.setText(flight.getDepartureCity());
            fltDestTextView.setText(flight.getDestinationCity());
            fltDeptTextView1.setText(flight.getDepartureCity());
            fltDestTextView1.setText(flight.getDestinationCity());
            deptTimeTextView.setText(flight.getDepartureTime());
            destTimeTextView.setText(flight.getDestinationTime());
            airportTextView.setText(flight.getDepartureAirport());
            destAirportTextView.setText(flight.getDestinationAirport());
            Picasso.get().load(flight.getFlightImage()).into(flightImage);
            String departureTime = flight.getDepartureTime();
            String destinationTime = flight.getDestinationTime();

            String duration = calculateDuration(departureTime, destinationTime);
            duration1.setText("Non stop | " + duration);
// Set the duration in your TextView
            durationTextView.setText(duration);
        }

        Button minusButton = findViewById(R.id.btnMinus);
        Button plusButton = findViewById(R.id.btnPlus);


        // Initial number of travelers

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberOfTravelers > 1) {
                    numberOfTravelers--;
                    numberOfTravelersTextView.setText(String.valueOf(numberOfTravelers));
                    // You can also update your logic here based on the new number of travelers
                }else {
                    Toast.makeText(FlightDetailActivity.this, "You have to book for minimum of 1 person", Toast.LENGTH_SHORT).show();

                }
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberOfTravelers < 10) {
                    numberOfTravelers++;
                    numberOfTravelersTextView.setText(String.valueOf(numberOfTravelers));

                    // You can also update your logic here based on the new number of travelers
                }
                else {
                    Toast.makeText(FlightDetailActivity.this, "You can book a maximum of 10 people at a time", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bookButton.setOnClickListener(v->{
            Intent intent1 = new Intent(FlightDetailActivity.this, FlightPayment.class);
            intent1.putExtra("bookingType", "flight");
            intent1.putExtra("numberOfPeople",numberOfTravelers);
            intent1.putExtra("flightBooking", flight);
            intent1.putExtra("class", flightSpinner.getSelectedItem().toString());
            startActivity(intent1);
            Log.d("FlightDetailActivity", "price: " + flight.getFlightPrice()+" \n class: "+flightSpinner.getSelectedItem().toString()+" \n no of people: "+numberOfTravelers);
        });











        flightSpinner = findViewById(R.id.flightSpinner);

        // Define the list of class types
        List<String> classTypes = getClassTypes();

        // Create an ArrayAdapter for class types
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        flightSpinner.setAdapter(adapter);

        flightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedClassType = (String) parentView.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "Selected Class: " + selectedClassType, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

    }
    public String calculateDuration(String departureTime, String destinationTime) {
        // Parse the departure and destination times
        String[] depTimeParts = departureTime.split(":");
        String[] destTimeParts = destinationTime.split(":");

        int depHours = Integer.parseInt(depTimeParts[0]);
        int depMinutes = Integer.parseInt(depTimeParts[1]);

        int destHours = Integer.parseInt(destTimeParts[0]);
        int destMinutes = Integer.parseInt(destTimeParts[1]);

        // Calculate the duration
        int durationHours = destHours - depHours;
        int durationMinutes = destMinutes - depMinutes;

        // Handle cases where the destination time is earlier than the departure time
        if (durationHours < 0) {
            durationHours += 24; // Add 24 hours to handle the next day
        }

        if (durationMinutes < 0) {
            durationMinutes += 60; // Add 60 minutes to handle borrowing from the next hour
            durationHours--; // Subtract 1 hour
        }

        // Format the duration as a string
        String durationString = String.format("%d hrs %d min", durationHours, durationMinutes);

        return durationString;
    }

    private List<String> getClassTypes() {
        List<String> classTypes = new ArrayList<>();
        // Add the types of flight classes
        classTypes.add("Economy");
        classTypes.add("Business");
        classTypes.add("First Class");
        return classTypes;
    }
}