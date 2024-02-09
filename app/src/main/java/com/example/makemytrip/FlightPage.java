package com.example.makemytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FlightPage extends AppCompatActivity {

    Button searchBtn;
    EditText searchEdt;
    private RecyclerView recyclerView;
    private FlightCardAdapter adapter;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_page);

                // Initialize RecyclerView and Adapter
                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                adapter = new FlightCardAdapter(new ArrayList<>());
                recyclerView.setAdapter(adapter);

                // Initialize Firebase DatabaseReference
                databaseReference = FirebaseDatabase.getInstance().getReference("flights");

                // Retrieve flights from Firebase and assign to adapter
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Flight> flights = new ArrayList<>();
                        for (DataSnapshot flightSnapshot : dataSnapshot.getChildren()) {
                            Flight flight = flightSnapshot.getValue(Flight.class);
                            flights.add(flight);
                        }
                        adapter.setFlightList(flights);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle error
                    }
                });
    }
}