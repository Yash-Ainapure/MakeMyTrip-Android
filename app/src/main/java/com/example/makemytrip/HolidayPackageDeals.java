package com.example.makemytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
    List<HolidayPackageInfo> packageList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_package_deals);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new HolidayPackageDealsAdapter(this, packageList);
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        String state=intent.getStringExtra("state");
        Log.d("state",state);
        if(!state.isEmpty()){
            retrieveHolidayData(state);
            Log.d("data retrivng","retriving data..");
        }else {
            Toast.makeText(this, "error occured:got null state", Toast.LENGTH_SHORT).show();
        }


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
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}