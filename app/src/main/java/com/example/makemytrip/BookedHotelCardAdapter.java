package com.example.makemytrip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BookedHotelCardAdapter extends RecyclerView.Adapter<BookedHotelCardAdapter.ViewHolder> {

    private List<HotelBookedInfo> bookedHotels;
    private Context context;

    // Constructor to initialize the adapter with a list of booked hotels
    public BookedHotelCardAdapter(Context context, List<HotelBookedInfo> bookedHotels) {
        this.context = context;
        this.bookedHotels = bookedHotels;
    }

    // ViewHolder class representing each item in the RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView hotelImageView;
        TextView hotelNameTextView;
        TextView hotelAddressTextView;
        TextView numberOfDaysTextView;
        TextView numberOfRoomsTextView;
        TextView startingDateTextView;
        Button cancelBookingButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelImageView = itemView.findViewById(R.id.hotelImageView);
            hotelNameTextView = itemView.findViewById(R.id.hotelNameTextView);
            hotelAddressTextView = itemView.findViewById(R.id.hotelAddressTextView);
            numberOfDaysTextView = itemView.findViewById(R.id.numberOfDaysTextView);
            numberOfRoomsTextView = itemView.findViewById(R.id.numberOfRoomsTextView);
            startingDateTextView = itemView.findViewById(R.id.startingDateTextView);
            cancelBookingButton = itemView.findViewById(R.id.cancelBookingButton);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booked_hotel_card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind the data to the ViewHolder's views
        HotelBookedInfo bookedHotel = bookedHotels.get(position);

        // Set the hotel image (Replace R.drawable.placeholder_image with your actual image resource)
        holder.hotelImageView.setImageResource(R.drawable.calendar);

        holder.hotelNameTextView.setText(bookedHotel.getHotelName());
        holder.hotelAddressTextView.setText(bookedHotel.getHotelAddress());
        holder.numberOfDaysTextView.setText("Number of Days: " + bookedHotel.getNumberOfDays());
        holder.numberOfRoomsTextView.setText("Number of Rooms: " + bookedHotel.getNumberOfRooms());
        holder.startingDateTextView.setText("Starting Date: " + bookedHotel.getStartingDate());

        // Set a click listener for the cancel booking button
        holder.cancelBookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the cancel booking action here
                // You may want to show a confirmation dialog before canceling the booking
            }
        });
    }

    @Override
    public int getItemCount() {
        // Return the size of the bookedHotels list
        return bookedHotels.size();
    }
}
