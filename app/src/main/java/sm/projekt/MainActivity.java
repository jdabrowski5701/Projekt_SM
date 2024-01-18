package sm.projekt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;


public class MainActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setTitle("");
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        MenuItem settingsItem = menu.findItem(R.id.action_settings);

        settingsItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showSettingsDialog();
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ustawienia");

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        layoutParams.width = (int) getResources().getDimension(R.dimen.dialog_width); // Ustaw szerokość na 300dp
        layoutParams.height = (int) getResources().getDimension(R.dimen.dialog_height); // Ustaw wysokość na 500dp
        alertDialog.getWindow().setAttributes(layoutParams);


        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(30);
        gradientDrawable.setColor(getResources().getColor(android.R.color.background_light));

        alertDialog.getWindow().setBackgroundDrawable(gradientDrawable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView logoImageView = findViewById(R.id.logoImageView);
        Button firstButton = findViewById(R.id.NewFlashcardButton);
        Button secondButton = findViewById(R.id.NewTestButton);
        Button thirdButton = findViewById(R.id.ProgressButton);
        Button lastButton = findViewById(R.id.NotesButton);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddFlashcardActivity.class);
                startActivity(intent);
            }
        });

        secondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewFlashcardsActivity.class);
                startActivity(intent);
            }
        });
    }
}
