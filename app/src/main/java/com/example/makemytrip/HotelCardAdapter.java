package com.example.makemytrip;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

