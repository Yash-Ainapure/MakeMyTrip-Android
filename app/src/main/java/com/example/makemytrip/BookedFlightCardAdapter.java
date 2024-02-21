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

public class BookedFlightCardAdapter extends  RecyclerView.Adapter<BookedFlightCardAdapter.ViewHolder>{

    private List<Flight> bookedFlights;
    private Context context;
    private FirebaseAuth mAuth;

    // Constructor to initialize the adapter with a list of booked hotels
    public BookedFlightCardAdapter(Context context, List<Flight> bookedFlights) {
        this.context = context;
        this.bookedFlights = bookedFlights;
    }

    // ViewHolder class representing each item in the RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView FlightNameTextView;
        TextView FromCityTextView;
        TextView ToCityTextView;
        TextView DepartureDateTextView;
        TextView DestinationDateTextView;
        Button cancelBookingButton;
         ImageView flightImage;
         TextView destairport,depairport,fltduration;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            FlightNameTextView = itemView.findViewById(R.id.flightNameTextView);
            FromCityTextView = itemView.findViewById(R.id.fromCity);
            ToCityTextView = itemView.findViewById(R.id.ToCity);
            DepartureDateTextView = itemView.findViewById(R.id.departureDateTextView);
            DestinationDateTextView = itemView.findViewById(R.id.destinationDateTextView);
            cancelBookingButton = itemView.findViewById(R.id.cancelBookingButton);
            flightImage = itemView.findViewById(R.id.imageViewCompanyLogo);
            destairport = itemView.findViewById(R.id.destairport);
            depairport = itemView.findViewById(R.id.depairport);
            fltduration = itemView.findViewById(R.id.fltduration);
        }
    }

    @NonNull
    @Override
    public BookedFlightCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booked_flight_card_layout, parent, false);
        return new BookedFlightCardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookedFlightCardAdapter.ViewHolder holder, int position) {
        // Bind the data to the ViewHolder's views
        Flight flight = bookedFlights.get(position);


        holder.FlightNameTextView.setText(flight.getFlightName());
        holder.FromCityTextView.setText("From: " + flight.getDepartureCity());
        holder.ToCityTextView.setText("To: " + flight.getDestinationCity());
        holder.DepartureDateTextView.setText(" " + flight.getDepartureTime());
        holder.DestinationDateTextView.setText(" " + flight.getDestinationTime());
        Picasso.get().load(flight.getFlightImage()).into(holder.flightImage);
        holder.destairport.setText(flight.getDestinationAirport());
        holder.depairport.setText(flight.getDepartureAirport());
        String departureTime = flight.getDepartureTime();
        String destinationTime = flight.getDestinationTime();

        String duration = calculateDuration(departureTime, destinationTime);
        holder.fltduration.setText(duration);

        // Set a click listener for the cancel booking button
        holder.cancelBookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showConfirmationDialog(flight.getFlightName());
            }
        });
    }
    public String calculateDuration(String departureTime, String destinationTime) {
        // Parse the departure and destination times
        String[] depTimeParts = departureTime.split(":");
        String[] destTimeParts = destinationTime.split(":");

        int depHours = Integer.parseInt(depTimeParts[0]);
        int depMinutes = Integer.parseInt(depTimeParts[1]);

        int destHours = Integer.parseInt(destTimeParts[0]);
        int destMinutes = Integer.parseInt(destTimeParts[1]);

        // Calculate the duration
        int durationHours = destHours - depHours;
        int durationMinutes = destMinutes - depMinutes;

        // Handle cases where the destination time is earlier than the departure time
        if (durationHours < 0) {
            durationHours += 24; // Add 24 hours to handle the next day
        }

        if (durationMinutes < 0) {
            durationMinutes += 60; // Add 60 minutes to handle borrowing from the next hour
            durationHours--; // Subtract 1 hour
        }

        // Format the duration as a string
        String durationString = String.format("%d hrs %d min", durationHours, durationMinutes);

        return durationString;
    }
    @Override
    public int getItemCount() {
        // Return the size of the bookedHotels list
        return bookedFlights.size();
    }

    private void showConfirmationDialog(String flightName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Cancel Booking");
        builder.setMessage("Are you sure you want to cancel this booking?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User confirmed, proceed to cancel booking
                cancelBooking(flightName);
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

    private void cancelBooking(String flightName) {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {

            String LoggedUserId = user.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(LoggedUserId).child("bookedFlights");
            Query query = databaseReference.orderByChild("airline").equalTo(flightName);
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
