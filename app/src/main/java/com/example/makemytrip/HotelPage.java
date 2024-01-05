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
                loadData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Toast.makeText(HotelPage.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Handle search button click
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
                        hotelList.add(hotel);
                    }
                }
                adapter = new HotelCardAdapter(hotelList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Toast.makeText(HotelPage.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

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

                                    // Update the adapter with the combined search results
                                    adapter = new HotelCardAdapter(searchResult);
                                    recyclerView.setAdapter(adapter);
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


    //previous working search feature
//private void performSearch() {
//    String location = searchEdt.getText().toString().trim();
//
//    if (TextUtils.isEmpty(location)) {
//        // Empty search query, load all hotels
//        loadData();
//    } else {
//        // Perform a search based on the location
//        Query searchQuery = databaseReference.orderByChild("address").startAt(location).endAt(location + "\uf8ff");
//
//        searchQuery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<Hotel> searchResult = new ArrayList<>();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Hotel hotel = dataSnapshot.getValue(Hotel.class);
//                    if (hotel != null) {
//                        searchResult.add(hotel);
//                    }
//                }
//                adapter = new HotelCardAdapter(searchResult);
//                recyclerView.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle error
//                Toast.makeText(HotelPage.this, "Search Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}

//first version

//        searchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String searchInput=searchEdt.getText().toString();
//                if (searchInput.isEmpty()) {
//                    Toast.makeText(HotelPage.this, "Please enter location to search", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//            }
//        });
//
//        // Add a ValueEventListener to update the adapter when data changes
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<Hotel> hotelList = new ArrayList<>();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Hotel hotel = dataSnapshot.getValue(Hotel.class);
//                    if (hotel != null) {
//                        hotelList.add(hotel);
//                    }
//                }
//                adapter = new HotelCardAdapter(hotelList);
//                recyclerView.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle error
//                Toast.makeText(HotelPage.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }