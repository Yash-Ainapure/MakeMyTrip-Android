package com.example.makemytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FlightDetailActivity extends AppCompatActivity {

    private TextView fltNameTextView;
    private TextView fltDeptTextView;
    private TextView fltDestTextView;
    private TextView deptTimeTextView;
    private TextView destTimeTextView;
    private TextView airportTextView;
    private Button bookButton;
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
            Intent intent1 = new Intent(FlightDetailActivity.this, FlightPayment.class);
            intent1.putExtra("bookingType", "flight");
            intent1.putExtra("numberOfPeople", numberPickerPeople.getValue());
            intent1.putExtra("flightBooking", flight);
            intent1.putExtra("class", flightSpinner.getSelectedItem().toString());
            startActivity(intent1);
            Log.d("FlightDetailActivity", "price: " + flight.getFlightPrice()+" \n class: "+flightSpinner.getSelectedItem().toString()+" \n no of people: "+numberPickerPeople.getValue());
        });

        numberPickerPeople = findViewById(R.id.numberPickerRooms);
        numberPickerPeople.setMinValue(1);
        numberPickerPeople.setMaxValue(10);
        numberPickerPeople.setValue(1);

        numberPickerPeople.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setBackgroundResource(R.drawable.np_number_picker_pressed);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.setBackgroundResource(R.drawable.np_number_picker_default);
            }
            return false;
        });

        numberPickerPeople.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                v.setBackgroundResource(R.drawable.np_number_picker_focused);
            } else {
                v.setBackgroundResource(R.drawable.np_number_picker_default);
            }
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

    private List<String> getClassTypes() {
        List<String> classTypes = new ArrayList<>();
        // Add the types of flight classes
        classTypes.add("Economy");
        classTypes.add("Business");
        classTypes.add("First Class");
        return classTypes;
    }
}