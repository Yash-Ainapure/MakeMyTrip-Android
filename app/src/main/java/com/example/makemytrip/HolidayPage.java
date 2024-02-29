package com.example.makemytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HolidayPage extends AppCompatActivity {
    ImageView package1IV,odishaPackage,bhutanPackage,bhutanPackage2,azerbijanPackage,goaPackage2;

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
        setContentView(R.layout.activity_holiday_page);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Set your desired icon for the navigation drawer toggle
            getSupportActionBar().setTitle("Holiday Packages");      }



        package1IV = findViewById(R.id.imageView8);
        odishaPackage = findViewById(R.id.imageView12);
        bhutanPackage = findViewById(R.id.imageView11);
        azerbijanPackage = findViewById(R.id.imageView10);
        goaPackage2=findViewById(R.id.imageView64);
        bhutanPackage2=findViewById(R.id.imageView18);
        LinearLayout parentLinearLayout = findViewById(R.id.parentLayout);

        for (int i = 0; i < parentLinearLayout.getChildCount(); i++) {
            View childLayout = parentLinearLayout.getChildAt(i);

            // Assuming child layout is LinearLayout, you can change the type accordingly
            if (childLayout instanceof LinearLayout) {
                // Assuming your TextView is the second child of the LinearLayout
                TextView textView = (TextView) ((LinearLayout) childLayout).getChildAt(1);

                //final int clickedIndex = i;
                final String textFromTextView = textView.getText().toString();

                childLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(HolidayPage.this, "clicked : "+textFromTextView, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(HolidayPage.this,HolidayPackageDeals.class);
                        intent.putExtra("state",textFromTextView);
                        startActivity(intent);
                    }
                });
            }
        }










        package1IV.setOnClickListener(v -> {

            List<String> otherImages = new ArrayList<>();
            otherImages.add("https://r1imghtlak.mmtcdn.com/5b9c18a8ba7511e8afbd0224510f5e5b.jpg?&output-quality=75&output-format=jpg&downsize=501:280&crop=501:280");
            otherImages.add("https://q-xx.bstatic.com/xdata/images/hotel/max1024x768/30936988.jpg?k=1561015ce2595b4c57efc4bc2cf5b1fec88a597aeb5dd6d23cd0daf6fe929270&o=");

            Hotel hotel = new Hotel();
            hotel.setId("123");
            hotel.setName("Country Inn Panjim Goa");
            hotel.setAddress("Panji,Goa");
            hotel.setImageUrl("https://pix8.agoda.net/hotelImages/487/487118/487118_14022821390018511893.jpg?ca=2&ce=1&s=450x450");
            hotel.setIsLiked(false);
            hotel.setPrice(5000);
            hotel.setOtherImages(otherImages);
            hotel.setRating(4.3f);
            hotel.setState("Goa");
            hotel.setCity("Panji");
            hotel.setLocationUrl("https://www.google.com/maps/d/u/0/viewer?mid=1zrZgjgGbRn9ug0Gy0WKx2lo_V5c&hl=en_US&ll=15.49707700000001%2C73.834981&z=17");

            Intent intent = new Intent(HolidayPage.this, HotelDetailActivity.class);
            intent.putExtra("hotel", hotel);
            startActivity(intent);
        });
        goaPackage2.setOnClickListener(v -> {

            List<String> otherImages = new ArrayList<>();
            otherImages.add("https://r1imghtlak.mmtcdn.com/5b9c18a8ba7511e8afbd0224510f5e5b.jpg?&output-quality=75&output-format=jpg&downsize=501:280&crop=501:280");
            otherImages.add("https://q-xx.bstatic.com/xdata/images/hotel/max1024x768/30936988.jpg?k=1561015ce2595b4c57efc4bc2cf5b1fec88a597aeb5dd6d23cd0daf6fe929270&o=");

            Hotel hotel = new Hotel();
            hotel.setId("123");
            hotel.setName("Country Inn Panjim Goa");
            hotel.setAddress("Panji,Goa");
            hotel.setImageUrl("https://pix8.agoda.net/hotelImages/487/487118/487118_14022821390018511893.jpg?ca=2&ce=1&s=450x450");
            hotel.setIsLiked(false);
            hotel.setPrice(5000);
            hotel.setOtherImages(otherImages);
            hotel.setRating(4.3f);
            hotel.setState("Goa");
            hotel.setCity("Panji");
            hotel.setLocationUrl("https://www.google.com/maps/d/u/0/viewer?mid=1zrZgjgGbRn9ug0Gy0WKx2lo_V5c&hl=en_US&ll=15.49707700000001%2C73.834981&z=17");

            Intent intent = new Intent(HolidayPage.this, HotelDetailActivity.class);
            intent.putExtra("hotel", hotel);
            startActivity(intent);
        });
        odishaPackage.setOnClickListener(v -> {

            List<String> otherImages = new ArrayList<>();
            otherImages.add("https://dynamic-media-cdn.tripadvisor.com/media/photo-o/29/5a/58/84/hotel-hindusthan-international.jpg?w=1200&h=-1&s=1");
            otherImages.add("https://dynamic-media-cdn.tripadvisor.com/media/photo-o/0e/78/18/ac/presidential-suite.jpg?w=300&h=-1&s=1");

            Hotel hotel = new Hotel();
            hotel.setId("345");
            hotel.setName("Hindusthan International Bhubaneswar");
            hotel.setAddress("A - 112 Unit Iii Shriya Talkies Street Kharvel Nagar, Bhubaneswar 751001 India");
            hotel.setImageUrl("https://dynamic-media-cdn.tripadvisor.com/media/photo-o/29/5a/58/78/hotel-hindusthan-international.jpg?w=300&h=300&s=1");
            hotel.setIsLiked(false);
            hotel.setPrice(7000);
            hotel.setOtherImages(otherImages);
            hotel.setRating(4.5f);
            hotel.setState("Odisha");
            hotel.setCity("Bhubaneswar");
            hotel.setLocationUrl("https://www.google.com/maps/search/odisha+bhubaneswar+hindustan+hotel+maps/@20.2862524,85.7843574,13z?entry=ttu");

            Intent intent = new Intent(HolidayPage.this, HotelDetailActivity.class);
            intent.putExtra("hotel", hotel);
            startActivity(intent);
        });
        bhutanPackage.setOnClickListener(v -> {

            List<String> otherImages = new ArrayList<>();
            otherImages.add("https://lh5.googleusercontent.com/p/AF1QipOH98AZ0Y_rL2lRhn-3OjXppY_Wzs8z_dBn3tb8=w253-h168-k-no");
            otherImages.add("https://lh5.googleusercontent.com/p/AF1QipMccBGsDptuhhRerWHmc0o8jCU0S-9VSt_oEJah=w253-h129-k-no");

            Hotel hotel = new Hotel();
            hotel.setId("567");
            hotel.setName("Park Hotel Bhutan");
            hotel.setAddress("V96M+Q5P, Tharpai Lam, Phuentsholing, Bhutan");
            hotel.setImageUrl("https://lh5.googleusercontent.com/p/AF1QipNbdeaQMlkf2zs9Mh1RpLNfJt5nZNk_boiIbnoi=w253-h277-k-no");
            hotel.setIsLiked(false);
            hotel.setPrice(7000);
            hotel.setOtherImages(otherImages);
            hotel.setRating(4.5f);
            hotel.setState("Phuentsholing");
            hotel.setCity("Tharpai Lam");
            hotel.setLocationUrl("https://www.google.com/maps/dir//V96M%2BQ5P+Park+Hotel+Bhutan,+Tharpai+Lam,+Phuentsholing,+Bhutan/@26.8618318,89.3008209,12z/data=!3m1!4b1!4m8!4m7!1m0!1m5!1m1!1s0x39e3cb3ec046251b:0x884efa730f08b95b!2m2!1d89.3832224!2d26.8618557?hl=en-GB&entry=ttu");

            Intent intent = new Intent(HolidayPage.this, HotelDetailActivity.class);
            intent.putExtra("hotel", hotel);
            startActivity(intent);
        });
        bhutanPackage2.setOnClickListener(v -> {

            List<String> otherImages = new ArrayList<>();
            otherImages.add("https://lh5.googleusercontent.com/p/AF1QipOH98AZ0Y_rL2lRhn-3OjXppY_Wzs8z_dBn3tb8=w253-h168-k-no");
            otherImages.add("https://lh5.googleusercontent.com/p/AF1QipMccBGsDptuhhRerWHmc0o8jCU0S-9VSt_oEJah=w253-h129-k-no");

            Hotel hotel = new Hotel();
            hotel.setId("567");
            hotel.setName("Park Hotel Bhutan");
            hotel.setAddress("V96M+Q5P, Tharpai Lam, Phuentsholing, Bhutan");
            hotel.setImageUrl("https://lh5.googleusercontent.com/p/AF1QipNbdeaQMlkf2zs9Mh1RpLNfJt5nZNk_boiIbnoi=w253-h277-k-no");
            hotel.setIsLiked(false);
            hotel.setPrice(7000);
            hotel.setOtherImages(otherImages);
            hotel.setRating(4.5f);
            hotel.setState("Phuentsholing");
            hotel.setCity("Tharpai Lam");
            hotel.setLocationUrl("https://www.google.com/maps/dir//V96M%2BQ5P+Park+Hotel+Bhutan,+Tharpai+Lam,+Phuentsholing,+Bhutan/@26.8618318,89.3008209,12z/data=!3m1!4b1!4m8!4m7!1m0!1m5!1m1!1s0x39e3cb3ec046251b:0x884efa730f08b95b!2m2!1d89.3832224!2d26.8618557?hl=en-GB&entry=ttu");

            Intent intent = new Intent(HolidayPage.this, HotelDetailActivity.class);
            intent.putExtra("hotel", hotel);
            startActivity(intent);
        });
        azerbijanPackage.setOnClickListener(v -> {

            List<String> otherImages = new ArrayList<>();
            otherImages.add("https://lh5.googleusercontent.com/p/AF1QipOH98AZ0Y_rL2lRhn-3OjXppY_Wzs8z_dBn3tb8=w253-h168-k-no");
            otherImages.add("https://lh5.googleusercontent.com/p/AF1QipMccBGsDptuhhRerWHmc0o8jCU0S-9VSt_oEJah=w253-h129-k-no");

            Hotel hotel = new Hotel();
            hotel.setId("567");
            hotel.setName("Park Hotel Bhutan");
            hotel.setAddress("V96M+Q5P, Tharpai Lam, Phuentsholing, Bhutan");
            hotel.setImageUrl("https://lh5.googleusercontent.com/p/AF1QipNbdeaQMlkf2zs9Mh1RpLNfJt5nZNk_boiIbnoi=w253-h277-k-no");
            hotel.setIsLiked(false);
            hotel.setPrice(7000);
            hotel.setOtherImages(otherImages);
            hotel.setRating(4.5f);
            hotel.setState("Phuentsholing");
            hotel.setCity("Tharpai Lam");
            hotel.setLocationUrl("https://www.google.com/maps/dir//V96M%2BQ5P+Park+Hotel+Bhutan,+Tharpai+Lam,+Phuentsholing,+Bhutan/@26.8618318,89.3008209,12z/data=!3m1!4b1!4m8!4m7!1m0!1m5!1m1!1s0x39e3cb3ec046251b:0x884efa730f08b95b!2m2!1d89.3832224!2d26.8618557?hl=en-GB&entry=ttu");

            Intent intent = new Intent(HolidayPage.this, HotelDetailActivity.class);
            intent.putExtra("hotel", hotel);
            startActivity(intent);
        });
    }

}