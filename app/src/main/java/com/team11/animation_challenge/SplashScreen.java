package com.team11.animation_challenge;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    Context context;
    ImageView imageViewOne,imageViewTwo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        imageViewOne = findViewById(R.id.imageView2);
        imageViewTwo = findViewById(R.id.imageView3);
        imageViewTwo.setVisibility(View.INVISIBLE);
        Animation fadeinanim = AnimationUtils.loadAnimation(SplashScreen.this,R.anim.fadein);
        imageViewTwo.startAnimation(fadeinanim);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                imageViewTwo.setVisibility(View.VISIBLE);
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
            }
        },2000);
    }
}
