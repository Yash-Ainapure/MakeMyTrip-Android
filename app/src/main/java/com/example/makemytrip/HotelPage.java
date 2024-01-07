package com.example.makemytrip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HotelPage extends AppCompatActivity {

    Button searchBtn;
    EditText searchEdt;
    private RecyclerView recyclerView;
    private HotelCardAdapter adapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_page);

        FirebaseApp.initializeApp(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchBtn=(Button) findViewById(R.id.searchBtn);
        searchEdt=(EditText) findViewById(R.id.searchEdt);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Set your desired icon for the navigation drawer toggle
        }

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("hotels");

        // Set up the initial data
        loadData();

        // Add a ValueEventListener to update the adapter when data changes
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (adapter != null) {
                    loadData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Toast.makeText(HotelPage.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // perform search when button clicked
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });
    }

    private void loadData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Hotel> hotelList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Hotel hotel = dataSnapshot.getValue(Hotel.class);
                    if (hotel != null) {
                        // Set the isLiked field based on the database value
                        hotel.setLiked(dataSnapshot.child("isLiked").getValue(Boolean.class));
                        hotelList.add(hotel);
                    }
                }
                adapter = new HotelCardAdapter(hotelList);
                recyclerView.setAdapter(adapter);

                // Set the database reference for the adapter
                adapter.setDatabaseReference(databaseReference);

                // Update the isLiked field for each hotel in the adapter
                for (Hotel hotel : hotelList) {
                    adapter.updateIsLikedInFirebase(hotel.getId(), hotel.isLiked());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Toast.makeText(HotelPage.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    //in search logic like/unlike is not set so it just displays the default unlike button will make changes in future
    // Search feature logic
    private void performSearch() {
        String queryText = searchEdt.getText().toString().trim();

        if (TextUtils.isEmpty(queryText)) {
            // Empty search query, load all hotels
            loadData();
        } else {
            // Perform a search based on both name and address
            Query searchQuery = databaseReference.orderByChild("name").startAt(queryText).endAt(queryText + "\uf8ff");

            searchQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot nameSnapshot) {
                    List<Hotel> searchResult = new ArrayList<>();
                    for (DataSnapshot nameDataSnapshot : nameSnapshot.getChildren()) {
                        Hotel hotel = nameDataSnapshot.getValue(Hotel.class);
                        if (hotel != null && hotel.getName().contains(queryText)) {
                            searchResult.add(hotel);
                        }
                    }

                    // Now, let's check the "address" field
                    databaseReference.orderByChild("address").startAt(queryText).endAt(queryText + "\uf8ff")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot addressSnapshot) {
                                    for (DataSnapshot addressDataSnapshot : addressSnapshot.getChildren()) {
                                        Hotel hotel = addressDataSnapshot.getValue(Hotel.class);
                                        if (hotel != null && hotel.getAddress().contains(queryText) && !searchResult.contains(hotel)) {
                                            // Avoid duplicate entries
                                            searchResult.add(hotel);
                                        }
                                    }

                                    // Update the existing adapter with the combined search results
                                    if (adapter != null) {
                                        adapter.setHotelList(searchResult);
                                        adapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle error
                                    Toast.makeText(HotelPage.this, "Search Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                    Toast.makeText(HotelPage.this, "Search Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}