package com.example.makemytrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class Country extends AppCompatActivity {

    private DatabaseReference databaseReference , databaseReference2;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        // Initialize Firebase Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("countries");

        // Initialize UI components
        listView = findViewById(R.id.listViewCountries);

        // Initialize adapter for the ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        // Set item click listener for the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the clicked item
                String selectedCountry = adapter.getItem(position);

                // Store the selected country in the user's information in the database
                storeSelectedCountry(selectedCountry);

                // Move back to Home.class
                Intent intent = new Intent(Country.this, home.class);
                startActivity(intent);
                finish(); // Close the current activity
            }
        });





        // Retrieve countries from Firebase and display in the ListView
        retrieveCountries();
    }
    private void storeSelectedCountry(String selectedCountry) {
        // Retrieve the user's ID from Firebase Authentication
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid();

            // Get reference to the user's information node in the database
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("userInfo");

            // Store the selected country under a new node (e.g., "selectedCountry")
            userRef.child("selectedCountry").setValue(selectedCountry);
        }}
    private void retrieveCountries() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Check if dataSnapshot exists and has children
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                    List<String> countries = new ArrayList<>();

                    // Iterate through each country in dataSnapshot
                    for (DataSnapshot countrySnapshot : dataSnapshot.getChildren()) {
                        // Get the country name
                        String countryName = countrySnapshot.getKey();

                        // Add the country name to the list
                        countries.add(countryName);
                    }

                    // Now, 'countries' array contains all country names from Firebase
                    // You can use this list to update the ListView adapter
                    updateListView(countries);
                } else {
                    // Handle the case when no countries are found
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    private void updateListView(List<String> countries) {
        // Clear the existing items in the adapter
        adapter.clear();

        // Add all countries to the adapter
        adapter.addAll(countries);

        // Notify the adapter that the data set has changed
        adapter.notifyDataSetChanged();
    }
}
