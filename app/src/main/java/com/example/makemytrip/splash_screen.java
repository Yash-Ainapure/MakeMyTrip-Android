package com.example.makemytrip;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class splash_screen extends AppCompatActivity {

    private static final int SPLASH_SCREEN_DELAY = 6500; // 5 seconds
    private static final int FADE_OUT_DURATION = 1000; // 1 second

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Hide the action bar

        requestWindowFeature(1);
        getWindow().setFlags(WindowManager. LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS) ;
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_splash_screen);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        VideoView videoView = findViewById(R.id.videoView);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.splash_screen_final;
        videoView.setVideoPath(videoPath);
        videoView.start();



        // Post a delayed runnable to start the Home activity after SPLASH_SCREEN_DELAY milliseconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create fade-out animation
                AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
                fadeOut.setDuration(FADE_OUT_DURATION);

                // Apply the animation to the VideoView
                videoView.startAnimation(fadeOut);

                // Set a listener to handle the end of the animation
                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // Do nothing on start
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // Start the Home activity
                        Intent intent = new Intent(splash_screen.this, MainActivity.class);
                        startActivity(intent);

                        // Finish the splash screen activity
                        finish();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // Do nothing on repeat
                    }
                });
            }
        }, SPLASH_SCREEN_DELAY);



    }
}
