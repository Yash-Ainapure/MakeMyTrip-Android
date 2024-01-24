package com.example.makemytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class home extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerTogg1e;
    LinearLayout hotel_tab;
    TextView username;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
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
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        hotel_tab = findViewById(R.id.hotel_tab);

          drawerLayout = findViewById(R.id.drawer_layout);
       navigationView = findViewById(R.id.navigationView);
        actionBarDrawerTogg1e = new ActionBarDrawerToggle(this , drawerLayout , R.string.menu_open, R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerTogg1e);
        actionBarDrawerTogg1e.syncState();
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View headerView = navigationView.getHeaderView(0);
       username=headerView.findViewById(R.id.username);
       //username.setText("yash patil");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        username.setText(user.getEmail());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            Intent intent;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_account) {
                    Log.i("MENU_DRAWER_TAG", "Account is clicked");
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
                    Log.i("MENU_DRAWER_TAG", "Country is clicked");
                } else if (itemId == R.id.logout) {
                    mAuth=FirebaseAuth.getInstance();
                    mAuth.signOut();
                    startActivity(new Intent(home.this,MainActivity.class));
                    Toast.makeText(home.this, "user logged out", Toast.LENGTH_SHORT).show();
                }

                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        hotel_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(home.this, HotelPage.class);
                startActivity(i);
            }
        });

    }



}