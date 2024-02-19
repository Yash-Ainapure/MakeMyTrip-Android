package com.example.makemytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FlightPage extends AppCompatActivity {

    Button searchBtn;
    EditText departure,destination;
    private RecyclerView recyclerView;
    private FlightCardAdapter adapter;
    private DatabaseReference databaseReference;
    String selectedCountry;
    List<Flight> flights = new ArrayList<>();

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
        setContentView(R.layout.activity_flight_page);

                // Initialize RecyclerView and Adapter
                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                adapter = new FlightCardAdapter(new ArrayList<>());
                recyclerView.setAdapter(adapter);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Set your desired icon for the navigation drawer toggle
        }
        getSupportActionBar().setTitle("Flights");
        if (user != null) {
            String userId = user.getUid();

            // Get reference to the user's information node in the database
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("userInfo");

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserInfo userInfo = snapshot.getValue(UserInfo.class);

                    selectedCountry = userInfo.getSelectedCountry();
                    if(selectedCountry == null){
                        selectedCountry = "India";
                        userInfo.setSelectedCountry(selectedCountry);
                        databaseReference.child("selectedCountry").setValue(selectedCountry);
                    }
                    databaseReference = FirebaseDatabase.getInstance().getReference("countries").child(selectedCountry);
                    loadData();

                    searchBtn = findViewById(R.id.searchBtn);
                    departure = findViewById(R.id.departure);
                    destination = findViewById(R.id.destinatiom);
                    searchBtn.setOnClickListener(v -> {
                        String departureCity = departure.getText().toString().trim();
                        String destinationCity = destination.getText().toString().trim();

                        if (!TextUtils.isEmpty(departureCity) && !TextUtils.isEmpty(destinationCity)) {
                            List<Flight> filteredFlights = new ArrayList<>();

                            for (Flight flight : flights) {
                                if (flight.getDepartureCity().equalsIgnoreCase(departureCity) &&
                                        flight.getDestinationCity().equalsIgnoreCase(destinationCity)) {
                                    filteredFlights.add(flight);
                                } else {
                                    if (containsKeyword(flight.getDepartureCity(), departureCity) ||
                                            containsKeyword(flight.getDestinationCity(), destinationCity) ||
                                            containsKeyword(flight.getAirline(), departureCity) || // Assuming airline is a relevant keyword
                                            containsKeyword(flight.getAirline(), destinationCity)) {
                                        filteredFlights.add(flight);
                                    }
                                }
                            }
                            LottieAnimationView animationView = findViewById(R.id.animationView);
                            RecyclerView recyclerView = findViewById(R.id.recyclerView);
                            TextView emptyText = findViewById(R.id.nothing_found);
                            TextView emptyHotelSubText = findViewById(R.id.description2);
                            if (filteredFlights.isEmpty()) {
                                animationView.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                emptyText.setVisibility(View.VISIBLE);
                                emptyHotelSubText.setVisibility(View.VISIBLE);
                            } else {
                                animationView.setVisibility(View.GONE);
                                emptyText.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                emptyHotelSubText.setVisibility(View.GONE);
                            }
                            FlightCardAdapter adapter = new FlightCardAdapter(filteredFlights);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(FlightPage.this, "Please enter both departure and destination cities", Toast.LENGTH_SHORT).show();
                            loadData();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void loadData() {
        Toast.makeText(this, "Loading data of " + selectedCountry + "!!", Toast.LENGTH_SHORT).show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                flights.clear();

                // Iterate through states
                for (DataSnapshot stateSnapshot : snapshot.child("states").getChildren()) {

                    // Iterate through cities
                    for (DataSnapshot citySnapshot : stateSnapshot.child("cities").getChildren()) {

                        //comes till here only
                        // Iterate through hotels
                        for (DataSnapshot flightSnapshot : citySnapshot.child("airports").getChildren()) {

                            for (DataSnapshot flightsSnapshot : flightSnapshot.child("flights").getChildren()) {

                                Map<String, Object> flightData = (Map<String, Object>) flightsSnapshot.getValue();

                                if (flightData != null) {
                                    Flight flt = flightsSnapshot.getValue(Flight.class);
                                    if (flt != null) {
                                        Log.d("FlightName", "Flight Name: " + flt.getAirline());
                                        Log.d("FlightName", "Flight ID: " + flt.getFlightId());
                                        Log.d("FlightName", "Flight City: " + flt.getDepartureCity());

                                        Random random = new Random();

                                        // Generate a random price between 2000 and 6000
                                        int randomPrice = random.nextInt(4001) + 2000;
                                        // Set the random price to your flight object
                                        flt.setFlightPrice(randomPrice);

                                        flights.add(flt);
                                    } else {
                                        Log.d("NullData", "Null data found for flight: " + flightsSnapshot.getValue());
                                        Toast.makeText(FlightPage.this, "Null data found for flight", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Log.d("NullData", "Null data found for flightSnapshot: " + flightsSnapshot.getValue());
                                    Toast.makeText(FlightPage.this, "Null data found for flightSnapshot", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }

                adapter = new FlightCardAdapter(flights);
                recyclerView.setAdapter(adapter);

                // Set the database reference for the adapter
                adapter.setDatabaseReference(databaseReference.child("states"));

                // Update the isLiked field for each hotel in the adapter
//            for (Flight flt : flights) {
//                adapter.updateIsLikedInFirebase(flt.getId(), flt.isLiked(),flt);
//                Log.d("hotels", "onDataChange: " + flt.getName() + " " + flt.isLiked());
//            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Toast.makeText(FlightPage.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean containsKeyword(String source, String keyword) {
        return source.toLowerCase().contains(keyword.toLowerCase());
    }

}