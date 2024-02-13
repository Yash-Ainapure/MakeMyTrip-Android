package com.example.makemytrip;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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

import java.util.Date;
import java.util.List;

public class HotelCardAdapter extends RecyclerView.Adapter<HotelCardAdapter.HotelViewHolder> {
    private List<Hotel> hotelList;
    private DatabaseReference hotelsRef;
    FirebaseAuth mAuth;

    public HotelCardAdapter(List<Hotel> hotelList) {
        this.hotelList = hotelList;
    }

    // Add a method to set the DatabaseReference separately
    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.hotelsRef = databaseReference;
    }

    public void setHotelList(List<Hotel> hotelList) {
        this.hotelList = hotelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_card_layout, parent, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        Hotel hotel = hotelList.get(position);
        holder.bindData(hotel);

        // Set the like button state based on isLiked field
        holder.likeButton.setChecked(hotel.isLiked());

        // Set the appropriate drawable for the like button
        int drawableRes = hotel.isLiked() ? R.drawable.ic_heart_selected : R.drawable.ic_heart_unselected;
        holder.likeButton.setButtonDrawable(drawableRes);

        // Set a click listener for the like button
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean newState = ((ToggleButton) v).isChecked();

                // Update the isLiked field in Firebase
                updateIsLikedInFirebase(hotel.getId(), newState, hotel);

                // Update the local hotelList
                hotel.setLiked(newState);


                // Set the appropriate drawable for the like button
                int drawableRes = newState ? R.drawable.ic_heart_selected : R.drawable.ic_heart_unselected;
                holder.likeButton.setButtonDrawable(drawableRes);

                notifyItemChanged(holder.getAdapterPosition());

                //add liked hotel to wishlist
                if (newState) {
                    mAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        String LoggedUserId = user.getUid();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(LoggedUserId).child("hotelsWishlist");
                        String UserId = databaseReference.push().getKey();
//                        Hotel.Ratings ratings = hotel.getRatings();
                        Hotel newHotel = new Hotel(hotel.getId(), hotel.getName(), hotel.getAddress(), hotel.getImageUrl(), hotel.isLiked(), hotel.getPrice(), hotel.getOtherImages(), hotel.getRating(), hotel.getState(), hotel.getCity());
                        databaseReference.child(UserId).setValue(newHotel);
                    } else {
                        Toast.makeText(v.getContext(), "Please Login to add to wishlist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //remove hotel from wishlist
                    mAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        String LoggedUserId = user.getUid();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(LoggedUserId).child("hotelsWishlist");
                        Query query = databaseReference.orderByChild("name").equalTo(hotel.getName());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot hotelSnapshot : dataSnapshot.getChildren()) {
                                    String hotelPushId = hotelSnapshot.getKey();
                                    if (hotelPushId != null) {
                                        // Remove hotel by its push ID
                                        databaseReference.child(hotelPushId).removeValue();
                                        // You can break the loop if you assume that hotel names are unique
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle error
                            }
                        });
                    } else {
                        Toast.makeText(v.getContext(), "Please Login to add to wishlist", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        // Set a click listener for the entire card to handle other actions
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HotelDetailActivity.class);
                intent.putExtra("hotel", hotel);
                v.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public class HotelViewHolder extends RecyclerView.ViewHolder {
        private ImageView hotelImage;
        private TextView hotelName;
        private TextView hotelAddress;
        private ToggleButton likeButton;
        private TextView hotelPrice;

        private TextView hotelRating;

        private TextView ratinginwords;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelImage = itemView.findViewById(R.id.imageViewHotel);
            hotelName = itemView.findViewById(R.id.textViewHotelName);
            hotelAddress = itemView.findViewById(R.id.textViewHotelAddress);
            likeButton = itemView.findViewById(R.id.likeButton);
            hotelPrice = itemView.findViewById(R.id.hotelPrice);
            hotelRating = itemView.findViewById(R.id.hotelRating);
            ratinginwords = itemView.findViewById(R.id.ratinginwords);

        }

        public void bindData(Hotel hotel) {
            // Bind data to UI elements
            hotelName.setText(hotel.getName());
            hotelAddress.setText(hotel.getAddress());

            //here hotel.getPrice() returns int which then is converted to string format
            hotelPrice.setText(String.valueOf(hotel.getPrice()));



            // Example using Picasso:
            Picasso.get().load(hotel.getImageUrl()).into(hotelImage);


            DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                    .getReference("countries").child("India")
                    .child("states").child(hotel.getState())
                    .child("cities")
                    .child(hotel.getCity())
                    .child("hotels")
                    .child(hotel.getId())
                    .child("ratings");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Ratings ratings = snapshot.getValue(Ratings.class);
                    if (ratings != null) {
//                       Toast.makeText(HotelDetailActivity.this, "Ratings" + ratings.getAverageRating(), Toast.LENGTH_SHORT).show();

                        hotelRating.setText(String.valueOf(ratings.getAverageRating()));
                        ratinginwords.setText(ratings.setRatinginwords());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
        void updateIsLikedInFirebase(String hotelId, boolean isLiked, Hotel hotel) {
            // Update the 'isLiked' field in the 'hotels' node in Firebase
            //hotelsRef.child(hotelId).child("isLiked").setValue(isLiked);
            if (hotelsRef != null) {
                DatabaseReference hotelRef = hotelsRef.child(hotel.getState()).child("cities").child(hotel.getCity()).child("hotels").child(hotelId);
                hotelRef.child("isLiked").setValue(isLiked);
            }
        }
    }


