package sm.projekt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import sm.projekt.GameView;

public class GameActivity extends AppCompatActivity {

    private FlashcardRepository flashcardRepository;
    private TextView currentWord;
    private TextView leftWord;
    private TextView rightWord;
    private LiveData<List<Flashcard>> flashcardsCopy;
    private LiveData<List<Flashcard>> flashcardsLiveData;
    private GameView gameView;

    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private ImageView playerIcon;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        showInstructionPopup();

        flashcardRepository = new FlashcardRepository(this.getApplication());
        currentWord = findViewById(R.id.word);
        leftWord = findViewById(R.id.leftWord);
        rightWord = findViewById(R.id.rightWord);

        flashcardsLiveData = flashcardRepository.findAllFlashcards();
        flashcardsCopy = flashcardsLiveData;

        // add sensor handling
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        playerIcon = findViewById(R.id.playerIcon);
        playerIcon.setBackgroundColor(Color.YELLOW);

        loadFlashcards();
        gameView = new GameView(this);

        frameLayout = findViewById(R.id.frameLayout);
        frameLayout.addView(gameView);
        gameView.setOnBallReachEndListener(new GameView.OnBallReachEndListener() {
            @Override
            public void onBallReachEnd() {
                Log.d("icon ", "przed width view " + gameView.getScreenWidth() + "przed height view " + gameView.getScreenHeight() );
                loadNextFlashcards();
                Log.d("icon ", "width view " + gameView.getScreenWidth() + "height view " + gameView.getScreenHeight() );
            }

            public void onCollisionDetected() {
                indicateChoice(true); // or false based on the game logic
            }
        });
    }

    private void showInstructionPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("How to Play"); // Set the title of the popup

        // Inflate the instruction_popup.xml layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.instruction_popup, null);

        // Set the content of the popup to the inflated layout
        builder.setView(dialogView);

        // Add a button to close the popup
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Close the popup
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void loadFlashcards() {
        // Observe the LiveData to get updates when flashcards change
        flashcardsCopy.observe(this, new Observer<List<Flashcard>>() {
            @Override
            public void onChanged(List<Flashcard> flashcards) {
                // Handle the list of flashcards here
                if (flashcards != null && !flashcards.isEmpty()) {
                    // Flashcards are available, you can use them in your game
                    // For example, you can start the game with the first flashcard
                    Flashcard firstFlashcard = flashcards.get(0);

                    currentWord.setText(firstFlashcard.getQuestion());

                    Random random = new Random();
                    int questionNumber = random.nextInt(flashcards.size() - 1)+1;
                    boolean isLeft = random.nextBoolean();
                    if (isLeft) {
                        leftWord.setText(firstFlashcard.getAnswer());
                        rightWord.setText(flashcards.get(questionNumber).getAnswer());
                    } else {
                        rightWord.setText(firstFlashcard.getAnswer());
                        leftWord.setText(flashcards.get(questionNumber).getAnswer());
                    }
                    usedFlashcards.add(firstFlashcard);
                }
            }
        });
    }

    private final SensorEventListener gyroscopeEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // Gyroscope sensor returns angular speeds in rad/s
            float angularSpeedX = event.values[0];
            float angularSpeedY = event.values[1];

            // Use angularSpeedY to move the player icon left or right
            // You may need to adjust the multiplier for sensitivity
            float deltaX = angularSpeedY * SENSITIVITY_MULTIPLIER;

            // Update player icon's position
            float newX = playerIcon.getX() + deltaX;
            // Ensure the player icon doesn't go off the screen
            int rightBound = gameView.getScreenWidth() - playerIcon.getWidth();
            newX = Math.max(0, Math.min(newX, rightBound));

            playerIcon.setX(newX);
            gameView.setPlayerPosition((int)(playerIcon.getX()), (int) playerIcon.getY());
            gameView.setPlayerSize(playerIcon.getWidth());
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // You can ignore this for now
        }
    };

    private static final float SENSITIVITY_MULTIPLIER = 15.0f;

    private List<Flashcard> usedFlashcards = new ArrayList<>();
    private Random random = new Random();
    private int i=5;

    //when the ball falls of for the first time, the left and right word do not change
    private void loadNextFlashcards() {
        List<Flashcard> flashcards = flashcardsLiveData.getValue();
        if (flashcards == null || flashcards.isEmpty()) {
            return;
        }

        Flashcard newCurrentFlashcard = getNextFlashcard(flashcards);
        if (newCurrentFlashcard == null) {
            Toast.makeText(this,"To były wszystie słowa", Toast.LENGTH_SHORT).show();
            return;
        }
        //Flashcard leftFlashcard
        //Random random = new Random();
        int questionNumber = random.nextInt(flashcards.size() - 1)+1;
        boolean isLeft = random.nextBoolean();
        if (isLeft) {
            leftWord.setText(newCurrentFlashcard.getAnswer());
            rightWord.setText(flashcards.get(random.nextInt(flashcards.size())).getAnswer());
        } else {
            rightWord.setText(newCurrentFlashcard.getAnswer());
            leftWord.setText(flashcards.get(random.nextInt(flashcards.size())).getAnswer());
        }
//        Flashcard leftFlashcard = flashcards.get(random.nextInt(flashcards.size()));
//        Flashcard rightFlashcard;
//        do {
//            rightFlashcard = flashcards.get(random.nextInt(flashcards.size()));
//        } while (rightFlashcard.equals(leftFlashcard));

        // Now update the TextViews
        currentWord.setText(newCurrentFlashcard.getQuestion());
//        leftWord.setText(leftFlashcard.getAnswer());
//        rightWord.setText(rightFlashcard.getAnswer());

        // Optionally, you can add the newCurrentFlashcard to the list of used flashcards
        usedFlashcards.add(newCurrentFlashcard);

        indicateChoice(false);

        int newSpeed = gameView.getBallSpeed() + 5;//2;
        gameView.setBallSpeed(newSpeed);

        gameView.setBall1X(i);
        gameView.setBall2X(i);
        gameView.checkX();
    }

    private Flashcard getNextFlashcard(List<Flashcard> flashcards) {
        Flashcard nextFlashcard;
        if (usedFlashcards.size() == flashcards.size()) return null;
        do {
            nextFlashcard = flashcards.get(random.nextInt(flashcards.size()));
        } while (usedFlashcards.contains(nextFlashcard) && usedFlashcards.size()<flashcards.size());

        return nextFlashcard;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(gyroscopeEventListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(gyroscopeEventListener);
    }


    public void indicateChoice(boolean isCorrect) {
        final int originalColor = frameLayout.getSolidColor(); // Get the original background color
        int newColor = isCorrect ? Color.GREEN : Color.RED;
        Log.d("icon ", "icony " + playerIcon.getY() + "iconx " + playerIcon.getX() + "height " + frameLayout.getHeight() + "width " + frameLayout.getWidth() );
        frameLayout.setBackgroundColor(newColor);
        new Handler().postDelayed(() -> frameLayout.setBackgroundColor(originalColor), 500); // 500ms delay
    }


    public ImageView getPlayerIcon() {
        return playerIcon;
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//
//        }
//    }
}
