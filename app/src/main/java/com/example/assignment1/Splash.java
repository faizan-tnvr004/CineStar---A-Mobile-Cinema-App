package com.example.assignment1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splash extends AppCompatActivity {
    ImageView logo;
    Animation logo_anim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        applyAnimation();
        moveToNext();
    }
    private void applyAnimation(){
        logo.setAnimation(logo_anim);

    }
    private void moveToNext(){
        new Handler().postDelayed(()->{
            // Check if user is already logged in
            SharedPreferences prefs = getSharedPreferences("cinefast_session_pref_v3", MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

            if (isLoggedIn) {
                // Skip login, go directly to MainActivity
                startActivity(new Intent(this, MainActivity.class));
            } else {
                // Go to onboarding
                startActivity(new Intent(this, onboarding.class));
            }
            finish();

        }, 5000);
    }
    private void init(){
        logo_anim = AnimationUtils.loadAnimation(this, R.anim.logo_anim);
        logo = findViewById(R.id.logo);



    }
}


