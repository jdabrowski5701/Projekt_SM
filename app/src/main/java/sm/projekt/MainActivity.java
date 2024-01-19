package sm.projekt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import java.util.Locale;


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

        String[] options = getResources().getStringArray(R.array.settings_options);
        String[] icons = getResources().getStringArray(R.array.settings_icons);

        builder.setAdapter(new SettingsAdapter(this, options, icons), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int option) {
                switch (option) {
                    case 0:
                        setLocale("pl");
                        break;
                    case 1:
                        setLocale("en");
                        break;
                    case 2:
                        finish();
                        break;
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        layoutParams.width = (int) getResources().getDimension(R.dimen.dialog_width);
        layoutParams.height = (int) getResources().getDimension(R.dimen.dialog_height);
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

        int orientation = getResources().getConfiguration().orientation;

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
        Animation buttonAnimation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Obsługa orientacji poziomej (landscape)

            RelativeLayout.LayoutParams logoParams = (RelativeLayout.LayoutParams) logoImageView.getLayoutParams();
            logoParams.setMargins(0, 50, 0, 0);
            logoImageView.setLayoutParams(logoParams);

            logoImageView.startAnimation(logoAnimation);
            buttonAnimation = AnimationUtils.loadAnimation(this, R.anim.landscape_button_slide_up);
            setStartOffsetForButton(firstButton, 0);
            setStartOffsetForButton(secondButton, 300);
            setStartOffsetForButton(thirdButton, 600);
            setStartOffsetForButton(lastButton, 900);
        } else {
            // Obsługa orientacji pionowej
            buttonAnimation = AnimationUtils.loadAnimation(this, R.anim.button_slide_up);
        }
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
    private void setStartOffsetForButton(Button button, int startOffset) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) button.getLayoutParams();
        params.setMargins(startOffset, 0, 0, 0);
        button.setLayoutParams(params);
    }

    private void setLocale(String languageCode) {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putString("language", languageCode)
                .apply();

        updateLocale(languageCode);
        recreate();
    }
    private void updateLocale(String languageCode) {
        Locale newLocale = new Locale(languageCode);
        Locale.setDefault(newLocale);

        Resources resources = getResources();
        Configuration configuration = new Configuration(resources.getConfiguration());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(newLocale);
        } else {
            configuration.locale = newLocale;
        }

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

    }
}
