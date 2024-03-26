package com.example.makemytrip;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class BookedHotelCardAdapter extends RecyclerView.Adapter<BookedHotelCardAdapter.ViewHolder> {

    private List<HotelBookedInfo> bookedHotels;
    private Context context;
    private FirebaseAuth mAuth;

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
//        TextView numberOfDaysTextView;
        TextView numberOfRoomsTextView;
        TextView startingDateTextView;
        TextView endingDateTextView;
        Button cancelBookingButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelImageView = itemView.findViewById(R.id.hotelImageView);
            hotelNameTextView = itemView.findViewById(R.id.hotelNameTextView);
            hotelAddressTextView = itemView.findViewById(R.id.hotelAddressTextView);
//            numberOfDaysTextView = itemView.findViewById(R.id.numberOfDaysTextView);
            numberOfRoomsTextView = itemView.findViewById(R.id.numberOfRoomsTextView);
            startingDateTextView = itemView.findViewById(R.id.startingDateTextView);
            endingDateTextView = itemView.findViewById(R.id.endingDateTextView);
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
        //holder.hotelImageView.setImageResource(R.drawable.calendar);
        //Picasso.get().load(bookedHotel.getImageUrl()).into(holder.hotelImageView);

        if (bookedHotel.getImageUrl() != null && !bookedHotel.getImageUrl().isEmpty()) {
            // Load image using Picasso
            Picasso.get()
                    .load(bookedHotel.getImageUrl()).into(holder.hotelImageView);
        } else {
            // Handle the case where the image URL is empty or null
            // You can set a default image or hide the ImageView
            holder.hotelImageView.setImageResource(R.drawable.calendarimg);
            Log.i(TAG, "onBindViewHolder: image url is null : "+bookedHotel.getImageUrl());
            // or holder.hotelImage.setVisibility(View.GONE);
        }

        holder.hotelNameTextView.setText(bookedHotel.getHotelName());
        holder.hotelAddressTextView.setText(bookedHotel.getHotelAddress());
//        holder.numberOfDaysTextView.setText("Number of Days: " + bookedHotel.getNumberOfDays());
        holder.numberOfRoomsTextView.setText("Number of Rooms: " + bookedHotel.getNumberOfRooms());
        holder.startingDateTextView.setText("Starting Date: " + bookedHotel.getStartingDate());
        holder.endingDateTextView.setText("Ending Date: " + bookedHotel.getEndingDate());

        // Set a click listener for the cancel booking button
        holder.cancelBookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showConfirmationDialog(bookedHotel.getHotelName());
            }
        });
    }

    @Override
    public int getItemCount() {
        // Return the size of the bookedHotels list
        return bookedHotels.size();
    }

    private void showPaymentTransferDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Payment Transfer");
        builder.setMessage("Your payment will be transferred back to you in a few days.");
        builder.setPositiveButton("OK", null);
        builder.show();
    }
    private void showConfirmationDialog(String hotelName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Cancel Booking");
        builder.setMessage("Are you sure you want to cancel this booking?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User confirmed, proceed to cancel booking

                cancelBooking(hotelName);
                showPaymentTransferDialog();
            }
        });


        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User canceled, do nothing
            }
        });

        builder.show();
    }

    private void cancelBooking(String hotelName) {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {

            String LoggedUserId = user.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(LoggedUserId).child("bookedHotels");
            Query query = databaseReference.orderByChild("hotelName").equalTo(hotelName);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot bookedHotelSnapshot : dataSnapshot.getChildren()) {
                        // Assuming 'bookedHotelsRef' is DatabaseReference to the booked hotels node
                        bookedHotelSnapshot.getRef().removeValue();
                        // You can break the loop if you assume that hotel names are unique
                        break;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });

            // Notify the adapter about the removal so that it updates the UI
            notifyDataSetChanged();
        }
        else{
            Toast.makeText(context, "Please Login to cancel booking", Toast.LENGTH_SHORT).show();
        }
    }


}
