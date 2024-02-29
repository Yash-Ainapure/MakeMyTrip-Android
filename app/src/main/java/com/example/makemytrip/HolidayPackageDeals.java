package com.example.makemytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HolidayPackageDeals extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private HolidayPackageDealsAdapter adapter;
    private Button enquireButton;
    List<HolidayPackageInfo> packageList=new ArrayList<>();
    AlertDialog.Builder builderDialog;
    AlertDialog dialog;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // Handle the back button
            case android.R.id.home:
                onBackPressed(); // This will call the default back button behavior
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_package_deals);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new HolidayPackageDealsAdapter(this, packageList);

        recyclerView.setAdapter(adapter);
        ActionBar actionBar = getSupportActionBar();

        Intent intent = getIntent();
        String state=intent.getStringExtra("state");
        Log.d("state",state);
        if(!state.isEmpty()){
            retrieveHolidayData(state);
            Log.d("data retrivng","retriving data..");
        }else {
            Toast.makeText(this, "error occured:got null state", Toast.LENGTH_SHORT).show();
        }
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Set your desired icon for the navigation drawer toggle
            getSupportActionBar().setTitle(state +" Packages");      }

    }

    private void retrieveHolidayData(String stateName) {
        DatabaseReference holidayReference = databaseReference.child("holiday").child(stateName);

        holidayReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot holidaySnapshot : dataSnapshot.getChildren()) {
                    HolidayPackageInfo packageObj = holidaySnapshot.getValue(HolidayPackageInfo.class);

                    // Do something with yourObject, e.g., display in UI
                    if (packageObj != null) {

                        packageList.add(packageObj);
                        Log.d("package info","name "+packageObj.getPackageName()+" added to list");
                    }
                }
                adapter.notifyDataSetChanged();

                LottieAnimationView animationView = findViewById(R.id.animationView);
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                TextView emptyText = findViewById(R.id.nothing_found);
                TextView emptyHotelSubText = findViewById(R.id.description2);


                if (packageList.isEmpty()) {
                    animationView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    emptyText.setVisibility(View.VISIBLE);
                    emptyHotelSubText.setVisibility(View.VISIBLE);
                } else {
                    animationView.setVisibility(View.GONE);
                    emptyText.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyHotelSubText.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}