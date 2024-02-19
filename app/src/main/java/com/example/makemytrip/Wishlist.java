//package com.example.makemytrip;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//
//public class Wishlist extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_wishlist);
//    }
//}

package com.example.makemytrip;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class Wishlist extends AppCompatActivity {
    private RecyclerView recyclerView;
    private WishlistCardAdapter wishlistAdapter;
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
        setContentView(R.layout.activity_wishlist);
        getSupportActionBar().setTitle("Wishlist");
        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.recyclerViewWishlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        wishlistAdapter = new WishlistCardAdapter(new ArrayList<>());
        recyclerView.setAdapter(wishlistAdapter);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Set your desired icon for the navigation drawer toggle
        }
        // Retrieve and display liked hotels
        displayLikedHotels();
    }

    private void displayLikedHotels() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String loggedUserId = user.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(loggedUserId).child("hotelsWishlist");

            // Retrieve liked hotels
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Hotel> likedHotelList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Hotel hotel = dataSnapshot.getValue(Hotel.class);
                        if (hotel != null) {
                            hotel.setLiked(dataSnapshot.child("liked").getValue(Boolean.class));
                            likedHotelList.add(hotel);
                        }
                    }

                    // Check if the likedHotelList is empty
                    LottieAnimationView animationView = findViewById(R.id.animationView);
                    RecyclerView recyclerView = findViewById(R.id.recyclerViewWishlist);
                    TextView emptyWishlistText = findViewById(R.id.nothing_saved_yet);
                    TextView emptyWishlistSubText = findViewById(R.id.description_wishlist);
                    if (likedHotelList.isEmpty()) {
                        animationView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        emptyWishlistText.setVisibility(View.VISIBLE);
                        emptyWishlistSubText.setVisibility(View.VISIBLE);

                    } else {
                        animationView.setVisibility(View.GONE);
                        emptyWishlistText.setVisibility(View.GONE);
                        emptyWishlistSubText.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }


                    // Update adapter with liked hotels
                    wishlistAdapter.setWishlist(likedHotelList);

                    // Update the isLiked field for each hotel in the adapter
                    for (Hotel hotel : likedHotelList) {
                        wishlistAdapter.updateIsLikedInFirebase(hotel.getId(), hotel.isLiked(), hotel);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                    Toast.makeText(Wishlist.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Please Login to view wishlist", Toast.LENGTH_SHORT).show();
        }
    }
}
