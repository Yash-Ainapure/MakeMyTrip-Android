package com.example.makemytrip;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;
        import java.util.List;

public class HolidayPackageDealsAdapter extends RecyclerView.Adapter<HolidayPackageDealsAdapter.ViewHolder> {

    private List<HolidayPackageInfo> hotelPackages;
    private Context context;

    public HolidayPackageDealsAdapter(Context context, List<HolidayPackageInfo> hotelPackages) {
        this.context = context;
        this.hotelPackages = hotelPackages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.package_card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HolidayPackageInfo hotelPackage = hotelPackages.get(position);

        // Bind data to the ViewHolder
        holder.tvPackageName.setText(hotelPackage.getPackageName());
        holder.tvPackageDetails.setText(String.valueOf(hotelPackage.getPackageDetails().replace("\\n", "\n")));
        holder.tvPackageDuration.setText(hotelPackage.getPackageDuration());
        holder.tvPrice.setText("₹ " +String.valueOf(hotelPackage.getPrice()));
        holder.enquireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: Enquire button clicked");
                // Open the enquire dialog
                // Create a new dialog
                AlertDialog.Builder builderDialog = new AlertDialog.Builder(context);
                // Set the custom layout
                View view = LayoutInflater.from(context).inflate(R.layout.enquire, null);
               Button okButton = view.findViewById(R.id.ok);
                builderDialog.setView(view);
                // Create the dialog
                AlertDialog dialog = builderDialog.create();
                // Show the dialog
                dialog.show();
                okButton.setOnClickListener(view1 -> {
                    holder.enquireButton.setText("Enquired");
                    holder.enquireButton.setEnabled(false);
                    dialog.dismiss();
                });
            }
        }
        );

    }

    @Override
    public int getItemCount() {
        return hotelPackages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPackageName;
        TextView tvPackageDetails;
        TextView tvPackageDuration;
        TextView tvPrice;
        Button enquireButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPackageName = itemView.findViewById(R.id.textViewHotelName);
            tvPackageDetails = itemView.findViewById(R.id.textViewHotelAddress);
            tvPackageDuration = itemView.findViewById(R.id.tvDuration);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            enquireButton = itemView.findViewById(R.id.enqbtn);
        }
    }
}

