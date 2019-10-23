package com.team11.animation_challenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button getStarted;
    Animation fromBottom;
    Animation popEntrance;
    ImageView welcomeImage;
    TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        popEntrance = AnimationUtils.loadAnimation(this, R.anim.pop_entrance);
        popEntrance.setDuration(1000);

        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom);
        fromBottom.setDuration(1000);

        welcomeImage = findViewById(R.id.welcome_image);
        welcomeImage.setAnimation(popEntrance);
        description = findViewById(R.id.description);
        description.setAnimation(fromBottom);
        getStarted = findViewById(R.id.choose);
        getStarted.setAnimation(fromBottom);

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent triviaIntent = new Intent(view.getContext(), CategoryActivity.class);
                startActivity(triviaIntent);
            }
        });
    }
}
