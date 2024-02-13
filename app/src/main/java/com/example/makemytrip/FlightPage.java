package com.example.makemytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class FlightPage extends AppCompatActivity {

    Button searchBtn;
    EditText departure,destination;
    private RecyclerView recyclerView;
    private FlightCardAdapter adapter;
    private DatabaseReference databaseReference;
    String selectedCountry;
    List<Flight> flights = new ArrayList<>();
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

        if (user != null) {
            String userId = user.getUid();

            // Get reference to the user's information node in the database
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("userInfo");
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserInfo userInfo = snapshot.getValue(UserInfo.class);

                    selectedCountry = userInfo.getSelectedCountry();
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
        Toast.makeText(this, "Loading data of" +  selectedCountry + "!!", Toast.LENGTH_SHORT).show();
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
                        for (DataSnapshot flightSnapshot: citySnapshot.child("airports").getChildren()) {

                            for (DataSnapshot flightsSnapshot: flightSnapshot.child("flights").getChildren()) {

                                Map<String, Object> flightData = (Map<String, Object>) flightsSnapshot.getValue();


                                if (flightData != null) {
                                    Flight flt = flightsSnapshot.getValue(Flight.class);
                                    if (flt != null) {
                                        Log.d("FlightName", "Flight Name: " + flt.getAirline());
                                        Log.d("FlightName", "Flight ID: " + flt.getFlightId());
                                        Log.d("FlightName", "Flight City: " + flt.getDepartureCity());
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
//                for (Flight flt : flights) {
//                    adapter.updateIsLikedInFirebase(flt.getId(), flt.isLiked(),flt);
//                    Log.d("hotels", "onDataChange: " + flt.getName() + " " + flt.isLiked());
//                }
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