package com.example.makemytrip;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HotelCardAdapter extends RecyclerView.Adapter<HotelCardAdapter.HotelViewHolder> {
    private List<Hotel> hotelList;


    public HotelCardAdapter(List<Hotel> hotelList) {
        this.hotelList = hotelList;
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

        //onclick listerner to get specific hotel cards information
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

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelImage = itemView.findViewById(R.id.imageViewHotel);
            hotelName = itemView.findViewById(R.id.textViewHotelName);
            hotelAddress = itemView.findViewById(R.id.textViewHotelAddress);
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

}

