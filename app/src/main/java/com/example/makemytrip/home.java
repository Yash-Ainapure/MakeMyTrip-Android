package com.example.makemytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class home extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView profileImage;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerTogg1e;
    LinearLayout hotel_tab, flight_tab;
    TextView username, welcometext, header_phone;
    private DatabaseReference databaseReference;
    String UserId;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerTogg1e.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Ask the user to confirm exit
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Finish the activity and exit the app
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        UserId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        hotel_tab = findViewById(R.id.hotel_tab);
        flight_tab = findViewById(R.id.flight_tab);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        actionBarDrawerTogg1e = new ActionBarDrawerToggle(this, drawerLayout, R.string.menu_open, R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerTogg1e);
        actionBarDrawerTogg1e.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Home");
        View headerView = navigationView.getHeaderView(0);
        username = headerView.findViewById(R.id.username);
        header_phone = headerView.findViewById(R.id.header_phone);
        welcometext = findViewById(R.id.welcometext);
        profileImage = headerView.findViewById(R.id.imageView);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(UserId).child("userInfo");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfo userInfo = snapshot.getValue(UserInfo.class);
                username.setText("Hi, " + userInfo.getFirstName());
                welcometext.setText("Welcome, " + userInfo.getFirstName());
                header_phone.setText(userInfo.getPhoneNumber());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(home.this, "failed to load username", Toast.LENGTH_SHORT).show();
            }
        });


        //set profile image
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("UserProfileImages/" + UserId);
        try {
            File localfile = File.createTempFile("tempfile", ".jpg");
            storageReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    DisplayMetrics dm = new DisplayMetrics();
                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    profileImage.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Toast.makeText(home.this, "failed to load profile image", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            Intent intent;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_account) {
                    startActivity(new Intent(home.this, MyAccount.class));
                } else if (itemId == R.id.view_manage) {
                    Log.i("MENU_DRAWER_TAG", "View/Manage is clicked");
                    intent = new Intent(home.this, ViewManageTrips.class);
                    startActivity(intent);
                } else if (itemId == R.id.wishlist) {
                    Log.i("MENU_DRAWER_TAG", "Wishlist is clicked");
                    intent = new Intent(home.this, Wishlist.class);
                    startActivity(intent);
                } else if (itemId == R.id.language) {
                    Log.i("MENU_DRAWER_TAG", "Language is clicked");
                } else if (itemId == R.id.country) {
                    intent = new Intent(home.this, Country.class);
                    startActivity(intent);
                    Log.i("MENU_DRAWER_TAG", "Country is clicked");
                } else if (itemId == R.id.logout) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(home.this, MainActivity.class));
                    Toast.makeText(home.this, "user logged out", Toast.LENGTH_SHORT).show();
                }

                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        hotel_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(home.this, HotelPage.class);
                startActivity(i);
            }
        });
        flight_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(home.this, FlightPage.class);
                startActivity(i);
            }
        });

    }

    private void loadProfileImage() {

        StorageReference storageReference = FirebaseStorage.getInstance().getReference("UserProfileImages/" + UserId);
        try {
            File localfile = File.createTempFile("tempfile", ".jpg");
            storageReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    DisplayMetrics dm = new DisplayMetrics();
                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    profileImage.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Toast.makeText(home.this, "failed to load profile image", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Call the method to load profile image when the activity resumes
        loadProfileImage();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(UserId).child("userInfo");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfo userInfo = snapshot.getValue(UserInfo.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}