package com.example.makemytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HotelPage extends AppCompatActivity {

    Button searchBtn;

    String selectedCountry = "India";
    EditText searchEdt;
    private RecyclerView recyclerView;
    private HotelCardAdapter adapter;
    private DatabaseReference databaseReference;
    List<Hotel> hotelList = new ArrayList<>();

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
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_page);

        FirebaseApp.initializeApp(this);
        getSupportActionBar().setTitle("Hotels");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchBtn=(Button) findViewById(R.id.searchBtn);
        searchEdt=(EditText) findViewById(R.id.searchEdt);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Set your desired icon for the navigation drawer toggle
        }


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
                    selectedCountry= userInfo.getSelectedCountry();
                    if(selectedCountry == null){
                        selectedCountry = "India";
                        userInfo.setSelectedCountry(selectedCountry);
                        databaseReference.child("selectedCountry").setValue(selectedCountry);
                    }
                    databaseReference = FirebaseDatabase.getInstance().getReference("countries").child(selectedCountry);
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

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            // Store the selected country under a new node (e.g., "selectedCountry")

        }



        // Initialize Firebase
        //changing as per country , state and city
        //databaseReference = FirebaseDatabase.getInstance().getReference("hotels");



        // Set up the initial data

        // perform search when button clicked
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });


        searchBtn.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Button pressed, scale down
                    scaleButton(searchBtn, 0.95f);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    // Button released or touch canceled, scale back to the original size
                    scaleButton(searchBtn, 1f);
                    break;
            }
            return false;
        });
    }

    private void scaleButton(Button button, float scale) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(button, "scaleX", scale);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(button, "scaleY", scale);
        scaleDownX.setDuration(150);
        scaleDownY.setDuration(150);
        scaleDownX.start();
        scaleDownY.start();
    }
//    private void loadData() {
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<Hotel> hotelList = new ArrayList<>();
//

//
////old logic
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Hotel hotel = dataSnapshot.getValue(Hotel.class);
//                    if (hotel != null) {
//                        // Set the isLiked field based on the database value
//                        hotel.setLiked(dataSnapshot.child("isLiked").getValue(Boolean.class));
//                        hotelList.add(hotel);
//                    }
//                }
//
//                        adapter = new HotelCardAdapter(hotelList);
//                        recyclerView.setAdapter(adapter);
//
//                        // Set the database reference for the adapter
//                        adapter.setDatabaseReference(databaseReference);
//
//                        // Update the isLiked field for each hotel in the adapter
//                        for (Hotel hotel : hotelList) {
//                            adapter.updateIsLikedInFirebase(hotel.getId(), hotel.isLiked());
//                            Log.d("hotels", "onDataChange: "+hotel.getName()+" "+hotel.isLiked());
//                        }
//
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle error
//                Toast.makeText(HotelPage.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//new logic
            private void loadData() {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        hotelList.clear();

                        // Iterate through states
                        for (DataSnapshot stateSnapshot : snapshot.child("states").getChildren()) {
                            // Iterate through cities
                            for (DataSnapshot citySnapshot : stateSnapshot.child("cities").getChildren()) {
                                // Iterate through hotels
                               for (DataSnapshot hotelSnapshot: citySnapshot.child("hotels").getChildren()) {// Retrieve hotel data as a Map
                                    Map<String, Object> hotelData = (Map<String, Object>) hotelSnapshot.getValue();

                                    if (hotelData != null) {
//                                        Toast.makeText(HotelPage.this, "Database Error: " +  hotelData.get("name").toString(), Toast.LENGTH_SHORT).show();
                                        if (hotelData.get("rating")==null){
                                            hotelData.put("rating",0.0);
                                        }
                                        Log.d("hotel name", "onDataChange: "+ hotelData.get("name")+" "+hotelData.containsKey("ratings"));
                                        // Create a Hotel object from the Map
                                        Hotel hotel = new Hotel();
                                        hotel.setId(hotelSnapshot.getKey());
                                        hotel.setName((String) hotelData.get("name"));
                                        hotel.setAddress((String) hotelData.get("address"));
                                        hotel.setImageUrl((String) hotelData.get("imageUrl"));
                                        hotel.setIsLiked((Boolean) hotelData.get("isLiked"));
                                        hotel.setPrice(((Long) hotelData.get("price")).intValue());
//                                       hotel.setRating(((Double) hotelData.get("rating")).floatValue());
                                       hotel.setCity((String) citySnapshot.getKey());
                                        hotel.setState((String) stateSnapshot.getKey());
                                        if (hotelData.containsKey("ratings")) {
                                            Map<String, Object> ratingsData = (Map<String, Object>) hotelData.get("ratings");

                                            if (ratingsData != null) {
//                                                float averageRating = ((Double) ratingsData.get("averageRating")).floatValue();
//                                                int numRatings = ((Long) ratingsData.get("numRatings")).intValue();
//                                                float totalRating = ((Double) ratingsData.get("totalRating")).floatValue();
//                                                Log.d("hotelrate", "onDataChange: "+averageRating);
//                                                // Create Ratings object
//                                                Hotel.Ratings ratings = new Hotel.Ratings(averageRating, numRatings, totalRating);
//                                                Log.d("Exam2", "onDataChange: "+ratings.getAverageRating());

                                                // Set Ratings to Hotel

                                            }
                                        }
                                        Log.d("city and state",""+hotel.getName()+" : "+hotel.getCity());
//                                         Handle otherImages
                                        List<String> otherImages = (List<String>) hotelData.get("otherImages");
                                        if (otherImages != null) {
                                            hotel.setOtherImages(otherImages);
                                        }

                                        hotelList.add(hotel);
                                    }
                                    else {
                                        Toast.makeText(HotelPage.this, "Null data " + hotelSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();}

                               }
                            }
                        }

                        adapter = new HotelCardAdapter(hotelList);
                        recyclerView.setAdapter(adapter);

                        // Set the database reference for the adapter
                        adapter.setDatabaseReference(databaseReference.child("states"));

                        // Update the isLiked field for each hotel in the adapter
                        for (Hotel hotel : hotelList) {
                            adapter.updateIsLikedInFirebase(hotel.getId(), hotel.isLiked(),hotel);
                            Log.d("hotels", "onDataChange: " + hotel.getName() + " " + hotel.isLiked());
                        }
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
            List<Hotel> filteredHotelList = new ArrayList<>();

            // Iterate through the original hotelList and filter based on the search query
            for (Hotel hotel : hotelList) {
                if (hotel.getName().toLowerCase().contains(queryText.toLowerCase()) ||
                        hotel.getAddress().toLowerCase().contains(queryText.toLowerCase())) {
                    filteredHotelList.add(hotel);
                }
            }
            LottieAnimationView animationView = findViewById(R.id.animationView);
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            TextView emptyText = findViewById(R.id.nothing_found);
            TextView emptyHotelSubText = findViewById(R.id.description2);
            if (filteredHotelList.isEmpty()) {
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
            // Update the adapter with the filtered list
            adapter = new HotelCardAdapter(filteredHotelList);
            recyclerView.setAdapter(adapter);

            // Set the database reference for the adapter
            adapter.setDatabaseReference(databaseReference.child("states"));

            // Update the isLiked field for each hotel in the adapter
            for (Hotel hotel : filteredHotelList) {
                adapter.updateIsLikedInFirebase(hotel.getId(), hotel.isLiked(), hotel);
                Log.d("hotels", "onDataChange: " + hotel.getName() + " " + hotel.isLiked());
            }
        }
    }



    // Search feature logic
    //private void performSearch() {
//        String queryText = searchEdt.getText().toString().trim();
//
//        if (TextUtils.isEmpty(queryText)) {
//            // Empty search query, load all hotels
//            loadData();
//        } else {

            // Perform a search based on both name and address
//            Query searchQuery = databaseReference.orderByChild("name").startAt(queryText).endAt(queryText + "\uf8ff");
//
//            searchQuery.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot nameSnapshot) {
//                    List<Hotel> searchResult = new ArrayList<>();
//                    for (DataSnapshot nameDataSnapshot : nameSnapshot.getChildren()) {
//                        Hotel hotel = nameDataSnapshot.getValue(Hotel.class);
//                        if (hotel != null && hotel.getName().contains(queryText)) {
//                            hotel.setLiked(nameDataSnapshot.child("isLiked").getValue(Boolean.class));
//                            searchResult.add(hotel);
//                        }
//                    }
//
//                    // Now, let's check the "address" field
//                    databaseReference.orderByChild("address").startAt(queryText).endAt(queryText + "\uf8ff")
//                            .addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot addressSnapshot) {
//                                    for (DataSnapshot addressDataSnapshot : addressSnapshot.getChildren()) {
//                                        Hotel hotel = addressDataSnapshot.getValue(Hotel.class);
//                                        if (hotel != null && hotel.getAddress().contains(queryText) && !searchResult.contains(hotel)) {
//                                            // Avoid duplicate entries
//                                            hotel.setLiked(addressDataSnapshot.child("isLiked").getValue(Boolean.class));
//                                            searchResult.add(hotel);
//                                        }
//                                    }
//
//                                    // Update the existing adapter with the combined search results
//                                    if (adapter != null) {
//                                        adapter.setHotelList(searchResult);
//                                        adapter.notifyDataSetChanged();
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//                                    // Handle error
//                                    Toast.makeText(HotelPage.this, "Search Error: problem during address searching" + error.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    // Handle error
//                    Toast.makeText(HotelPage.this, "Search Error: problem during name searching" + error.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
        //}
    //}

}