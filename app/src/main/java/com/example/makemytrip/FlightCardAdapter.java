package com.example.makemytrip;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

public class FlightCardAdapter extends RecyclerView.Adapter<FlightCardAdapter.FlightViewHolder> {
    private List<Flight> flightList;
    private DatabaseReference flightsRef;
    FirebaseAuth mAuth;

    public FlightCardAdapter(List<Flight> flightList) {
        this.flightList = flightList;
    }

    // Add a method to set the DatabaseReference separately
    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.flightsRef = databaseReference;
    }

    public void setFlightList(List<Flight> flightList) {
        this.flightList = flightList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flight_card_layout, parent, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flightList.get(position);
        holder.bindData(flight);

        // Set the click listener for the entire card to handle actions
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click action, e.g., navigate to flight details activity
                Intent intent = new Intent(v.getContext(), FlightDetailActivity.class);
                intent.putExtra("flight", flight);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    public class FlightViewHolder extends RecyclerView.ViewHolder {
        private TextView flightName;
        private TextView departureCity;
        private TextView destinationCity;
        private TextView flightPrice;
        private TextView departureTime;
        private TextView destinationTime;
        private ImageView flightImage;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            flightName = itemView.findViewById(R.id.textViewFlightName);
            departureCity = itemView.findViewById(R.id.textViewDepartureCity);
            destinationCity = itemView.findViewById(R.id.textViewDestinationCity);
            flightPrice = itemView.findViewById(R.id.textViewPrice);
            departureTime = itemView.findViewById(R.id.textViewDepartureTime);
            destinationTime = itemView.findViewById(R.id.textViewDestinationTime);
            flightImage = itemView.findViewById(R.id.imageViewCompanyLogo);

        }

        public void bindData(Flight flight) {
            // Bind data to UI elements

            flightName.setText(flight.getFlightName());
            departureCity.setText(flight.getDepartureCity());
            destinationCity.setText(flight.getDestinationCity());
            String price= String.valueOf(flight.getFlightPrice());
            flightPrice.setText("₹ "+ price);
            departureTime.setText(flight.getDepartureTime());
            destinationTime.setText(flight.getDestinationTime());


            Picasso.get().load(flight.getFlightImage()).into(flightImage);

        }
    }

    void updateIsLikedInFirebase(String flightId, boolean isLiked) {
        // Update the 'isLiked' field in the 'flights' node in Firebase
        if (flightsRef != null) {
            DatabaseReference flightRef = flightsRef.child(flightId);
            flightRef.child("isLiked").setValue(isLiked);
        }
    }

    void handleWishlistAction(Flight flight, boolean isLiked) {
        // Add or remove flight from wishlist based on the like state
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String loggedUserId = user.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(loggedUserId).child("flightsWishlist");
            String flightId = databaseReference.push().getKey();

            // Create a new Flight object with relevant details
            Flight newFlight = new Flight(flight.getFlightId(), flight.getFlightName(), flight.getDepartureCity(), flight.getDestinationCity(), flight.getFlightPrice(), flight.isLiked(),flight.getDepartureTime(),flight.getDestinationTime(),flight.getFlightImage());

            // Add or remove flight from wishlist based on the like state
            if (isLiked) {
                databaseReference.child(flightId).setValue(newFlight);
            } else {
                Query query = databaseReference.orderByChild("flightName").equalTo(flight.getFlightName());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot flightSnapshot : dataSnapshot.getChildren()) {
                            String flightPushId = flightSnapshot.getKey();
                            if (flightPushId != null) {
                                // Remove flight by its push ID
                                databaseReference.child(flightPushId).removeValue();
                                // You can break the loop if you assume that flight names are unique
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle error
                    }
                });
            }
        } else {
            Log.d("FlightCardAdapter", "Please Login to add to wishlist");
        }
    }
}
