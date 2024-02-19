package com.example.makemytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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

public class ViewBookedFlights extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookedFlightCardAdapter adapter;
    private List<Flight> bookedHotels; // Assuming you have this list

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
        setContentView(R.layout.activity_view_booked_flights);
        getSupportActionBar().setTitle("View Booked Flights");


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Set your desired icon for the navigation drawer toggle
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookedHotels = new ArrayList<>();
        // Initialize the adapter with the list of booked hotels
        adapter = new BookedFlightCardAdapter(this, bookedHotels);

        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);

        retrieveBookedHotelsData();
    }

    private void retrieveBookedHotelsData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("bookedFlights");

            // Attach a ValueEventListener to retrieve the data
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Clear the existing list before adding new data
                    bookedHotels.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Flight bookedFlight = snapshot.getValue(Flight.class);
                        if (bookedFlight != null) {
                            bookedHotels.add(bookedFlight);
                        }
                    }
                    LottieAnimationView animationView = findViewById(R.id.animationView);
                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    TextView emptyHotelText = findViewById(R.id.nothing_booked_yet);
                    TextView emptyHotelSubText = findViewById(R.id.description_flights);
                    if (bookedHotels.isEmpty()) {
                        animationView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        emptyHotelText.setVisibility(View.VISIBLE);
                        emptyHotelSubText.setVisibility(View.VISIBLE);

                    } else {
                        animationView.setVisibility(View.GONE);
                        emptyHotelText.setVisibility(View.GONE);
                        emptyHotelSubText.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    // Notify the adapter that the data has changed
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        }
    }
}