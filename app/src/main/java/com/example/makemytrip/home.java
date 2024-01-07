package com.example.makemytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class home extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerTogg1e;
    LinearLayout hotel_tab;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerTogg1e.onOptionsItemSelected(item)) {

            return true;
        }


        return super.onOptionsItemSelected(item);
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
                } else if (itemId == R.id.language) {
                    Log.i("MENU_DRAWER_TAG", "Language is clicked");
                } else if (itemId == R.id.country) {
                    Log.i("MENU_DRAWER_TAG", "Country is clicked");
                } else if (itemId == R.id.logout) {
                    Log.i("MENU_DRAWER_TAG", "Logout is clicked");
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
                Toast.makeText(home.this, "clicked hotles", Toast.LENGTH_SHORT).show();
            }
        });

    }



}