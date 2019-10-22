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

    public Button chooseButton;
    Animation frombottom;
    Animation fromtop;
    ImageView welcomScreenImage;
    TextView welcomeMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fromtop = AnimationUtils.loadAnimation(this, R.anim.from_top);
        fromtop.setDuration(1000);

        frombottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom);
        frombottom.setDuration(1000);

        welcomScreenImage = findViewById(R.id.imageView);
        welcomScreenImage.setAnimation(fromtop);

        welcomeMessage = findViewById(R.id.textView);
        welcomeMessage.setAnimation(fromtop);

        chooseButton = findViewById(R.id.choose);
        chooseButton.setAnimation(frombottom);

        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent triviaIntent = new Intent(view.getContext(), CategoryActivity.class);
                startActivity(triviaIntent);
            }
        });
    }
}
