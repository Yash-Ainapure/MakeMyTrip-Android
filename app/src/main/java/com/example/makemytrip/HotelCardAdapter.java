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

import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class HotelCardAdapter extends RecyclerView.Adapter<HotelCardAdapter.HotelViewHolder> {
    private List<Hotel> hotelList;
    private DatabaseReference hotelsRef;

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
                updateIsLikedInFirebase(hotel.getId(), newState);

                // Update the local hotelList
                hotel.setLiked(newState);

                // Update the UI
                //holder.likeButton.setChecked(newState);

                // Set the appropriate drawable for the like button
                int drawableRes = newState ? R.drawable.ic_heart_selected : R.drawable.ic_heart_unselected;
                holder.likeButton.setButtonDrawable(drawableRes);

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
        return hotelList.size();
    }

    public class HotelViewHolder extends RecyclerView.ViewHolder {
        private ImageView hotelImage;
        private TextView hotelName;
        private TextView hotelAddress;
        private ToggleButton likeButton;

        public HotelViewHolder(@NonNull View itemView) {
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
        //hotelsRef.child(hotelId).child("isLiked").setValue(isLiked);
        if (hotelsRef != null) {
            DatabaseReference hotelRef = hotelsRef.child(hotelId);
            hotelRef.child("isLiked").setValue(isLiked);
        }
    }
}
