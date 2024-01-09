//package com.example.makemytrip;
//
//public class WishlistCardAdapter {
//
//}
//


package com.example.makemytrip;

import android.content.Intent;
import android.util.Log;
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

import java.util.List;

public class WishlistCardAdapter extends RecyclerView.Adapter<WishlistCardAdapter.WishlistViewHolder> {
    private List<Hotel> wishlist;
    private DatabaseReference hotelsRef;
    private FirebaseAuth mAuth;

    public WishlistCardAdapter(List<Hotel> wishlist) {
        this.wishlist = wishlist;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.hotelsRef = databaseReference;
    }

    public void setWishlist(List<Hotel> wishlist) {
        this.wishlist = wishlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_card_layout, parent, false);
        return new WishlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        Hotel hotel = wishlist.get(position);
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

                Log.i("MENU_DRAWER_TAG", "liked btn state : "+newState);
                // Update the isLiked field in Firebase
                updateIsLikedInFirebase(hotel.getId(), newState);

                //add liked hotel to wishlist
                if(newState){
                    mAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user!=null){
                        String LoggedUserId = user.getUid();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(LoggedUserId).child("hotelsWishlist");
                        String UserId=databaseReference.push().getKey();
                        Hotel newHotel=new Hotel(hotel.getId(),hotel.getName(),hotel.getAddress(),hotel.getImageUrl(),hotel.isLiked());
                        databaseReference.child(UserId).setValue(newHotel);
                    }
                    else{
                        Toast.makeText(v.getContext(), "Please Login to add to wishlist", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //remove hotel from wishlist
                    mAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user!=null){
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

                        //make isLiked value to false
                        DatabaseReference hotelRef = FirebaseDatabase.getInstance().getReference().child("hotels");
                        Query query1=hotelRef.orderByChild("name").equalTo(hotel.getName());
                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot hotelSnapshot : dataSnapshot.getChildren()) {
                                    String hotelPushId = hotelSnapshot.getKey();
                                    if (hotelPushId != null) {
                                        // Remove hotel by its push ID
                                        hotelRef.child(hotelPushId).child("isLiked").setValue(false);
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
                    }
                    else{
                        Toast.makeText(v.getContext(), "Please Login to add to wishlist", Toast.LENGTH_SHORT).show();
                    }
                }

                // Update the local wishlist
                hotel.setLiked(newState);

                // Set the appropriate drawable for the like button
                int drawableRes = newState ? R.drawable.ic_heart_selected : R.drawable.ic_heart_unselected;
                holder.likeButton.setButtonDrawable(drawableRes);

                // Notify adapter of data change
                notifyItemChanged(holder.getAdapterPosition());
            }
        });

        // Set a click listener for the entire card to handle other actions
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event - Display name, address, and imageUrl in a toast
                Toast.makeText(v.getContext(), "Name: " + hotel.getName() + "\nAddress: " + hotel.getAddress() + "\nImageUrl: " + hotel.getImageUrl(), Toast.LENGTH_SHORT).show();

                // Handle click event - Open HotelDetailActivity with hotel details
                 Intent intent = new Intent(v.getContext(), HotelDetailActivity.class);
                 intent.putExtra("hotel", hotel);
                 v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wishlist.size();
    }

    public class WishlistViewHolder extends RecyclerView.ViewHolder {
        private ImageView hotelImage;
        private TextView hotelName;
        private TextView hotelAddress;
        private ToggleButton likeButton;

        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelImage = itemView.findViewById(R.id.imageViewHotel);
            hotelName = itemView.findViewById(R.id.textViewHotelName);
            hotelAddress = itemView.findViewById(R.id.textViewHotelAddress);
            likeButton = itemView.findViewById(R.id.likeButton);
        }

        public void bindData(Hotel hotel) {
            // Bind data to UI elements
            hotelName.setText(hotel.getName());
            hotelAddress.setText(hotel.getAddress());

            // Load image using a library like Picasso or Glide
            // Example using Picasso:
            // Picasso.get().load(hotel.getImageUrl()).into(hotelImage);
        }
    }

    void updateIsLikedInFirebase(String hotelId, boolean isLiked) {
        // Update the 'isLiked' field in the 'hotels' node in Firebase
        if (hotelsRef != null) {
            DatabaseReference hotelRef = hotelsRef.child(hotelId);
            hotelRef.child("isLiked").setValue(isLiked);
        }
    }
}
