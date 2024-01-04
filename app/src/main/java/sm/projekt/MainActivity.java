package sm.projekt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView logoImageView = findViewById(R.id.logoImageView);
        Button firstButton = findViewById(R.id.FirstButton);
        Button secondButton = findViewById(R.id.SecondButton);
        Button thirdButton = findViewById(R.id.ThirdButton);
        Button lastButton = findViewById(R.id.LastButton);

        // Animacja dla logo
        Animation logoAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        logoImageView.startAnimation(logoAnimation);

        // Animacja dla przycisków
        Animation buttonAnimation = AnimationUtils.loadAnimation(this, R.anim.button_slide_up);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                logoImageView.clearAnimation();
                firstButton.setVisibility(View.VISIBLE);
                firstButton.startAnimation(buttonAnimation);
            }
        }, 1500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                secondButton.setVisibility(View.VISIBLE);
                secondButton.startAnimation(buttonAnimation);
            }
        }, 2400);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                thirdButton.setVisibility(View.VISIBLE);
                thirdButton.startAnimation(buttonAnimation);
            }
        }, 3300);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lastButton.setVisibility(View.VISIBLE);
                lastButton.startAnimation(buttonAnimation);
            }
        }, 4200);
    }
}
